package de.jaehrig.akalj.infrastructure;

import de.jaehrig.akalj.domain.model.AddressEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    Optional<AddressEntity> findByStreetAndNumber(String street, String name);

}
