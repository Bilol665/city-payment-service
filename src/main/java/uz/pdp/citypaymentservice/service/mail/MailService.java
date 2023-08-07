package uz.pdp.citypaymentservice.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.citypaymentservice.domain.dto.UserDto;
import uz.pdp.citypaymentservice.domain.dto.UserReadDto;
import uz.pdp.citypaymentservice.domain.dto.mail.MailDto;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.service.user.AuthService;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final RestTemplate restTemplate;
    @Value("${services.notification-url}")
    private String notificationServiceUrl;
    private final AuthService authService;
    private void sendMail(String email, String message) {
         UserReadDto dto = authService.loadByName(email);
        MailDto mailDto = new MailDto(message,dto.getEmail());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailDto> entity = new HttpEntity<>(mailDto,httpHeaders);
        restTemplate.exchange(
                URI.create(notificationServiceUrl + "/send-single"),
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void saveCardMessage(String email,String number,Double Balance) {
        String message = "Your card Successfully added ✅"+ '\n'+
                         "Your card number💳 :" + number + '\n'+
                         "Your balance💵 :" + Balance
                ;
        sendMail(email,message);
    }


    public void fillBalanceMessage(String email,String number,Double Balance) {
        String message = "Successfully incoming your balance ✅"+ '\n'+
                "Your card number💳 :" + number + '\n'+
                "Your balance💵 :" + Balance
                ;
        sendMail(email,message);
    }

    public void receiverMessage(String email,Double money,String number){
        String message= "Your card has been credited"+'\n'+
                        "Sender card💳 :" +number + '\n' +
                        "amount of money💵 :" + money;
        sendMail(email,message);
    }


    public void senderMessage(String email,Double money,String number){
        String message= "Money has been transferred from your card to another card"+'\n'+
                "Receiver card💳 :" +number + '\n' +
                "amount of money💵 :" + money;
        sendMail(email,message);
    }
}
