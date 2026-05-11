package com.mahmoud.devCollab.dto.customeValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Email is required.")
@Email
@Size(max = 250, message = "Email must not be more than 250 character.")
@Constraint(validatedBy = {})
public @interface ValidEmail {
    String message() default "Invalid username.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
