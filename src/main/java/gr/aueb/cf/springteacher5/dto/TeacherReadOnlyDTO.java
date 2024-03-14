package gr.aueb.cf.springteacher5.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherReadOnlyDTO extends BaseDTO {
    private String firstname;
    private String lastname;

    public TeacherReadOnlyDTO(Long id, String firstname, String lastname) {
        setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
