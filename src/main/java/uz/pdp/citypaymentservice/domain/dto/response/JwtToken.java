package uz.pdp.citypaymentservice.domain.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtToken{
    private String accessToken;
    private String refreshToken;
}
