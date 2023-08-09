package uz.pdp.citypaymentservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.pdp.citypaymentservice.domain.entity.card.CardType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {
   @NotBlank(message = "number is blank please try again")
    private String number;
    @NotBlank(message = "Holder name is blank please try again")
    private String holderName;
    @NotNull(message = "expired date card is blank please try again")
    private Integer expireDate;
    private Integer pinCode;
    private String type;
}
