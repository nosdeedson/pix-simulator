package com.E3N.pix.application.modules;

import com.E3N.pix.application.CreateEntryKeyUseCase;
import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.mocks.OwnerDtoMock;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.owner.dto.OwnerDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class CreateEntryKeyUseCaseTest extends UnitTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private CreateEntryKeyUseCase useCase;

    static List<Arguments> providerKeyExists() {
        return List.of(
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.CNPJ)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.PHONE)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.CPF)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.PHONE))
        );
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @ParameterizedTest
    @MethodSource("providerKeyExists")
    public void givenExistingOwnerCreatingSameKey_whenCallingCreateOrUpdateEntryKey_shouldReturnKeyAlreadyExist(OwnerDto expectedOwnerDto) {
        var expectedMessage = "Key already exists.";
        var expectedOwner = Owner.getInstance(
                expectedOwnerDto.name(), expectedOwnerDto.tradeName(), expectedOwnerDto.taxIdNumber(), expectedOwnerDto.typePerson(),
                expectedOwnerDto.account().toEntity()
        );
        Mockito.when(ownerRepository.findByKey(expectedOwnerDto.account().entryKeyDto().key()))
                .thenReturn(Optional.of(expectedOwner));

        Either<Notification, Owner> result = this.useCase.createOrUpdateEntryKey(expectedOwnerDto);
        Notification notification = null;
        Owner owner = null;
        if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertNull(owner);
        Assertions.assertEquals(expectedMessage, notification.getViolations().getFirst().reason());
    }

    @ParameterizedTest
    @MethodSource("providerKeyExists")
    public void givenExistingOwnerFromOtherBank_whenCallingCreateOrUpdateEntryKey_shouldReturnKeyInAnotherBank(OwnerDto dto) {
        var expectedMessage = "Key is registered in another bank, create a portability or claims ownership";
        var expectedOwner = Owner.getInstance(
                dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(),
                dto.account().toEntity()
        );
        var expectedDto = OwnerDtoMock.getOwner(dto);
        Mockito.when(ownerRepository.findByKey(expectedDto.account().entryKeyDto().key()))
                .thenReturn(Optional.of(expectedOwner));
        Either<Notification, Owner> result = this.useCase.createOrUpdateEntryKey(expectedDto);
        Notification notification = null;
        Owner owner = null;
        if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertNull(owner);
        Assertions.assertEquals(expectedMessage, notification.getViolations().getFirst().reason());
    }

    @ParameterizedTest
    @MethodSource("providerKeyExists")
    public void givenNoneExistentOwner_whenCallingCreateOrUpdateEntryKey_shouldCreateOne(OwnerDto dto) {
        var expectedOwner = Owner.getInstance(
                dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity()
        );
        Mockito.when(ownerRepository.findByKey(dto.account().entryKeyDto().key()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.save(Mockito.any()))
                .thenReturn(expectedOwner);
        Either<Notification, Owner> result = this.useCase.createOrUpdateEntryKey(dto);
        Notification notification = null;
        Owner owner = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value1)) {
            notification = value1;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(expectedOwner.getId(), owner.getId());
        Assertions.assertNull(notification);
    }

    @ParameterizedTest
    @MethodSource("providerKeyExists")
    public void givenExistentOwner_whenCallingCreateOrUpdateEntryKey_shouldUpdateOwner(OwnerDto dto) {
        var expectedOwner = Owner.getInstance(
                dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity()
        );
        Mockito.when(ownerRepository.findByKey(dto.account().entryKeyDto().key()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(Optional.of(expectedOwner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(expectedOwner);
        Either<Notification, Owner> result = this.useCase.createOrUpdateEntryKey(dto);
        Notification notification = null;
        Owner owner = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value1)) {
            notification = value1;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertNull(notification);
        Assertions.assertEquals(expectedOwner.getId(), owner.getId());
    }

    @ParameterizedTest
    @MethodSource("providerKeyExists")
    public void givenExistentOwner_whenCallingCreateOrUpdateEntryKey_shouldReturnNotificationIfNewKeyInvalid(OwnerDto dto) {
        var invalidDto = OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.CNPJ);
        var expectedOwner = Owner.getInstance(
                dto.name(), dto.tradeName(), dto.taxIdNumber(), dto.typePerson(), dto.account().toEntity()
        );
        Mockito.when(ownerRepository.findByKey(dto.account().entryKeyDto().key()))
                .thenReturn(Optional.empty());
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(Optional.of(expectedOwner));
        Either<Notification, Owner> result = this.useCase.createOrUpdateEntryKey(invalidDto);
        Owner owner = null;
        Notification notification = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value1)) {
            notification = value1;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertNull(owner);
    }
}
