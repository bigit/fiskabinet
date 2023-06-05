package ru.antelit.fiskabinet.validator;



import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = InnUniqueValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InnUniqueConstraint {
    String message() default "Организация с таким ИНН уже существует";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
