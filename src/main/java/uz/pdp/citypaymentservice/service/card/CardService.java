package uz.pdp.citypaymentservice.service.card;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.dto.UserDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.repository.CardRepository;
import uz.pdp.citypaymentservice.service.user.AuthService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public CardEntity saveCard(CardDto cardDto, UUID ownerId){
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        UserDto user = authService.loadById(ownerId);
        user.setId(ownerId);
        card.setOwnerId(user.getId());
        card.setBalance(0.0);
        return cardRepository.save(card);
    }

    public List<CardEntity>getCard(UUID ownerId){
        return cardRepository.findCardEntitiesByOwnerId(ownerId);
    }

    public void deleteCardById(UUID cardId){
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("Card not found"));
        cardRepository.delete(card);
    }


    public CardEntity updateCardById(UUID id,CardDto cardDto){
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Card Not found"));
        modelMapper.map(cardDto,card);
        return cardRepository.save(card);
    }
}
