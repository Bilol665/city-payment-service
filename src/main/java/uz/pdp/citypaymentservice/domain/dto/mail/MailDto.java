package uz.pdp.citypaymentservice.domain.dto.mail;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MailDto {
    private String message;
    private String  email;
}
