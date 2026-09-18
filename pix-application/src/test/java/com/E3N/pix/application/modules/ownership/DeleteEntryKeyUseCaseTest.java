package com.E3N.pix.application.modules.ownership;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.modules.ownership.mocks.OwnerMock;
import com.E3N.pix.application.ownership.DeleteEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomKeysMock;
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

public class DeleteEntryKeyUseCaseTest extends UnitTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private DeleteEntryKeyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @Test
    void giveUseCase_shouldBeInstantiated() {
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(ownerRepository);
    }

    @Test
    void givenInvalidValues_whenCallingDeleteEntryKey_shouldEitherWithNotification() {
        var expectedKey = RandomKeysMock.randomEmails();
        var expectedParticipant = RandomParticipant.getParticipant();
        Mockito.when(ownerRepository.findByKeyAndParticipant(expectedKey, expectedParticipant))
                .thenReturn(Optional.empty());

        var expectedDetail = "EntryKey not found";
        var result = useCase.deleteEntryKey(expectedKey, expectedParticipant, Reason.USER_REQUESTED);
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
    void givenValidValues_whenCallingDeleteEntryKey_shouldReturnEitherWithOwner() {
        var owner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedParticipant = owner.getAccounts().getFirst().getParticipant().getParticipant();
        var result = useCase.deleteEntryKey(expectedKey, expectedParticipant, Reason.USER_REQUESTED);
        result.fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertNotNull(owner1.getAccounts().getFirst().getEntryKeys().getFirst().getDeletedAt());
                    Assertions.assertNotNull(owner1.getAccounts().getFirst().getDeletedAt());
                    return owner1;
                }
        );
    }

    @Test
    void givenOwnerTwoKey_whenCallingDeleteEntryKey_shouldDeleteFirstOne() {
        var owner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedParticipant = owner.getAccounts().getFirst().getParticipant().getParticipant();
        EntryKey key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        owner.getAccounts().getFirst().addKey(key);
        var result = useCase.deleteEntryKey(expectedKey, expectedParticipant, Reason.USER_REQUESTED);
        result.fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertNotNull(owner1.getAccounts().getFirst().getEntryKeys().getFirst().getDeletedAt());
                    Assertions.assertNull(owner1.getAccounts().getFirst().getEntryKeys().getLast().getDeletedAt());
                    Assertions.assertNull(owner1.getAccounts().getFirst().getDeletedAt());
                    return owner1;
                }
        );
    }

    @Test
    void givenOwnerTwoKey_whenCallingDeleteEntryKey_shouldDeleteSecondOne() {
        var owner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var expectedParticipant = owner.getAccounts().getFirst().getParticipant().getParticipant();
        EntryKey key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var expectedKey = key.getKey().getKey();
        owner.getAccounts().getFirst().addKey(key);
        var result = useCase.deleteEntryKey(expectedKey, expectedParticipant, Reason.USER_REQUESTED);
        result.fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertNotNull(owner.getAccounts().getFirst().getEntryKeys().getLast().getDeletedAt());
                    Assertions.assertNull(owner.getAccounts().getFirst().getEntryKeys().getFirst().getDeletedAt());
                    Assertions.assertNull(owner.getAccounts().getFirst().getDeletedAt());
                    return owner1;
                }
        );
    }

    @Test
    void givenOwnerTwoAccounts_whenCallingDeleteEntryKey_shouldDeleteJustOne() {
        var owner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedParticipant = owner.getAccounts().getFirst().getParticipant().getParticipant();
        EntryKey key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var acc = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC, "2026-09-17 31:18:33", key);
        owner.addNewAccountOrNewKey(acc);
        var result = useCase.deleteEntryKey(expectedKey, expectedParticipant, Reason.USER_REQUESTED);
        result.fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertNotNull(owner1.getAccounts().getFirst().getEntryKeys().getLast().getDeletedAt());
                    Assertions.assertNull(owner1.getAccounts().getLast().getEntryKeys().getFirst().getDeletedAt());
                    Assertions.assertNull(owner1.getAccounts().getFirst().getDeletedAt());
                    Assertions.assertNotNull(owner1.getAccounts().getLast().getDeletedAt());
                    return owner1;
                }
        );
    }
}
