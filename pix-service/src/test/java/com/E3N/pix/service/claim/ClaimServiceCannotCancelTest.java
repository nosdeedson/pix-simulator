package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;
import com.E3N.pix.service.mocks.ClaimServiceMock;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.when;

public class ClaimServiceCannotCancelTest {

    static final String EXPECTED_DONOR_CANNOT = "Donor cannot cancel this type of claim.";
    static final String EXPECTED_CLAIMER_CANNOT = "Claimer cannot cancel this type of claim.";
    static final String EXPECTED_CLAIM_STATUS_NOT_ALLOW = "Claim's status does not permit to cancel.";
    static final String EXPECTED_RESOLUTION_PERIOD_NOT_ENDED = "Claim's resolution period must have ended to cancel as default operation.";


    @Test //ok
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnParticipantExclusion() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var notification = service.cannotCancel(claim, ClaimOperationReason.PARTICIPANT_EXCLUSION, claim.getClaimerParticipant());
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertEquals("The use of 'PARTICIPANT_EXCLUSION is exclusive for DICT.", notification.getDetail());
    }

    @Test //ok
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimOperationNotAllowed() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.USER_REQUESTED, claim.getClaimerParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_CLAIM_STATUS_NOT_ALLOW, result.getDetail());
    }

    @Test //ok
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnResolutionPeriodNotEnded() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.OPEN);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_RESOLUTION_PERIOD_NOT_ENDED, result.getDetail());
    }

    @Test //ok
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.ACCOUNT_CLOSURE, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test //ok
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimerCannot1() {
        var today = Instant.now();
        var eightDaysAgo = today.minus(8, ChronoUnit.DAYS);
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(Instant::now).thenReturn(eightDaysAgo);
            var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
            var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
            var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
            var service = new ClaimService();
            mockedStatic.when(Instant::now).thenReturn(today);
            var result = service.cannotCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getClaimerParticipant());
            Assertions.assertInstanceOf(Notification.class, result);
            Assertions.assertEquals(EXPECTED_CLAIMER_CANNOT, result.getDetail());
        }
    }

    @Test // OK
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot1() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot2() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.FRAUD, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot3() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.FRAUD, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot4() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot5() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RFB_VALIDATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimStatusNotAllowed() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.WAITING_RESOLUTION);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_CLAIM_STATUS_NOT_ALLOW, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimStatusNotAllowed1() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(StatusClaim.WAITING_RESOLUTION);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_CLAIM_STATUS_NOT_ALLOW, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimStatusNotAllowed2() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(StatusClaim.OPEN);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_CLAIM_STATUS_NOT_ALLOW, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnClaimStatusNotAllowed3() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_CLAIM_STATUS_NOT_ALLOW, result.getDetail());
    }

    @Test
    void givenValidClaim_whenCalling_cannotCancel_shouldReturnDonorCannot6() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(StatusClaim.WAITING_RESOLUTION);
        var service = new ClaimService();
        var result = service.cannotCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getDonorParticipant());
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(EXPECTED_DONOR_CANNOT, result.getDetail());
    }
}
