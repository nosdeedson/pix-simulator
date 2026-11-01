package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.GetClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GetClaimUseCaseTest extends UnitTest {

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private GetClaimUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository, ownerRepository);
    }

    @Test
    void givenNotExistentId_whenCalling_execute_shouldReturnNotificationClaimNotFound(){
        var expectedId = UUID.randomUUID().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.empty());
        var result = useCase.execute(expectedId);
        var res = (Notification) result.fold(
                notification1 -> {
                    return notification1;
                },
                dto -> {
                    return dto;
                }
        );

       Assertions.assertInstanceOf(Notification.class, res);
       Assertions.assertEquals("Claim not found.", res.getDetail());
       verify(ownerRepository, never()).findByTaxIdNumber(anyString());

    }

    @Test
    void givenNotExistentId_whenCalling_execute_shouldReturnNotificationClaimerNotFound(){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claim.getId().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        when(ownerRepository.findByTaxIdNumber(anyString()))
                .thenReturn(Optional.empty());
        var result = (Notification) useCase.execute(expectedId)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Owner not found.", result.getDetail());
    }

    @Test
    void givenNotExistentId_whenCalling_execute_shouldReturnNotificationClaimerOut(){
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var expectedId = claim.getId().toString();
        when(claimRepository.findById(any()))
                .thenReturn(Optional.of(claim));
        when(ownerRepository.findByTaxIdNumber(anyString()))
                .thenReturn(Optional.of(claimer));
        var result = (ClaimOutputDto) useCase.execute(expectedId)
                .fold(notification -> notification, dto -> dto);
        Assertions.assertInstanceOf(ClaimOutputDto.class, result);
        Assertions.assertEquals(claim.getId().toString(), result.id());
    }


}
