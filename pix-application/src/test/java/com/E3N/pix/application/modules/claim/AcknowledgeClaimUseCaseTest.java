package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.AcknowledgeClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AcknowledgeClaimUseCaseTest extends UnitTest {

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private AcknowledgeClaimUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository, ownerRepository);
    }

    @Test
    void givenNoneExistentId_whenCalling_execute_shouldReturnNotificationClaimNotFound(){
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var expectedId = UUID.randomUUID().toString();
        var expectedParticipant = RandomParticipant.getParticipant();
        var result = (Notification) useCase.execute(expectedId, expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Claim not found.", result.getDetail());
        verify(ownerRepository, never()).findByTaxIdNumber(anyString());
    }

    @Test
    void givenNoneExistentId_whenCalling_execute_shouldReturnNotificationClaimsNotBelong(){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claim.getId().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        var expectedParticipant = RandomParticipant.getParticipant();
        var result = (Notification) useCase.execute(expectedId, expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Claim belongs to another participant.", result.getDetail());
        verify(ownerRepository, never()).findByTaxIdNumber(anyString());
    }

    @Test
    void givenNoneExistentId_whenCalling_execute_shouldReturnNotificationClaimerNotFound(){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claim.getId().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        when(ownerRepository.findByTaxIdNumber(any()))
                .thenReturn(Optional.empty());
        var expectedParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var result = (Notification) useCase.execute(expectedId, expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Claimer not found.", result.getDetail());
    }

    @Test
    void givenNoneExistentId_whenCalling_execute_shouldReturnClaimOutput(){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claim.getId().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        when(ownerRepository.findByTaxIdNumber(any()))
                .thenReturn(Optional.of(claimer));
        var expectedParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var result = (ClaimOutputDto) useCase.execute(expectedId, expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(ClaimOutputDto.class, result);
        Assertions.assertEquals(claim.getId().toString(), result.id());
    }

}
