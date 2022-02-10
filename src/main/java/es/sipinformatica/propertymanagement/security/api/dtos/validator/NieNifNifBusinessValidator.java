package es.sipinformatica.propertymanagement.security.api.dtos.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.sipinformatica.propertymanagement.security.domain.services.UtilService;

public class NieNifNifBusinessValidator implements ConstraintValidator<ValidateNieNifNifBusiness, String> {

    @Override
    public void initialize(ValidateNieNifNifBusiness constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {       
        return UtilService.validateNieNifNifBusiness(value);

    }

}
