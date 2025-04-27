package br.com.thaua.Ecommerce.services.returnTypeUsers;

import br.com.thaua.Ecommerce.domain.entity.UsersEntity;
import br.com.thaua.Ecommerce.userDetails.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class ExtractTypeUserContextHolder {
    public static UsersEntity extractUser() {
        log.info("EXTRACT TYPE USER CONTEXT HOLDER - EXTRACT USER");
        MyUserDetails myUserDetails = extractMyUserDetails();
        return myUserDetails.getUser();
    }

    private static MyUserDetails extractMyUserDetails() {
        log.info("EXTRACT TYPE USER CONTEXT HOLDER - EXTRACT MY USER DETAILS");
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
