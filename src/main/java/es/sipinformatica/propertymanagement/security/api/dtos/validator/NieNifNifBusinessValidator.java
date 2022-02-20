package es.sipinformatica.propertymanagement.security.api.dtos.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.sipinformatica.propertymanagement.security.domain.services.UtilService;

public class NieNifNifBusinessValidator implements ConstraintValidator<ValidateNieNifNifBusiness, String> {

    @Override
    public void initialize(ValidateNieNifNifBusiness constraintAnnotation) {
         // default implementation ignored

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {       
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{validate.nie.nif.nif.business.null}").addConstraintViolation();
            return false;           
        }
        return UtilService.validateNieNifNifBusiness(value);

    }

}
