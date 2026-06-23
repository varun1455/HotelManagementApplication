package com.project.stayEase.security;

import com.project.stayEase.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class SecurityUtils {

    public User getCurrentuser(){
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }

    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        assert user != null;
        return user.getId();
    }
}
