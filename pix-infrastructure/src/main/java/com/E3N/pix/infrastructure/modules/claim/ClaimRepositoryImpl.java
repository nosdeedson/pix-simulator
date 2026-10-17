package com.E3N.pix.infrastructure.modules.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
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

    @Override
    public List<Claim> findClaimsAsClaimer(String participant, StatusClaim statusClaim, TypeClaim typeClaim, Instant modifiedAfter, Instant modifiedBefore, int limit) {
        return claimRepository.findClaimsAsDonor(participant, statusClaim, typeClaim, modifiedAfter, modifiedBefore, PageRequest.of(0, limit))
            .stream().map(ClaimJPAEntity::from).toList();
    }

    @Override
    public List<Claim> findClaimsAsDonor(String participant, StatusClaim statusClaim, TypeClaim typeClaim, Instant modifiedAfter, Instant modifiedBefore, int limit) {
        return claimRepository.findClaimsAsDonor(participant, statusClaim, typeClaim, modifiedAfter, modifiedBefore, PageRequest.of(0, limit))
                .stream().map(ClaimJPAEntity::from).toList();
    }
}
