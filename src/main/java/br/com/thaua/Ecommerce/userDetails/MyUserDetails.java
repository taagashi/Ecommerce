package br.com.thaua.Ecommerce.userDetails;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    @Getter
    private Long id;
    private String email;
    private String password;
    private String role;
    @Getter
    private UsersEntity user;
    @Getter
    private Object typeUser;

    public MyUserDetails(Long id, String email, String password, String role, UsersEntity user, Object typeUser) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = "ROLE_" + role;
        this.user = user;
        this.typeUser = typeUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
