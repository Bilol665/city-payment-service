package uz.pdp.citypaymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.service.card.CardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1/card")
public class TestController {
    private final CardService cardService;
    @PostMapping("/save/{id}")
    public ResponseEntity<CardEntity>save(
            @PathVariable UUID id,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(cardService.saveCard(cardDto,id));
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
}
