package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.ListClaimUseCase;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.claim.mocks.ListClaimDtoMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ListClaimUseCaseTest extends UnitTest {

    Owner claimer = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
    Owner donor = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
    Claim claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private ListClaimUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository);
        Mockito.reset(ownerRepository);
    }

    @Test
    void givenConfiguration_shouldBeInstantiatedTest() {
        Assertions.assertNotNull(claimRepository);
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(useCase);
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnClaimerNotFound() {
        var dto = ListClaimDtoMock.getDto();
        when(claimRepository.findClaims(anyString(), eq(false), eq(false), any(), any(), any(), any(), eq(20)))
                .thenReturn(List.of());
        var result = useCase.execute(dto);
        result.fold(
                (notification -> {
                    Assertions.assertInstanceOf(Notification.class, notification);
                    Assertions.assertEquals("There is no claims.", notification.getDetail());
                    return notification;
                }),
                claimOutputDTOs -> {
                    Assertions.assertTrue(claimOutputDTOs.isEmpty());
                    return null;
                }
        );
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnListOfClaims() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var claim = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        var account = claimer.getAccounts().getFirst();
        var key = account.getEntryKeys().getFirst();
        var listCLaims = ListClaimDtoMock.getDto();
        when(claimRepository.findClaims(anyString(), eq(false), eq(false), any(), any(), any(), any(), eq(20)))
                .thenReturn(List.of(claim));
        when(ownerRepository.findAccount(anyString(), anyString()))
                .thenReturn(Optional.of(account));
        when(ownerRepository.findEntryKey(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(key));
        when(ownerRepository.findByTaxIdNumber(anyString()))
                .thenReturn(Optional.of(claimer));
        var result = useCase.execute(listCLaims);
        Notification notification = null;
        List<ClaimOutputDto> list = new ArrayList<>();
        if (result instanceof Either.Left<Notification, List<ClaimOutputDto>>(Notification value)) {
            notification = value;
        }
        if (result instanceof Either.Right<Notification, List<ClaimOutputDto>>(List<ClaimOutputDto> value)) {
            list = value;
        }
        Assertions.assertNull(notification);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(claimer.getTaxIdNumber().getTaxIdNumber(), list.getFirst().claimerDto().taxIdNumber());
        Assertions.assertEquals(claim.getDonorParticipant(), list.getFirst().donorParticipant());
    }
}
