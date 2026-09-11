package com.E3N.pix.infrastructure.modules.owner;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OwnerRepositoryImpl implements OwnerRepositoryInterface {

    private final OwnerJPARepository ownerRepository;

    public OwnerRepositoryImpl(OwnerJPARepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Optional<Owner> findByTaxIdNumber(String taxIdNumber) {
        var ownerJPA = this.ownerRepository.findByTaxIdNumber(taxIdNumber);
        return ownerJPA.map(OwnerJPAEntity::from);
    }

    @Override
    public Optional<Owner> findByKey(String key) {
        var ownerJPA = this.ownerRepository.findByKey(key);
        return ownerJPA.map(OwnerJPAEntity::from);
    }

    @Override
    public Owner save(Owner owner) {
        OwnerJPAEntity entity = OwnerJPAEntity.from(owner);
        entity = this.ownerRepository.save(entity);
        return OwnerJPAEntity.from(entity);
    }

    @Override
    public void delete(UUID id) {
        this.ownerRepository.deleteById(id.toString());
    }

    @Override
    public Optional<Owner> findById(UUID id) {
        var ownerJPA = this.ownerRepository.findById(id.toString());
        return ownerJPA.map(OwnerJPAEntity::from);
    }

    @Override
    public List<Owner> findAll() {
        var ownersJPA = ownerRepository.findAll();
        return ownersJPA.stream().map(OwnerJPAEntity::from)
                .toList();
    }

    @Override
    public Owner update(Owner owner) {
        var ownerJPA = ownerRepository.save(OwnerJPAEntity.from(owner));
        return OwnerJPAEntity.from(ownerJPA);
    }
}
