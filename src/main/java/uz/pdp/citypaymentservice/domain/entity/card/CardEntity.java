package uz.pdp.citypaymentservice.domain.entity.card;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.citypaymentservice.domain.entity.BaseEntity;

import java.util.UUID;

@Entity(name = "card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardEntity extends BaseEntity {
    private String number;
    private String holderName;
    private Integer pinCode;
    private Integer createdDate;
    private Integer expiredDate;
    private Double balance;
    private UUID ownerId;
}
