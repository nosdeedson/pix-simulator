package com.E3N.pix.application.modules.claim;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.claim.CreateClaimUseCase;
import com.E3N.pix.application.claim.dto.ClaimOutputDto;
import com.E3N.pix.application.modules.claim.mocks.ClaimDtoMock;
import com.E3N.pix.application.modules.claim.mocks.ClaimMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerMock;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;
import com.E3N.test.Owner.RandomCpfMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class CreateClaimUseCaseTest extends UnitTest {

    @Mock
    private ClaimRepositoryInterface claimRepository;

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private CreateClaimUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(claimRepository);
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnClaimerNotFound() {
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.empty());
        var dto = ClaimDtoMock.getClaimDto(TypePerson.NATURAL_PERSON, TypeClaim.OWNERSHIP, TypeKey.EMAIL, null, null, RandomCpfMock.getRandomCFP());
        var result = useCase.execute(dto);
        Notification notification = null;
        ClaimOutputDto claim = null;
        if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
            claim = value;
        }
        Assertions.assertNull(claim);
        Assertions.assertNotNull(notification);
        Assertions.assertEquals("Claimer not found.", notification.getDetail());
        Mockito.verify(ownerRepository, Mockito.times(0)).findByKey(anyString());
        Mockito.verifyNoInteractions(claimRepository);
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnDonorNotFound() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(claimer));

        Mockito.when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.empty());
        var dto = ClaimDtoMock.getClaimDto(TypePerson.NATURAL_PERSON, TypeClaim.OWNERSHIP, TypeKey.EMAIL,
                claimer.getAccounts().getFirst().getNumber().getNumber(),
                claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(), RandomCpfMock.getRandomCFP()
        );
        var result = useCase.execute(dto);
        Notification notification = null;
        ClaimOutputDto claim = null;
        if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
            claim = value;
        }
        Assertions.assertNull(claim);
        Assertions.assertNotNull(notification);
        Assertions.assertEquals("Donor not found", notification.getDetail());
        Mockito.verifyNoInteractions(claimRepository);
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnEntryKeyFromClaimService() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(claimer));
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var expectedEntryKey = donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedTypeKey = donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType();
        var expectClaimerParticipant = donor.getAccounts().getFirst().getParticipant().getParticipant();
        Mockito.when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.of(donor));
        try (MockedStatic<ClaimService> service = Mockito.mockStatic(ClaimService.class)) {
            service.when(
                    () -> ClaimService.getEntryToDonate(donor, expectedEntryKey)
            ).thenReturn(Optional.of(donor.getAccounts().getFirst().getEntryKeys().getFirst()));
            service.when(
                    () -> ClaimService.validateTypeClaim(claimer, donor, TypeClaim.PORTABILITY, expectClaimerParticipant,
                            expectedTypeKey)
            ).thenReturn(null);
            var dto = ClaimDtoMock.getClaimDto(TypePerson.NATURAL_PERSON, TypeClaim.OWNERSHIP, TypeKey.EMAIL,
                    claimer.getAccounts().getFirst().getNumber().getNumber(),
                    claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(), RandomCpfMock.getRandomCFP()
            );
            var result = useCase.execute(dto);
            Notification notification = null;
            ClaimOutputDto claim = null;
            if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
                notification = value;
            } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
                claim = value;
            }
            Assertions.assertNull(claim);
            Assertions.assertNotNull(notification);
            Assertions.assertEquals("Key not found.", notification.getDetail());
            Mockito.verifyNoInteractions(claimRepository);
        }
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldReturnNotificationFromClaimService() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(claimer));
        var expectedParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var expectedEntryKey = donor.getAccounts().getFirst().getEntryKeys().getFirst();
        Mockito.when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.of(donor));
        try (MockedStatic<ClaimService> service = Mockito.mockStatic(ClaimService.class)) {
            service.when(
                    () -> ClaimService.getEntryToDonate(donor, expectedEntryKey.getKey().getKey())
            ).thenReturn(Optional.of(expectedEntryKey));
            service.when(
                    () -> ClaimService.validateTypeClaim(claimer, donor, TypeClaim.OWNERSHIP, expectedParticipant, expectedEntryKey.getKey().getType())
            ).thenReturn(Notification.create("Conflict", 400, "Type of claim must be Ownership"));
            var dto = ClaimDtoMock.getClaimDto(TypePerson.NATURAL_PERSON, TypeClaim.OWNERSHIP, TypeKey.EMAIL,
                    claimer.getAccounts().getFirst().getNumber().getNumber(),
                    claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(), RandomCpfMock.getRandomCFP()
            );
            var result = useCase.execute(dto);
            Notification notification = null;
            ClaimOutputDto claim = null;
            if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
                notification = value;
            } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
                claim = value;
            }
            Assertions.assertNull(claim);
            Assertions.assertNotNull(notification);
            Assertions.assertEquals("Key not found.", notification.getDetail());
            Mockito.verifyNoInteractions(claimRepository);
        }
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldSaveClaimAsPortability() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(claimer));
        var expectedParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var expectedKey = donor.getAccounts().getFirst().getEntryKeys().getFirst();
        Mockito.when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.of(donor));
        var claimToSave = ClaimMock.mockClaim(claimer, donor, TypeClaim.PORTABILITY);
        Mockito.when(claimRepository.save(Mockito.any()))
                .thenReturn(claimToSave);
        try (MockedStatic<ClaimService> service = Mockito.mockStatic(ClaimService.class)) {
            service.when(
                    () -> ClaimService.getEntryToDonate(donor, expectedKey.getKey().getKey())
            ).thenReturn(Optional.of(expectedKey));
            service.when(
                    () -> ClaimService.validateTypeClaim(claimer, donor, TypeClaim.PORTABILITY, expectedParticipant, expectedKey.getKey().getType())
            ).thenReturn(null);
            service.when(
                    () -> ClaimService.create(
                            claimer.getAccounts().getFirst().getId().toString(),
                            claimer.getAccounts().getFirst().getParticipant().getParticipant(),
                            expectedKey.getKey().getKey(),
                            claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType(),
                            donor.getAccounts().getFirst().getParticipant().getParticipant(),
                            TypeClaim.PORTABILITY,
                            claimer.getTaxIdNumber().getTaxIdNumber(),
                            claimer.getType()
                    )
            ).thenReturn(claimToSave);
            var dto = ClaimDtoMock.getClaimDto(
                    TypePerson.NATURAL_PERSON, TypeClaim.PORTABILITY, TypeKey.EMAIL,
                    claimer.getAccounts().getFirst().getNumber().getNumber(), expectedKey.getKey().getKey(), claimer.getTaxIdNumber().getTaxIdNumber()
            );
            var result = useCase.execute(dto);
            Notification notification = null;
            ClaimOutputDto claim = null;
            if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
                notification = value;
            } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
                claim = value;
            }
            Assertions.assertNull(notification);
            Assertions.assertNotNull(claim);
            Assertions.assertNotNull(claim.id());
            Assertions.assertEquals(dto.claimerDto().taxIdNumber(), claim.claimerDto().taxIdNumber());
        }
    }

    @Test
    void givenValidDto_whenCallingExecute_shouldSaveClaimAsOwnership() {
        var claimer = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(claimer));
        var expectedParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var donor = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        var expectedKey = donor.getAccounts().getFirst().getEntryKeys().getFirst();
        Mockito.when(ownerRepository.findByKey(anyString()))
                .thenReturn(Optional.of(donor));
        var claimToSave = ClaimMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        Mockito.when(claimRepository.save(any()))
                .thenReturn(claimToSave);
        try (MockedStatic<ClaimService> service = Mockito.mockStatic(ClaimService.class)) {
            service.when(
                    () -> ClaimService.getEntryToDonate(donor, expectedKey.getKey().getKey())
            ).thenReturn(Optional.of(expectedKey));
            service.when(
                    () -> ClaimService.validateTypeClaim(claimer, donor, TypeClaim.OWNERSHIP, expectedParticipant, expectedKey.getKey().getType())
            ).thenReturn(null);
            service.when(
                    () -> ClaimService.create(
                            claimer.getAccounts().getFirst().getId().toString(),
                            claimer.getAccounts().getFirst().getParticipant().getParticipant(),
                            expectedKey.getKey().getKey(),
                            claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType(),
                            donor.getAccounts().getFirst().getParticipant().getParticipant(),
                            TypeClaim.OWNERSHIP,
                            claimer.getTaxIdNumber().getTaxIdNumber(),
                            claimer.getType()
                    )
            ).thenReturn(claimToSave);
            var dto = ClaimDtoMock.getClaimDto(
                    TypePerson.NATURAL_PERSON, TypeClaim.OWNERSHIP, TypeKey.EMAIL,
                    claimer.getAccounts().getFirst().getNumber().getNumber(), expectedKey.getKey().getKey(), claimer.getTaxIdNumber().getTaxIdNumber()
            );
            var result = useCase.execute(dto);
            Notification notification = null;
            ClaimOutputDto claim = null;
            if (result instanceof Either.Left<Notification, ClaimOutputDto>(Notification value)) {
                notification = value;
            } else if (result instanceof Either.Right<Notification, ClaimOutputDto>(ClaimOutputDto value)) {
                claim = value;
            }
            Assertions.assertNotNull(notification);
            Assertions.assertEquals("The type of claim must be Portability", notification.getViolations().getFirst().reason());
        }
    }
}
