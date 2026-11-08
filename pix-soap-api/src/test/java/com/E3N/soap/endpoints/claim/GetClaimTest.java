package com.E3N.soap.endpoints.claim;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.GetClaimRequest;
import com.E3N.pix.soap.contract.GetClaimResponse;
import com.E3N.pix.soap.endpoints.Claim;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.mocks.claim.ClaimsEndpointMock;
import com.E3N.soap.mocks.entryKey.OwnerMock;
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
public class GetClaimTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    @Mock
    private ClaimRepositoryImpl claimRepository;

    private Claim claim;

    @BeforeEach
    void setUp() {
        claim = new Claim(claimRepository, ownerRepository);
    }

    @Test
    void shouldBeInstantiated() {
        Assertions.assertNotNull(claim);
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(claimRepository);
    }

    @Test
    void givenNoneExistentId_whenCalling_getClaim_shouldReturnNotificationWithClaimNotFound() {
        var expectedId = UUID.randomUUID().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var request = new GetClaimRequest();
        request.setClaimId(expectedId);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.getClaim(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claim not found.", result.getNotification().getDetail());
    }

    @Test
    void givenNoneExistentId_whenCalling_getClaim_shouldReturnNotificationWithOwnerNotFound() {
        var expectedId = UUID.randomUUID().toString();
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claims));
        when(ownerRepository.findByTaxIdNumber(anyString()))
                .thenReturn(Optional.empty());
        var request = new GetClaimRequest();
        request.setClaimId(expectedId);
        var result = Assertions.assertThrows(SoapFaultException.class, () -> claim.getClaim(request));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Owner not found.", result.getNotification().getDetail());
    }

    @Test
    void givenAExistentId_whenCalling_getClaim_shouldReturnAClaim() {
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claims.getId().toString();
        var request = new GetClaimRequest();
        request.setClaimId(expectedId);
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claims));
        when(ownerRepository.findByTaxIdNumber(anyString()))
                .thenReturn(Optional.of(claimer));
        var result = claim.getClaim(request);
        Assertions.assertInstanceOf(GetClaimResponse.class, result);
        Assertions.assertEquals(expectedId, result.getClaim().getId());
    }


}
