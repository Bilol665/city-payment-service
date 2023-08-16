package uz.pdp.citypaymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.dto.P2PDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.service.card.CardService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1")
public class PaymentController {
    private final CardService cardService;


    @PutMapping("/p2p")
    public ResponseEntity<CardEntity>p2p(
            Principal principal,
            @RequestBody P2PDto p2PDto
    ){
        return  ResponseEntity.ok(cardService.peerToPeer(p2PDto,principal));
    }
//    @PutMapping("/transact")
//    public ResponseEntity<CardEntity>p2p(
//            Principal principal,
//            @RequestParam UUID receiver,
//            @RequestParam Double balance
//    ){
//        return  ResponseEntity.ok(cardService.peerToPeer(principal,receiver,balance));
//    }
}
