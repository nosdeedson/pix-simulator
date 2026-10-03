package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class ClaimServiceTest extends UniTest {

    @InjectMocks
    private ClaimService claimService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var claimer = OwnerMock.mockOwner();
        var acc = claimer.getAccounts().getFirst();
        var donor = OwnerMock.mockOwner();
        var key = donor.getAccounts().getFirst().getEntryKeys().getFirst();
        var result = ClaimService.create(acc, key, donor.getAccounts().getFirst().getParticipant().getParticipant(),
                TypeClaim.PORTABILITY, claimer
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertFalse(result.getNotification().hasError());
    }

    @Test
    void givenInvalidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var claimer = OwnerMock.mockOwner();
        var acc = claimer.getAccounts().getFirst();
        var donor = OwnerMock.mockOwner();
        var key = donor.getAccounts().getFirst().getEntryKeys().getFirst();
        var result = ClaimService.create(acc, key, donor.getAccounts().getFirst().getParticipant().getParticipant(),
                TypeClaim.OWNERSHIP, claimer
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertEquals("This kind of Key must open a portability", result.getNotification().getViolations().getFirst().reason());
    }

    @Test
    void givenRightTypeClaim_whenCalling_validateTypeClaim_shouldReturnNull(){
        var claimer = OwnerMock.mockOwner();
        var donor = OwnerMock.mockOwner();
        var claimerParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var result = ClaimService.validateTypeClaim(claimer, donor, TypeClaim.OWNERSHIP, claimerParticipant);
        Assertions.assertNull(result);
    }

    @Test
    void givenWrongTypeClaim_whenCalling_validateTypeClaim_shouldReturnNull(){
        var claimer = OwnerMock.mockOwner();
        var claimerParticipant = RandomParticipant.getParticipant();
        var result = ClaimService.validateTypeClaim(claimer, claimer, TypeClaim.PORTABILITY, claimerParticipant);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Type of claim must be Ownership", result.getDetail());
    }
}
