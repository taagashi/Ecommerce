package br.com.thaua.Ecommerce.services.returnTypeUsers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExtractTypeUserContextHolder {
    public UsersEntity extractUser() {
        log.info("EXTRACT TYPE USER CONTEXT HOLDER - EXTRACT USER");
        MyUserDetails myUserDetails = extractMyUserDetails();
        return myUserDetails.getUser();
    }

    private MyUserDetails extractMyUserDetails() {
        log.info("EXTRACT TYPE USER CONTEXT HOLDER - EXTRACT MY USER DETAILS");
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
