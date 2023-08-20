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

    @PostMapping("/p2p")
    public ResponseEntity<CardEntity>p2p(
            Principal principal,
            @RequestBody P2PDto p2PDto
    ){
        return  ResponseEntity.ok(cardService.peerToPeer(p2PDto,principal));
    }
    @PostMapping("/save")
    public ResponseEntity<CardEntity> save(
            Principal principal,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(cardService.saveCard(cardDto,principal));
    }
    @GetMapping("/get-b-user/{card}")
    public ResponseEntity<UUID> get(
            @PathVariable String card
    ) {
        return ResponseEntity.ok(cardService.getUserByCard(card));
    }
    @GetMapping("/get/{cardId}")
    public ResponseEntity<String> getById(
            @PathVariable UUID cardId
    ) {
        return ResponseEntity.ok(cardService.getById(cardId));
    }
    @GetMapping("/get")
    public ResponseEntity<List<CardEntity>>get(
            Principal principal
    ){
        return ResponseEntity.ok(cardService.getCard(principal));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CardEntity>update(
            @PathVariable UUID id,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(cardService.updateCardById(id,cardDto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus>delete(
            @PathVariable UUID id
    ){
        cardService.deleteCardById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/fill/{id}")
    public ResponseEntity<CardEntity>fill(
            Principal principal,
            @PathVariable UUID id,
            @RequestParam Double balance
    ){
        return ResponseEntity.ok(cardService.fillBalance(id,balance,principal));
    }
}
