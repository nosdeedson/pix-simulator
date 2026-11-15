package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;
import com.E3N.pix.service.mocks.ClaimServiceMock;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ClaimServiceCanCancelTest {

    // validation of the exception rules
    @ParameterizedTest
    @CsvSource({
            "FRAUD",
            "RECONCILIATION",
            "RFB_VALIDATION"
    })
    void givenValidClaim_whenCalling_canCancel_claimerCanCancelPortabilityWhenPortabilityAndReasonIsFraud(String reason) {
        ClaimOperationReason reason1 = ClaimOperationReason.valueOf(reason);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, reason1, claim.getClaimerParticipant());
        Assertions.assertTrue(canCancel);
    }

    // validation of the exception rules
    @Test
    void givenValidClaim_whenCalling_canCancel_ownerCanCancelOwnerShipWhenReasonIsFraud() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, ClaimOperationReason.FRAUD, claim.getClaimerParticipant());
        Assertions.assertTrue(canCancel);
    }

    // VALIDATE PARTICIPANT_EXCLUSION
    @ParameterizedTest
    @CsvSource({
            "PORTABILITY",
            "OWNERSHIP"
    })
    void givenValidClaim_whenCalling_canCancel_withReasonPARTICIPANT_EXCLUSION_shouldReturnFalse(String typeClaim) {
        var type = TypeClaim.valueOf(typeClaim);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, type);
        claim.updateStatus(StatusClaim.OPEN);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, ClaimOperationReason.PARTICIPANT_EXCLUSION, claim.getDonorParticipant());
        Assertions.assertFalse(canCancel);
    }

    // validation of portability
    // Validate claim status (OPEN, WAITING_RESOLUTION) CASE (USER_REQUESTED, FRAUD) CLAIMER AND DONOR CAN CANCEL
    @ParameterizedTest
    @CsvSource({
            "OPEN, USER_REQUESTED",
            "WAITING_RESOLUTION, FRAUD"
    })
    void givenValidClaim_whenCalling_canCancel_withStatusClaimOpenOrWaitingResolution_shouldReturnTrue(String statusClaim, String reason) {
        var reasonToCancel = ClaimOperationReason.valueOf(reason);
        var status = StatusClaim.valueOf(statusClaim);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(status);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, reasonToCancel, claim.getDonorParticipant());
        Assertions.assertTrue(canCancel);
    }

    // Validate status claim (WAITING_RESOLUTION) reason RECONCILIATION can not cancel claim
    @Test
    void givenValidClaim_whenCalling_canCancel_withClaimStatusWaitingResolutionAndReasonReconciliation_shouldReturnFalse() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.WAITING_RESOLUTION);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, ClaimOperationReason.RECONCILIATION, claim.getClaimerParticipant());
        Assertions.assertFalse(canCancel);
    }

    // Case reason (ACCOUNT_CLOSURE, RECONCILIATION, RF_VALIDATION) Claimer can cancel claim
    @ParameterizedTest
    @CsvSource({
            "ACCOUNT_CLOSURE",
            "RECONCILIATION",
            "RFB_VALIDATION"
    })
    void givenValidClaim_whenCalling_cannotCancel_ClaimerCanCancelIfReason(String reason) {
        var reasonToCancel = ClaimOperationReason.valueOf(reason);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, reasonToCancel, claim.getClaimerParticipant());
        Assertions.assertTrue(canCancel);
    }

    // Case reason DEFAULT_OPERATION donor can cancel claim
    @Test
    void givenValidClaim_whenCalling_canCancel_ifDefaultOperationDonorCanCancel() {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var result = service.canCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getDonorParticipant());
        Assertions.assertTrue(result);
    }

    // Case reason DEFAULT_OPERATION donor can cancel claim
    @Test
    void givenValidClaim_whenCalling_canCancel_ifDefaultOperationClaimerCANNOTCANCEL() {
        // same rule of the previous test, but this time the claimer is trying to cancel claim he can't
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var service = new ClaimService();
        var result = service.canCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getClaimerParticipant());
        Assertions.assertFalse(result);
    }

    // Case reason (FRAUD, RFB_VALIDATION, RECONCILIATION) and status claim is confirmed claimer can cancel
    @ParameterizedTest
    @CsvSource({
            "FRAUD",
            "RECONCILIATION",
            "RFB_VALIDATION",
    })
    void givenValidClaim_whenCalling_canCancel_ClaimerCanCancelIfReasonAndStatusClaimIsConfirmed(String reason) {
        var reasonToCancel = ClaimOperationReason.valueOf(reason);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, reasonToCancel, claim.getClaimerParticipant());
        Assertions.assertTrue(canCancel);
    }

    // Case reason (FRAUD, RFB_VALIDATION, RECONCILIATION) and status claim is confirmed claimer can cancel
    @ParameterizedTest
    @CsvSource({
            "FRAUD",
            "RECONCILIATION",
            "RFB_VALIDATION",
    })
    void givenValidClaim_whenCalling_canCancel_DONORCANNOTCANCELIfReasonAndStatusClaimIsConfirmed(String reason) {
        var reasonToCancel = ClaimOperationReason.valueOf(reason);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEmails(), TypeKey.EMAIL);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.CONFIRMED);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, reasonToCancel, claim.getDonorParticipant());
        Assertions.assertFalse(canCancel);
    }

    //Validation of OWNERSHIP
    // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
    // if reason is DEFAULT_OPERATION completion period must have expired
    @ParameterizedTest
    @CsvSource({
            "OPEN",
            "WAITING_RESOLUTION",
            "CONFIRMED",
    })
    void givenValidClaimAsOwnership_whenCalling_canCancel_claimerCanCancel(String statusClaim) {
        var status = StatusClaim.valueOf(statusClaim);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(status);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, ClaimOperationReason.USER_REQUESTED, claim.getClaimerParticipant());
        Assertions.assertTrue(canCancel);
    }

    // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
    // if reason is DEFAULT_OPERATION completion period must have expired
    @ParameterizedTest
    @CsvSource({
            "OPEN",
            "WAITING_RESOLUTION",
            "CONFIRMED",
    })
    void givenValidClaimAsOwnership_whenCalling_canCancel_DONORCANOTCANCEL(String statusClaim) {
        // same rule as the previous test but now is the donor trying to cancel
        var status = StatusClaim.valueOf(statusClaim);
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(status);
        var service = new ClaimService();
        var canCancel = service.canCancel(claim, ClaimOperationReason.USER_REQUESTED, claim.getDonorParticipant());
        Assertions.assertFalse(canCancel);
    }

    // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
    // if reason is DEFAULT_OPERATION completion period must have expired
    @ParameterizedTest
    @CsvSource({
            "OPEN",
            "WAITING_RESOLUTION",
            "CONFIRMED",
    })
    void givenValidClaimAsOwnership_whenCalling_canCancel_claimerCanCancelWhenCompletionPeriodExpired(String statusClaim) {
        var today = Instant.now();
        var eightDaysAgo = today.minus(8, ChronoUnit.DAYS);
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(Instant::now).thenReturn(eightDaysAgo);
            var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
            var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
            var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
            claim.updateStatus(StatusClaim.valueOf(statusClaim));
            mockedStatic.when(Instant::now).thenReturn(today);
            var service = new ClaimService();
            var result = service.canCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getClaimerParticipant());
            Assertions.assertTrue(result);
        }
    }

    // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
    // if reason is DEFAULT_OPERATION completion period must have expired
    @ParameterizedTest
    @CsvSource({
            "CANCELLED",
            "COMPLETED",
    })
    void givenValidClaimAsOwnership_whenCalling_canCancel_CLAIMERCANNOTCANCEL_WhenStatusClaimDifferentOfOPEN_WAITINGRESOLUTION_CONFIRMED(String statusClaim) {
        var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
        var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        claim.updateStatus(StatusClaim.valueOf(statusClaim));
        var service = new ClaimService();
        var result = service.canCancel(claim, ClaimOperationReason.DEFAULT_OPERATION, claim.getClaimerParticipant());
        Assertions.assertFalse(result);
    }

    // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
    // if reason is DEFAULT_OPERATION completion period must have expired
    @ParameterizedTest
    @CsvSource({
            "USER_REQUESTED, CANCELLED",
            "USER_REQUESTED, COMPLETED",
            "ACCOUNT_CLOSURE, CANCELLED",
            "ACCOUNT_CLOSURE, COMPLETED",
            "FRAUD, CANCELLED",
            "FRAUD, COMPLETED",
            "RECONCILIATION, CANCELLED",
            "RECONCILIATION, COMPLETED",
            "RFB_VALIDATION, CANCELLED",
            "RFB_VALIDATION, COMPLETED",
            "PARTICIPANT_EXCLUSION, CANCELLED",
            "PARTICIPANT_EXCLUSION, COMPLETED",
    })
    void givenValidClaimAsOwnership_whenCalling_canCancel_claimerCANOTCANCELIFCOMPLETIONPERIODNOTEXPIRED(String reason, String claimStatus) {
        var today = Instant.now();
        var eightDaysAgo = today.minus(8, ChronoUnit.DAYS);
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(Instant::now).thenReturn(eightDaysAgo);
            var claimer = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomEVP(), TypeKey.EVP);
            var donor = OwnerMock.mockOwnerPassingKey(RandomKeysMock.randomPhone(), TypeKey.PHONE);
            var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
            claim.updateStatus(StatusClaim.valueOf(claimStatus));
            mockedStatic.when(Instant::now).thenReturn(today);
            var service = new ClaimService();
            var result = service.canCancel(claim, ClaimOperationReason.valueOf(reason), claim.getClaimerParticipant());
            Assertions.assertFalse(result);
        }
    }

}
