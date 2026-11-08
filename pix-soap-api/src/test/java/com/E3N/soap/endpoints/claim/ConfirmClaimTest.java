package com.E3N.soap.endpoints.claim;

import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.ClaimOperationReasonType;
import com.E3N.pix.soap.contract.ConfirmClaimResponse;
import com.E3N.pix.soap.endpoints.Claim;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mocks.claim.ClaimSoapMock;
import com.E3N.soap.mocks.claim.ConfirmClaimMock;
import com.E3N.soap.mocks.entryKey.OwnerMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class ConfirmClaimTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    @Mock
    private ClaimRepositoryImpl claimRepository;

    private Claim claim;

    @BeforeEach
    void setUp(){
        claim = new Claim(claimRepository, ownerRepository);
    }

    @Test
    void shouldBeInstantiated() {
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(claimRepository);
        Assertions.assertNotNull(claim);
    }

    @Test
    void givenInvalidClaimId_whenCalling_confirmClaim_shouldReturnClaimNotFound(){
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var request = ConfirmClaimMock.getRequest(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), ClaimOperationReasonType.DEFAULT_OPERATION);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.confirmClaim(request));
        Assertions.assertNotNull(request);
        Assertions.assertEquals("Claim not found.", result.getNotification().getDetail());
    }

    @Test
    void givenInvalidClaimId_whenCalling_confirmClaim_shouldReturnStatusClaimDoesNotPermitOperation(){
        var claimer = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var claimModel = ClaimSoapMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claimModel));
        var request = ConfirmClaimMock.getRequest(claimModel.getId().toString(), claimModel.getClaimerParticipant(), ClaimOperationReasonType.DEFAULT_OPERATION);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.confirmClaim(request));
        Assertions.assertNotNull(request);
        Assertions.assertEquals("The status of claim does not permit this operation.", result.getNotification().getDetail());
    }

    @Test
    void givenInvalidClaimId_whenCalling_confirmClaim_shouldReturnClaimResponse(){
        var claimer = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var claimModel = ClaimSoapMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claimModel.updateStatus(StatusClaim.WAITING_RESOLUTION);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claimModel));
        when(ownerRepository.findByKey(any()))
                .thenReturn(Optional.of(donor));
        when(ownerRepository.findByTaxIdNumber(any()))
                .thenReturn(Optional.of(claimer));
        var request = ConfirmClaimMock.getRequest(claimModel.getId().toString(), claimModel.getClaimerParticipant(), ClaimOperationReasonType.USER_REQUESTED);
        var result = claim.confirmClaim(request);
        Assertions.assertInstanceOf(ConfirmClaimResponse.class, result);
        Assertions.assertEquals(claimModel.getId().toString(), result.getClaim().getId());
    }
}
