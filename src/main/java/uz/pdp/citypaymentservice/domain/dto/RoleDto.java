package uz.pdp.citypaymentservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleDto {
    @NotBlank(message = "role name cannot be blank")
    private String role;
}
