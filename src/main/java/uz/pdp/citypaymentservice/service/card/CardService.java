package uz.pdp.citypaymentservice.service.card;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
import uz.pdp.citypaymentservice.domain.dto.P2PDto;
import uz.pdp.citypaymentservice.domain.dto.UserReadDto;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;
import uz.pdp.citypaymentservice.domain.entity.card.CardType;
import uz.pdp.citypaymentservice.exception.DataNotFoundException;
import uz.pdp.citypaymentservice.exception.NotEnoughBalance;
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
        UserReadDto userDto = authService.loadByName(principal.getName(), principal);
        try {
            card.setType(CardType.valueOf(cardDto.getType()));
        } catch (Exception e) {
            throw new NotEnoughBalance("Invalid type!");
        }
        card.setOwnerId(userDto.getId());
        card.setBalance(10000000.0);
        mailService.saveCardMessage(userDto.getEmail(),card.getNumber(),card.getBalance());
        return cardRepository.save(card);
    }

    public List<CardEntity>getCard(Principal  principal){
        UserReadDto userReadDto = authService.loadByName(principal.getName(), principal);
        return cardRepository.findCardEntitiesByOwnerId(userReadDto.getId());
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

    public CardEntity fillBalance(UUID cardId,Double balance,Principal principal) {
        CardEntity card = cardRepository.getReferenceById(cardId);
        card.setBalance(balance);
        UserReadDto userReadDto = authService.loadById(card.getOwnerId(), principal);
        mailService.fillBalanceMessage(userReadDto.getEmail(), card.getNumber(), card.getBalance());
        return cardRepository.save(card);
    }

    public CardEntity peerToPeer(P2PDto p2PDto, Principal principal){

        CardEntity senderCard = cardRepository.findCardEntityByNumber(p2PDto.getSender())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        CardEntity receiverCard= cardRepository.findCardEntityByNumber(p2PDto.getReceiver())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        UserReadDto senderUser=authService.loadById(senderCard.getOwnerId(),principal);
        UserReadDto receiverUser=authService.loadById(receiverCard.getOwnerId(),principal);
        if (senderCard.getBalance()< p2PDto.getCash()){
            throw new NotEnoughBalance("not enough money");
        }

        senderCard.setBalance(senderCard.getBalance()- p2PDto.getCash());
        receiverCard.setBalance(receiverCard.getBalance()+ p2PDto.getCash());

        mailService.receiverMessage(receiverUser.getEmail(), receiverCard.getBalance(), senderCard.getNumber());
        mailService.senderMessage(senderUser.getEmail(),senderCard.getBalance(),receiverCard.getNumber());

        cardRepository.save(receiverCard);
        return cardRepository.save(senderCard);
    }

    public UUID getUserByCard(String card) {
        return cardRepository.findCardEntityByNumber(card).orElseThrow(() -> new DataNotFoundException("Card not found")).getOwnerId();
    }

    public String getById(UUID cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new DataNotFoundException("Card not found!")).getNumber();
    }
}
