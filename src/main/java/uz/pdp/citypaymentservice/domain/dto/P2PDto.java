package uz.pdp.citypaymentservice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class P2PDto {
    private String sender;
    private String receiver;
    private Double cash;
}
