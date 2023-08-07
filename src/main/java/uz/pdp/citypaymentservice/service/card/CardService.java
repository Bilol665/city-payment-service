package uz.pdp.citypaymentservice.service.card;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.dto.UserDto;
import uz.pdp.citypaymentservice.domain.dto.UserReadDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.repository.CardRepository;
import uz.pdp.citypaymentservice.service.mail.MailService;
import uz.pdp.citypaymentservice.service.user.AuthService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final MailService mailService;

    public CardEntity saveCard(CardDto cardDto,  Principal principal){
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        UserReadDto userDto = authService.loadById(principal.getName());
        card.setOwnerId(userDto.getId());
        card.setBalance(0.0);
        mailService.saveCardMessage(userDto.getEmail(),card.getNumber(),card.getBalance());
        return cardRepository.save(card);
    }

    public List<CardEntity>getCard(UUID ownerId){
        return cardRepository.findCardEntitiesByOwnerId(ownerId);
    }

    public void deleteCardById(UUID cardId){
        cardRepository.deleteById(cardId);
    }


    public CardEntity updateCardById(UUID id,CardDto cardDto){
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Card Not found"));
        modelMapper.map(cardDto,card);
        return cardRepository.save(card);
    }
}
