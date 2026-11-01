package com.E3N.soap.endpoints.claim;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.AcknowledgeClaimRequest;
import com.E3N.pix.soap.contract.AcknowledgeClaimResponse;
import com.E3N.pix.soap.endpoints.Claim;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.claim.ClaimsEndpointMock;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetClaimAcknowledgeTest {

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
    void givenNoneExistentId_whenCalling_getAcknowledge_shouldReturnNotificationWithClaimNotFound(){
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var request = new AcknowledgeClaimRequest();
        request.setClaimId(UUID.randomUUID().toString());
        request.setParticipant(RandomParticipant.getParticipant());
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.acknowledge(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claim not found.", result.getNotification().getDetail() );
        verify(ownerRepository, never()).findByTaxIdNumber(any());
    }

    @Test
    void givenNoneExistentId_whenCalling_getAcknowledge_shouldReturnNotificationWithParticipantDivergent(){
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claims));
        var request = new AcknowledgeClaimRequest();
        request.setClaimId(UUID.randomUUID().toString());
        request.setParticipant(RandomParticipant.getParticipant());
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.acknowledge(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claim belongs to another participant.", result.getNotification().getDetail() );
        verify(ownerRepository, never()).findByTaxIdNumber(any());
    }

    @Test
    void givenNoneExistentId_whenCalling_getAcknowledge_shouldReturnNotificationWithClaimerNotFound(){
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claims));
        when(ownerRepository.findByTaxIdNumber(any()))
                .thenReturn(Optional.empty());
        var request = new AcknowledgeClaimRequest();
        request.setClaimId(claims.getId().toString());
        request.setParticipant(claims.getClaimerParticipant());
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.acknowledge(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claimer not found.", result.getNotification().getDetail() );
    }

    @Test
    void givenNoneExistentId_whenCalling_getAcknowledge_shouldReturnAcknowledge(){
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claims));
        when(ownerRepository.findByTaxIdNumber(any()))
                .thenReturn(Optional.of(claimer));
        var request = new AcknowledgeClaimRequest();
        request.setClaimId(claims.getId().toString());
        request.setParticipant(claims.getClaimerParticipant());
        var result = claim.acknowledge(request);
        Assertions.assertInstanceOf(AcknowledgeClaimResponse.class, result);
        Assertions.assertEquals(claims.getId().toString(), result.getClaim().getId());
    }
}
