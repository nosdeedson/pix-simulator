package com.E3N.pix.application.modules.ownership;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.modules.ownership.mock.AccountDtoMock;
import com.E3N.pix.application.modules.ownership.mock.EntryKeyDtoMock;
import com.E3N.pix.application.modules.ownership.mock.OwnerDtoMock;
import com.E3N.pix.application.ownership.CreateEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.ownership.dto.OwnerDto;
import com.E3N.test.Owner.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateEntryKeyUseCaseTest extends UnitTest {

    private static final Faker faker = new Faker();

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private CreateEntryKeyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    static List<Arguments> providerKeyExistent() {
        return List.of(
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.LEGAL_PERSON, TypeKey.CNPJ)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.LEGAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.LEGAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.LEGAL_PERSON, TypeKey.PHONE)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.CPF)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.PHONE))
        );
    }

    @ParameterizedTest
    @MethodSource("providerKeyExistent")
    void givenExistentKey_whenCalling_createOrUpdateEntryKey_shouldReturnNotification(OwnerDto expectedOwnerDto){
        var expectedOwner = Owner.getInstance(
                expectedOwnerDto.name(), expectedOwnerDto.tradeName(), expectedOwnerDto.taxIdNumber(), expectedOwnerDto.typePerson(),
                expectedOwnerDto.account().toEntity()
        );
        Mockito.when(ownerRepository.findByKey(expectedOwnerDto.account().entryKeyDto().key()))
                .thenReturn(Optional.of(expectedOwner));
        var result = useCase.createOrUpdateEntryKey(expectedOwnerDto);
        Notification notification = null;
        Owner owner = null;
        if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        }
        var expectedMessages = List.of("Key already exists.","Key is registered in another bank, create a portability or claims ownership");
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertNull(owner);
        Assertions.assertEquals(1, notification.getViolations().size());
        Assertions.assertTrue(expectedMessages.containsAll(notification.getViolations().stream().map(Violation::reason).toList()));
    }

    @Test
    void givenNoExistentKey_whenCalling_createOrUpdateEntryKey_shouldReturnNotification() {
        var dto = OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.EMAIL);
        Mockito.when(ownerRepository.findByKey(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(Mockito.anyString()))
                .thenReturn(Optional.empty());
        var result = useCase.createOrUpdateEntryKey(dto).fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertTrue(notification.hasError());
                    Assertions.assertFalse(notification.getViolations().isEmpty());
                    return notification;
                },
                owner -> {
                    Assertions.assertNull(owner);
                    return null;
                }
        );
        Assertions.assertInstanceOf(Notification.class, result);
    }


    @Test
    void givenNoExistentKey_whenCalling_createOrUpdateEntryKey_shouldReturnOwner() {
        var dto = OwnerDtoMock.getOwnerDto(TypePerson.LEGAL_PERSON, TypeKey.EMAIL);
        Mockito.when(ownerRepository.findByKey(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(Mockito.anyString()))
                .thenReturn(Optional.empty());
        var result = useCase.createOrUpdateEntryKey(dto).fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner -> {
                    Assertions.assertNotNull(owner);
                    Assertions.assertFalse(owner.getNotification().hasError());
                    Assertions.assertEquals(dto.name(), owner.getName().getName());
                    Assertions.assertEquals(dto.taxIdNumber(), owner.getTaxIdNumber().getTaxIdNumber());
                    return owner;
                }
        );
        Assertions.assertInstanceOf(Owner.class, result);
    }

    @Test
    void givenExistentKey_whenCalling_createOrUpdateEntryKey_shouldReturnNotificationNotAllowedMoreKeys() {
        var dto = OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.CPF);
        var owner  = Owner.getInstance(dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity());
        var key2 = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var key3 = EntryKey.getInstance(RandomKeysMock.randomEVP(), TypeKey.EVP, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        owner.getAccounts().getFirst().getEntryKeys().addAll(List.of(key2, key3));
        var key4 = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var key5 = EntryKey.getInstance(RandomKeysMock.randomEVP(), TypeKey.EVP, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var key6 = EntryKey.getInstance(RandomKeysMock.randomLegalPersonDocument(), TypeKey.CNPJ, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key4);
        account.getEntryKeys().addAll(List.of(key5, key6));
        owner.addNewAccountOrNewKey(account);
        Mockito.when(ownerRepository.findByKey(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        var result = useCase.createOrUpdateEntryKey(dto).fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertEquals("Owner is not allowed to have more keys", notification.getViolations().getFirst().reason());
                    return notification;
                },
                _ -> {
                    return null;
                }
        );
        Assertions.assertInstanceOf(Notification.class, result);
    }



    @Test
    void givenInvalidAccount_whenCalling_createOrUpdateEntryKey_shouldNotification() {
        var dto = OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.CPF);
        var owner  = Owner.getInstance(dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity());
        var invalidAccount = OwnerDtoMock.getOwnerDtoWithInvalidAccount(owner);
        Mockito.when(ownerRepository.findByKey(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        var result = useCase.createOrUpdateEntryKey(invalidAccount).fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertTrue(notification.hasError());
                    Assertions.assertEquals("Branch is invalid.", notification.getViolations().getFirst().reason());
                    return notification;
                },
                owner1 -> {
                    return owner1;
                }
        );
        Assertions.assertInstanceOf(Notification.class, result);
    }

    @Test
    void givenNoExistentKey_whenCalling_createOrUpdateEntryKey_shouldUpdateOwner() {
        var dto = OwnerDtoMock.getOwnerDto(TypePerson.NATURAL_PERSON, TypeKey.CPF);
        var owner  = Owner.getInstance(dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity());
        var newDto = OwnerDtoMock.fromOwner(owner, EntryKeyDtoMock.getEntryKeyDto(TypeKey.EMAIL));
        Mockito.when(ownerRepository.findByKey(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        var result = useCase.createOrUpdateEntryKey(newDto).fold(
                notification -> {
                    Assertions.assertNull(notification);
                    return null;
                },
                owner1 -> {
                    Assertions.assertNotNull(owner1);
                    Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), owner1.getTaxIdNumber().getTaxIdNumber());
                    Assertions.assertEquals(2, owner1.getAccounts().getFirst().getEntryKeys().size());
                    return owner1;
                }
        );
        Assertions.assertInstanceOf(Owner.class, result);
    }
}
