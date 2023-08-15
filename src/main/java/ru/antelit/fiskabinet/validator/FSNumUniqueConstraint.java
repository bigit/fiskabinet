package ru.antelit.fiskabinet.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = InnValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FSNumUniqueConstraint {
    String message() default "Организация с таким ИНН уже существует";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
