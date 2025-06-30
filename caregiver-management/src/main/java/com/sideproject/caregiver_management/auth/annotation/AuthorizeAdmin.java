package com.sideproject.caregiver_management.auth.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.validateAccessToUser(#userId) && @authorizationService.validateAdmin()")
public @interface AuthorizeAdmin {
}
