package com.E3N.pix.infrastructure.modules.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClaimRepositoryImpl implements ClaimRepositoryInterface {

    private final ClaimJPARepository claimRepository;

    public ClaimRepositoryImpl(ClaimJPARepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    public Claim save(Claim entity) {
        var claimJPA = ClaimJPAEntity.from(entity);
        claimJPA = this.claimRepository.save(claimJPA);
        return ClaimJPAEntity.from(claimJPA);
    }

    @Override
    public void delete(UUID id) {
        this.claimRepository.deleteById(id.toString());
    }

    @Override
    public Optional<Claim> findById(UUID id) {
        var claimJPA = this.claimRepository.findById(id.toString());
        return claimJPA.map(ClaimJPAEntity::from);
    }

    @Override
    public List<Claim> findAll() {
        return this.claimRepository.findAll()
                .stream()
                .map(ClaimJPAEntity::from)
                .toList();
    }

    @Override
    public Claim update(Claim entity) {
        var claimJPA = ClaimJPAEntity.from(entity);
        return ClaimJPAEntity.from(this.claimRepository.save(claimJPA));
    }
}
