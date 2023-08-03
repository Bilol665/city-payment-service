package uz.pdp.citypaymentservice.domain.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {
    private String number;
    private String holderName;
    private Integer createdDate;
    private Integer expiredDate;
    private UUID ownerId;
}
