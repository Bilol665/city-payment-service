package uz.pdp.citypaymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.service.card.CardService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1/card")
public class TestController {
    private final CardService cardService;
    @PostMapping("/save")
    public ResponseEntity<CardEntity>save(
             Principal principal,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(cardService.saveCard(cardDto,principal));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<CardEntity>>get(
        @PathVariable UUID id
    ){
       return ResponseEntity.ok(cardService.getCard(id));
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

    @PutMapping("/p2p")
    public ResponseEntity<CardEntity>p2p(
            @RequestParam String sender,
            @RequestParam String receiver,
            @RequestParam Double balance
    ){
        return  ResponseEntity.ok(cardService.PeerToPeer(sender,receiver,balance));
    }
}
