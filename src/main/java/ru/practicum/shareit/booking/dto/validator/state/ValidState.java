package ru.practicum.shareit.booking.dto.validator.state;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStateImpl.class)
public @interface ValidState {
    String message() default "";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
