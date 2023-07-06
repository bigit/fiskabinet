package ru.antelit.fiskabinet.validator;


import ru.antelit.fiskabinet.service.OrgService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InnUniqueValidator implements ConstraintValidator<InnUniqueConstraint, String> {

    private OrgService orgService;

    public InnUniqueValidator(OrgService orgService) {
        this.orgService = orgService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !orgService.checkInnExists(value);
    }
}
