package uz.pdp.citypaymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citypaymentservice.domain.entity.card.CardEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    List<CardEntity> findCardEntitiesByOwnerId(UUID id);

}
