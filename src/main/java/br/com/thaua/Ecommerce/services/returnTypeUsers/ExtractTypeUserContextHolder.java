package br.com.thaua.Ecommerce.services.returnTypeUsers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class ExtractTypeUserContextHolder {
    public static UsersEntity extractUser() {
        MyUserDetails myUserDetails = extractMyUserDetails();
        return myUserDetails.getUser();
    }

    private static MyUserDetails extractMyUserDetails() {
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
