package com.mahmoud.devCollab.dto.customeValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Username is required.")
@Size(min = 4, max = 150, message = "Username must be in range 4-150 character.")
@Pattern(
    regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,149}$",
    message = "Username must be in range 4-150 character, start with a letter, contain only letters, numbers, and underscore."
)
@Constraint(validatedBy = {})
public @interface ValidUsername {
    String message() default "Invalid username.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
