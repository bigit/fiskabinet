package ru.antelit.fiskabinet.validator;


import org.springframework.beans.factory.annotation.Autowired;
import ru.antelit.fiskabinet.service.OrgService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InnUniqueValidator implements ConstraintValidator<InnUniqueConstraint, String> {

    @Autowired
    private OrgService orgService;

    @Override
    public void initialize(InnUniqueConstraint annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !orgService.checkInnExists(value);
    }
}
