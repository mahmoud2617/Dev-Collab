package com.mahmoud.devCollab.dto.customeValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Username is required.")
@Constraint(validatedBy = {})
public @interface ValidUsername {
    String message() default "Invalid username.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
