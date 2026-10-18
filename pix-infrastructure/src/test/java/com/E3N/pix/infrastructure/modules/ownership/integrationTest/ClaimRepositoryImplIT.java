package com.E3N.pix.infrastructure.modules.ownership.integrationTest;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.IntegrationTest;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.shared.utils.DateHelper;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ClaimRepositoryImplIT extends IntegrationTest {

    final String donorParticipant = "8HTVVWKM";

    Claim claim = Claim.getInstance(
            UUID.randomUUID().toString(),
            RandomParticipant.getParticipant(),
            RandomParticipant.getParticipant(),
            RandomKeysMock.randomEmails(),
            RandomCNPJMock.getRandomCNPJ(),
            TypeClaim.PORTABILITY,
            TypeKey.EMAIL,
            TypePerson.LEGAL_PERSON
    );

    Claim claimDonorSituation = Claim.getInstance(
            UUID.randomUUID().toString(),
            RandomParticipant.getParticipant(),
            donorParticipant,
            RandomKeysMock.randomEmails(),
            RandomCNPJMock.getRandomCNPJ(),
            TypeClaim.PORTABILITY,
            TypeKey.EMAIL,
            TypePerson.LEGAL_PERSON
    );

    @Autowired
    private ClaimRepositoryImpl repository;

    @Test
    void givenInjection_shouldRepositoryBeInstantiated() {
        Assertions.assertNotNull(repository);
    }

    @Test
    void givenValidClaim_whenCallingSave_shouldReturnClaimJPA() {
        var result = repository.save(claim);
        Assertions.assertInstanceOf(Claim.class, result);
    }

    @Test
    void givenValidId_whenCalling_delete_shouldDelete() {
        var result = repository.save(claim);
        var found = repository.findById(result.getId()).get();
        Assertions.assertInstanceOf(Claim.class, found);
        repository.delete(found.getId());
        var optionalClaim = repository.findById(result.getId());
        Assertions.assertTrue(optionalClaim.isEmpty());
    }

    @Test
    void shouldFindAllClaims() {
        var all = repository.findAll();
        Assertions.assertTrue(all.isEmpty());
        repository.save(claim);
        all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
        Assertions.assertEquals(1, all.size());
    }

    @Test
    void givenValidClaim_whenCalling_update_shouldUpdate() {
        var expectedStatus = StatusClaim.CANCELLED;
        Claim c = claim;
        repository.save(c);
        Claim found = repository.findById(c.getId()).get();
        found.updateStatus(expectedStatus);
        found = repository.update(found);
        Assertions.assertEquals(expectedStatus, found.getStatus());
    }

    @Test
    void givenNoExistentValues_whenCalling_findClaims_shouldReturnEmpty(){
        repository.save(claim);
        var expectedParticipant = "ANY-PARTICIPANT";
        var expectedStatusClaim = StatusClaim.OPEN;
        var expectedType = TypeClaim.PORTABILITY;
        var expectedModifiedAfter = DateHelper.getDateFrom(RandomDateMock.getRandomDate(), "dd/MM/yyyy");
        var expectedModifiedBefore = DateHelper.getDateFrom(RandomDateMock.getRandomDate(), "dd/MM/yyyy");
        var results = repository.findClaims(expectedParticipant, false, false, expectedStatusClaim, expectedType, expectedModifiedAfter, expectedModifiedBefore, 200);
        Assertions.assertTrue(results.isEmpty());
    }

    @Test
    void givenExistentValues_whenCalling_findClaims_shouldReturnAsClaimer(){
        repository.save(claim);
        var expectedParticipant = claim.getClaimerParticipant();
        var expectedStatusClaim = claim.getStatus();
        var expectedType = claim.getType();
        var expectedModifiedAfter = claim.getLastModified().minus(5, ChronoUnit.DAYS);
        var expectedModifiedBefore = claim.getLastModified().plus(5, ChronoUnit.DAYS);
        var results = repository.findClaims(expectedParticipant, false, true, expectedStatusClaim, expectedType, expectedModifiedAfter, expectedModifiedBefore, 200);
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(expectedParticipant, results.getFirst().getClaimerParticipant());
    }

    @Test
    void givenExistentValues_whenCalling_findClaims_shouldReturnListAsDonor(){
        repository.save(claimDonorSituation);
        var expectedParticipant = donorParticipant;
        var expectedStatusClaim = claimDonorSituation.getStatus();
        var expectedType = claimDonorSituation.getType();
        var expectedModifiedAfter = claimDonorSituation.getLastModified().plus(5, ChronoUnit.DAYS);
        var expectedModifiedBefore = claimDonorSituation.getLastModified().minus(5, ChronoUnit.DAYS);
        var results = repository.findClaims(expectedParticipant, true, false, expectedStatusClaim, expectedType, expectedModifiedAfter, expectedModifiedBefore, 200);
        Assertions.assertEquals(expectedParticipant, results.getFirst().getDonorParticipant());
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void givenNullValuesWithJustParticipant_whenCalling_findClaims_shouldReturnListClaims(){
        repository.save(claim);
        var expectedParticipant = claim.getClaimerParticipant();
        var results = repository.findClaims(expectedParticipant, false, false, null, null, null, null, 20);
        Assertions.assertEquals(1, results.size());
    }
}
