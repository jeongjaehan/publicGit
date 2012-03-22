package com.paran.bizportal.validation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;

@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TelephoneValidator.class)
@Size(min = 12, max = 13)
public @interface Telephone {
	String message() default "{Telephone}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
