package gr.aueb.cf.springteacher5.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {
    @NotNull
    private Long id;
}
