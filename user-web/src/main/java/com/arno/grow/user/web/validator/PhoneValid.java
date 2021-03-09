package com.arno.grow.user.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/9 13:04
 * @Description:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidAnnotationValidator.class)
public @interface PhoneValid {
    String message() default "Illegal phone number";
    String pattern() default "^[1][3,4,5,6,7,8,9]\\d{9}$";
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
