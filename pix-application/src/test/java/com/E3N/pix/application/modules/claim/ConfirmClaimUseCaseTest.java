package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.ConfirmClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;

public class ConfirmClaimUseCaseTest extends UnitTest {

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private ConfirmClaimUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository, ownerRepository);
    }

    @Test
    void givenNoExistentId_whenCalling_execute_shouldReturnEitherWithNotificationOfNoClaim(){
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var result = (Notification) useCase.execute(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), ClaimOperationReason.DEFAULT_OPERATION)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claim not found.", result.getDetail());
    }

    @Test
    void givenExistentId_whenCalling_execute_shouldReturnEitherWithNotificationOfWrongStatus(){
        var claimer = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        var expectedParticipant = claim.getClaimerParticipant();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        var result = (Notification) useCase.execute(claim.getId().toString(), expectedParticipant, ClaimOperationReason.DEFAULT_OPERATION)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("The status of claim does not permit this operation.", result.getDetail());
    }

    @Test
    void givenValidId_whenCalling_execute_shouldConfirmClaim(){
        var claimer = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        claim.updateStatus(StatusClaim.WAITING_RESOLUTION);
        var expectedParticipant = claim.getClaimerParticipant();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        when(ownerRepository.findByKey(claim.getKeyClaimed()))
                .thenReturn(Optional.of(donor));
        when(ownerRepository.findByTaxIdNumber(claim.getTaxIdNumberClaimer().getTaxIdNumber()))
                .thenReturn(Optional.of(claimer));
        when(ownerRepository.update(donor))
                .thenReturn(donor);
        when(ownerRepository.update(claimer))
                .thenReturn(claimer);
        when(claimRepository.update(claim))
                .thenReturn(claim);
        var result = (ClaimOutputDto) useCase.execute(claim.getId().toString(), expectedParticipant, ClaimOperationReason.USER_REQUESTED)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(claim.getId().toString(), result.id());
        Assertions.assertEquals(claim.getClaimerParticipant(), result.claimerAccountDto().participant());
        Assertions.assertEquals(claim.getKeyClaimed(), result.key());
        Assertions.assertEquals(claim.getDonorParticipant(), result.donorParticipant());
        Assertions.assertEquals(claim.getTaxIdNumberClaimer().getTaxIdNumber(), result.claimerDto().taxIdNumber());
    }
}
