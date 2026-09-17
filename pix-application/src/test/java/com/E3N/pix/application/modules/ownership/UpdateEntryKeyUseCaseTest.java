package com.E3N.pix.application.modules.ownership;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.modules.ownership.mocks.OwnerMock;
import com.E3N.pix.application.modules.ownership.mocks.UpdateEntryKeyDtoMock;
import com.E3N.pix.application.ownership.UpdateEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

public class UpdateEntryKeyUseCaseTest extends UnitTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private UpdateEntryKeyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @Test
    void givenUseCase_shouldInstantiate() {
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(ownerRepository);
    }

    @Test
    void givenValidDto_whenCallingWithNOExistentKey_shouldReturnEitherWithNotification() {
        var dto = UpdateEntryKeyDtoMock.getUpdateEntryKeyValid(RandomParticipant.getParticipant(), RandomKeysMock.randomEmails(), RandomCpfMock.getRandomCFP(), Reason.USER_REQUESTED);
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        var expectedDetail = "EntryKey does not exist.";
        var result = useCase.execute(dto, dto.key());
        result.fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertEquals(expectedDetail, notification.getDetail());
                    return notification;
                },
                owner -> {
                    Assertions.assertNull(owner);
                    return null;
                }
        );
    }

    @Test
    void givenValidDtoWithUnrelatedKey_whenCallingUpdateKey_shouldReturnEitherWithNotification() {
        var dto = UpdateEntryKeyDtoMock.getUpdateEntryKeyValid(RandomParticipant.getParticipant(), RandomKeysMock.randomEmails(), RandomCpfMock.getRandomCFP(), Reason.USER_REQUESTED);
        var unrelatedKey = "doesNotMatchDto";
        var expectedDetail = "Key in URL does not match the key in body.";
        var result = useCase.execute(dto, unrelatedKey);
        result.fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertEquals(expectedDetail, notification.getDetail());
                    return notification;
                },
                owner -> {
                    Assertions.assertNull(owner);
                    return null;
                }
        );
    }

    @Test
    void givenInvalidDto_whenCallingWithNOExistentKey_shouldReturnEitherWithNotification() {
        var dto = UpdateEntryKeyDtoMock.getUpdateEntryKeyValid(RandomParticipant.getParticipant(), RandomKeysMock.randomEmails(), RandomCpfMock.getRandomCFP(), Reason.USER_REQUESTED);
        var owner = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(owner));
        var result = useCase.execute(dto, dto.key());
        var expectedReasons = Arrays.asList("There is no key that matches the key passed", "Participants different, Open a claim or portability.");
        result.fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertTrue(notification.hasError());
                    Assertions.assertTrue(expectedReasons.containsAll(notification.getViolations().stream().map(Violation::reason).toList()));
                    return notification;
                },
                owner1 -> {
                    Assertions.assertNull(owner1);
                    return null;
                }
        );
    }

    @Test
    void givenValidDto_whenCallingUpdate_shouldReturnEitherWithOwner() {
        var owner = OwnerMock.getOwner(TypePerson.NATURAL_PERSON);
        var account = owner.getAccounts().getFirst();
        var key = account.getEntryKeys().getFirst();
        var dto = UpdateEntryKeyDtoMock.getUpdateEntryKeyValid(account.getParticipant().getParticipant(),
                key.getKey().getKey(), owner.getTaxIdNumber().getTaxIdNumber(), Reason.BRANCH_TRANSFER);
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var result = useCase.execute(dto, dto.key());
        result.fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertEquals(dto.accountDto().number(), owner1.getAccounts().getLast().getNumber().getNumber());
                    Assertions.assertNotNull(owner.getAccounts().getFirst().getDeletedAt());
                    return owner1;
                }
        );
    }
}
