package com.E3N.pix.service.owner;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.pix.service.owner.mocks.MockOwner;
import com.E3N.pix.service.owner.mocks.dto.OwnerDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class OwnerServiceTest extends UniTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @ParameterizedTest
    @MethodSource("provider")
    public void givenAValidOwnerDto_shouldCreateOwner(OwnerDto dto) {
        var expectedOwner = MockOwner.get(dto);
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(null);
        Mockito.when(ownerRepository.save(Mockito.any(Owner.class)))
                .thenReturn(expectedOwner);

        Either<Notification, Owner> result = ownerService.getOrCreate(dto);
        Owner owner = null;
        Notification notification = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertNull(notification);
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenAValidOwnerDto_shouldReturnOwnerFromBD(OwnerDto dto) {
        var expectedOwner = MockOwner.get(dto);
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(expectedOwner);
        Mockito.when(ownerRepository.save(Mockito.any(Owner.class)))
                .thenReturn(expectedOwner);

        Either<Notification, Owner> result = ownerService.getOrCreate(dto);
        Owner owner = null;
        Notification notification = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertNull(notification);
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.CPF)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.PHONE)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.CNPJ)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.PHONE)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getOwner(TypePerson.LEGAL_PERSON, TypeKey.EMAIL))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOwners")
    public void givenInvalidOwnerDto_shouldReturnNotification(OwnerDto dto) {
        var expectedOwner = MockOwner.get(dto);
        Mockito.when(ownerRepository.findByTaxIdNumber(dto.taxIdNumber()))
                .thenReturn(null);
        Mockito.when(ownerRepository.save(Mockito.any(Owner.class)))
                .thenReturn(expectedOwner);

        Either<Notification, Owner> result = ownerService.getOrCreate(dto);
        Owner owner = null;
        Notification notification = null;
        if (result instanceof Either.Right<Notification, Owner>(Owner value1)) {
            owner = value1;
        } else if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        }
        assert notification != null;
        List<String> violations = notification.getViolations()
                .stream()
                .map(Violation::reason)
                .toList();
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertTrue(notification.hasError());
        Assertions.assertTrue(expectedErrors().containsAll(violations));
        Assertions.assertNull(owner);
    }

    static Stream<Arguments> provideInvalidOwners() {
        return Stream.of(
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.NATURAL_PERSON, TypeKey.CPF)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.NATURAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.NATURAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.NATURAL_PERSON, TypeKey.PHONE)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.CNPJ)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.EVP)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.EMAIL)),
                Arguments.of(OwnerDtoMock.getInvalidOwner(TypePerson.LEGAL_PERSON, TypeKey.PHONE))
        );
    }

    static List<String> expectedErrors() {
        return Arrays.asList(
                "Name must not be null.",
                "Name should not have special characters.",
                "TypePerson must not be null.",
                "Must be a full name.",
                "Min size of name is 3, Max size is 100.",
                "TaxIdNumber is invalid."
        );
    }

}
