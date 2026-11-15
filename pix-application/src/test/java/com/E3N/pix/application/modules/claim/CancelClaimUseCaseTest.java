package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.CancelClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

public class CancelClaimUseCaseTest extends UnitTest {

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private CancelClaimUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository, ownerRepository);
    }

    @Test
    void givenValidNoExistentClaim_whenCalling_execute_shouldReturnClaimNotFound(){
        when(claimRepository.findById(any())).thenReturn(Optional.empty());
        var result = (Notification) useCase.execute(UUID.randomUUID().toString(), ClaimOperationReason.USER_REQUESTED, RandomParticipant.getParticipant())
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Claim not found.", result.getDetail());
        Mockito.verifyNoInteractions(ownerRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "PORTABILITY",
            "OWNERSHIP"
    })
    void givenValidClaim_whenCalling_execute_shouldReturnClaimOutputAsClaimerRequest(String typeClaim){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwnerWithPhoneKey(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.valueOf(typeClaim));
        when(claimRepository.findById(any())).thenReturn(Optional.of(claim));
        when(ownerRepository.findByKey(any())).thenReturn(Optional.of(claimer));
        var expectedClaimId = claim.getId().toString();
        var expectedParticipant = claim.getClaimerParticipant();
        var result = (ClaimOutputDto) useCase.execute(expectedClaimId, ClaimOperationReason.USER_REQUESTED, expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(ClaimOutputDto.class, result);
        Assertions.assertEquals(claim.getId().toString(), result.id());
        Assertions.assertEquals(claimer.getAccounts().getFirst().getParticipant().getParticipant(), result.claimerAccountDto().participant());
        Assertions.assertEquals(donor.getAccounts().getFirst().getParticipant().getParticipant(), result.donorParticipant());
    }

    @ParameterizedTest
    @CsvSource({
            "PORTABILITY, USER_REQUESTED",
            "OWNERSHIP, FRAUD"
    })
    void givenValidClaim_whenCalling_execute_shouldReturnClaimOutputAsDonorRequest(String typeClaim, String reason){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwnerWithPhoneKey(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.valueOf(typeClaim));
        when(claimRepository.findById(any())).thenReturn(Optional.of(claim));
        when(ownerRepository.findByKey(any())).thenReturn(Optional.of(claimer));
        var expectedClaimId = claim.getId().toString();
        var expectedParticipant = claim.getDonorParticipant();
        var result = (ClaimOutputDto) useCase.execute(expectedClaimId, ClaimOperationReason.valueOf(reason), expectedParticipant)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(ClaimOutputDto.class, result);
        Assertions.assertEquals(claim.getId().toString(), result.id());
        Assertions.assertEquals(claimer.getAccounts().getFirst().getParticipant().getParticipant(), result.claimerAccountDto().participant());
        Assertions.assertEquals(donor.getAccounts().getFirst().getParticipant().getParticipant(), result.donorParticipant());
    }

}
