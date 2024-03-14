package gr.aueb.cf.springteacher5.validator;

import gr.aueb.cf.springteacher5.dto.TeacherInsertDTO;
import gr.aueb.cf.springteacher5.dto.TeacherUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TeacherUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherUpdateDTO teacherUpdateDTO = (TeacherUpdateDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (teacherUpdateDTO.getFirstname().length() < 3 || teacherUpdateDTO.getFirstname().length() > 50) {
            errors.reject("firstname", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (teacherUpdateDTO.getLastname().length() < 3 || teacherUpdateDTO.getLastname().length() > 50) {
            errors.reject("lastname", "size");
        }

        // if (teacherService.isUsernameExists(teacherUpdateDTO.getUsername())) {
        //  errors.reject("username", "exists");
        // }
    }
}
