package com.sideproject.caregiver_management.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    enum Role {
        ROLE_GUEST, ROLE_USER, ROLE_ADMIN
    }
    Role value();
}
