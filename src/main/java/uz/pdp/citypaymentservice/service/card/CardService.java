package uz.pdp.citypaymentservice.service.card;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citypaymentservice.domain.dto.CardDto;
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
        UserReadDto userDto = authService.loadByName(principal.getName());
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
        UserReadDto userReadDto = authService.loadByName(principal.getName());
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

    public CardEntity fillBalance(UUID cardId,Double balance,Principal principal){
        UserReadDto userReadDto = authService.loadByName(principal.getName());
        CardEntity card = cardRepository.getReferenceById(cardId);
            card.setBalance(balance);
            mailService.fillBalanceMessage(userReadDto.getEmail(),card.getNumber(),card.getBalance());
            return cardRepository.save(card);
    }

    public CardEntity peerToPeer(Principal sender, String receiver, Double amount){
        UserReadDto senderUser = authService.loadByName(sender.getName());

        CardEntity senderCard = cardRepository.findCardEntityByOwnerId(senderUser.getId())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        CardEntity receiverCard= cardRepository.findCardEntityByNumber(receiver)
                .orElseThrow(()->new DataNotFoundException("Card not found"));

        UserReadDto receiverUser = authService.loadById(receiverCard.getOwnerId());


        if (senderCard.getBalance()<amount){
            throw new NotEnoughBalance("not enough money");
        }

        senderCard.setBalance(senderCard.getBalance()-amount);
        receiverCard.setBalance(receiverCard.getBalance()+amount);

        mailService.receiverMessage(receiverUser.getEmail(), receiverCard.getBalance(), senderCard.getNumber());
        mailService.senderMessage(senderUser.getEmail(),senderCard.getBalance(),receiverCard.getNumber());

        cardRepository.save(receiverCard);
        return cardRepository.save(senderCard);
    }
    public CardEntity peerToPeer(Principal sender, UUID receiver, Double amount) {
        UserReadDto senderUser = authService.loadByName(sender.getName());
        UserReadDto userReadDto = authService.loadById(receiver);

        CardEntity senderCard = cardRepository.findCardEntityByOwnerId(senderUser.getId())
                .orElseThrow(() -> new DataNotFoundException("Card not found"));
        CardEntity receiverCard = cardRepository.findCardEntityByOwnerId(receiver)
                .orElseThrow(() -> new DataNotFoundException("Card not found"));


        if (senderCard.getBalance() < amount) {
            throw new NotEnoughBalance("not enough money");
        }

        senderCard.setBalance(senderCard.getBalance() - amount);
        receiverCard.setBalance(receiverCard.getBalance() + amount);

        mailService.receiverMessage(userReadDto.getEmail(), amount, senderCard.getNumber());
        mailService.senderMessage(senderUser.getEmail(), amount, receiverCard.getNumber());

        cardRepository.save(receiverCard);
        return cardRepository.save(senderCard);
    }
}
