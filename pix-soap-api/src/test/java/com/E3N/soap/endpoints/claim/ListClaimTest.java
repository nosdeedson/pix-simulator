package com.E3N.soap.endpoints.claim;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.modules.claim.ClaimRepositoryImpl;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.ListClaimsResponse;
import com.E3N.pix.soap.endpoints.Claim;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mocks.claim.ClaimsEndpointMock;
import com.E3N.soap.mocks.claim.ListClaimRequestMock;
import com.E3N.soap.mocks.entryKey.OwnerMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ListClaimTest extends UnitTest {

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
    void givenJustParticipant_whenCallingEndpoint_shouldReturnNoneClaims() {
        var participant = RandomParticipant.getParticipant();
        var request = ListClaimRequestMock.getListClaimRequestWithAllNull(participant);
        when(claimRepository.findClaims(eq(participant), nullable(Boolean.class), nullable(Boolean.class), any(), any(), any(), any(), eq(20)))
                .thenReturn(List.of());
        var result = claim.listClaim(request);
        Assertions.assertInstanceOf(ListClaimsResponse.class, result);
        Assertions.assertTrue(result.getClaims().getClaim().isEmpty());
    }

    @Test
    void givenJustParticipant_whenCallingEndpoint_shouldReturnClaims() {
        var participant = RandomParticipant.getParticipant();
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var request = ListClaimRequestMock.getListClaimRequestWithAllNull(participant);
        when(claimRepository.findClaims(eq(participant), nullable(Boolean.class), nullable(Boolean.class), any(), any(), any(), any(), eq(20)))
                .thenReturn(List.of(claims));
        when(ownerRepository.findByAccountAndKey(anyString(), anyString()))
                .thenReturn(Optional.of(claimer));
        var result = claim.listClaim(request);
        Assertions.assertInstanceOf(ListClaimsResponse.class, result);
        Assertions.assertFalse(result.getClaims().getClaim().isEmpty());
    }

    @Test
    void givenDtoWihAllAttributesSet_whenCallingEndpoint_shouldReturnClaims() {
        var participant = RandomParticipant.getParticipant();
        var claimer = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var claims = ClaimsEndpointMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var request = ListClaimRequestMock.getListClaimRequest(Boolean.TRUE, participant);
        when(claimRepository.findClaims(eq(participant), nullable(Boolean.class), nullable(Boolean.class), any(), any(), any(), any(), eq(20)))
                .thenReturn(List.of(claims));
        when(ownerRepository.findByAccountAndKey(anyString(), anyString()))
                .thenReturn(Optional.of(claimer));
        var result = claim.listClaim(request);
        Assertions.assertInstanceOf(ListClaimsResponse.class, result);
        Assertions.assertFalse(result.getClaims().getClaim().isEmpty());
    }


}
