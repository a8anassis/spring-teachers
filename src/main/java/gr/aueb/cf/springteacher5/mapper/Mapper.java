package gr.aueb.cf.springteacher5.mapper;


import gr.aueb.cf.springteacher5.dto.TeacherInsertDTO;
import gr.aueb.cf.springteacher5.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.springteacher5.dto.TeacherUpdateDTO;
import gr.aueb.cf.springteacher5.model.Teacher;

public class Mapper {

    private Mapper() {}

    public static Teacher mapToTeacher(TeacherInsertDTO dto) {
        return new Teacher(null, dto.getFirstname(), dto.getLastname());
    }

    public static Teacher mapToTeacher(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname());
    }

    public static TeacherReadOnlyDTO mapToReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname());
    }


}
