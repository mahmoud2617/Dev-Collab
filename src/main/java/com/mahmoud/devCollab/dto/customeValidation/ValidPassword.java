package com.mahmoud.devCollab.dto.customeValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Password is required.")
@Size(min = 8, max = 250, message = "Password must be between {min} and {max} characters.")
@Constraint(validatedBy = {})
public @interface ValidPassword {
    String message() default "Invalid password.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
