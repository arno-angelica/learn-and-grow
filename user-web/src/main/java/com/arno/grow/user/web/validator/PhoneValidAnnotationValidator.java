package com.arno.grow.user.web.validator;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/9 13:06
 * @Description:
 */
public class PhoneValidAnnotationValidator implements ConstraintValidator<PhoneValid, String> {
    private String message;
    private String PATTERN;

    @Override
    public void initialize(PhoneValid phoneValid) {
        this.message = phoneValid.message();
        this.PATTERN = phoneValid.pattern();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(phone)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message);
            return false;
        }
        boolean isValid = Pattern.compile(PATTERN).matcher(phone).matches();
        if (!isValid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message);
        }
        return isValid;
    }
}
