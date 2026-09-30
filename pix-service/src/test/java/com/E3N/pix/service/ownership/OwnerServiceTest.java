package com.E3N.pix.service.ownership;

import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.ownership.dto.OwnerDto;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.pix.service.ownership.mocks.dto.OwnerDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class OwnerServiceTest extends UniTest {

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenAValidOwnerDto_shouldCreateOwner(OwnerDto dto) {
        Owner result = ownerService.create(dto);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertFalse(result.getNotification().hasError());
    }

    static List<Arguments> provider() {
        return List.of(
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
        Owner result = ownerService.create(dto);
        List<String> violations = result.getNotification().getViolations()
                .stream()
                .map(Violation::reason)
                .toList();
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertTrue(expectedErrors().containsAll(violations));
    }

    static List<Arguments> provideInvalidOwners() {
        return List.of(
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

    @Test
    void givenExistentKeyValues_whenCalling_validateKeyExistence_shouldReturnNotification() {
        var owner = OwnerMock.mockOwner();
        var dto = OwnerDtoMock.from(owner);
        var result = ownerService.validateKeyExistence(owner, dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(1, result.getViolations().size());
        Assertions.assertTrue(result.hasError());
        Assertions.assertEquals("Key already exists.", result.getViolations().getFirst().reason());
    }

    @Test
    void givenExistentKeyValues_whenCalling_validateKeyExistenceWithDifferentAccount_shouldReturnNotification() {
        var owner = OwnerMock.mockOwner();
        var dto = OwnerDtoMock.fromDifferentAccount(owner);
        var result = ownerService.validateKeyExistence(owner, dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(1, result.getViolations().size());
        Assertions.assertTrue(result.hasError());
        Assertions.assertEquals("Key is registered in another bank, create a portability or claims ownership.", result.getViolations().getFirst().reason());
    }

    @Test
    void givenDifferentParticipants_whenCalling_validateKeyExistence_shouldReturnNotificationZeroError() {
        var owner = OwnerMock.mockOwner();
        var dto = OwnerDtoMock.getOwner(TypePerson.NATURAL_PERSON, TypeKey.EVP);
        var result = ownerService.validateKeyExistence(owner, dto);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals(0, result.getViolations().size());
        Assertions.assertFalse(result.hasError());
    }
}
