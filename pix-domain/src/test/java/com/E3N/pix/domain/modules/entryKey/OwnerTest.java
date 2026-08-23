package com.E3N.pix.domain.modules.entryKey;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.Error;
import com.E3N.pix.domain.validation.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

public class OwnerTest extends UnitTest {
    // valid natural person
    @ParameterizedTest
    @MethodSource("providerNaturalPerson")
    public void givenValidNaturalPerson_shouldReturnOwner(
            final String keyOwnershipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        var result = Owner.getInstanceNaturalPerson(keyOwnershipDate, name, taxIdNumber, type);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertInstanceOf(Instant.class, result.getKeyOwnerShipDate());
        Assertions.assertEquals(name, result.getName().getName());
        Assertions.assertEquals(taxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
        Assertions.assertEquals(type, result.getType());
    }

    static Stream<Arguments> providerNaturalPerson() {
        return Stream.of(
                Arguments.of("12/08/2026", "Edson Jose de Souza", "27305672246", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Lucineia Souza", "47820019590", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Juliana Souza", "85628685828", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "João Silva", "92212939043", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Jose Ribeiro", "96639666022", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Pedro Silva", "74427945562", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Antonio Silva", "75628144657", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Jussara Vaconcelos", "99467334470", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Rachel Green", "75291652160", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Robert Green", "73735342019", TypePerson.NATURAL_PERSON)
        );
    }

    // invalid natural person
    @ParameterizedTest
    @MethodSource("providerInvalidNaturalPerson")
    public void givenInvalidNaturalPerson_shouldReturnOwnerWithNotification(
            final String keyOwnershipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        var result = Owner.getInstanceNaturalPerson(keyOwnershipDate, name, taxIdNumber, type);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertTrue(Arrays.asList(
                new Error("KeyOwnershipDate is required."),
                new Error("Must be a full name."),
                new Error(taxIdNumber + " is invalid."),
                new Error("Person type is required."),
                new Error("Name should not have special characters"),
                new Error("Min size of name is 3, Max size is 100."),
                new Error("Invalid value."),
                new Error(taxIdNumber + "Invalid value."),
                new Error("Type person is required."),
                new Error("Name must not be null"),
                new Error("TypePerson must not be null."),
                new Error("Name should not have special characters."),
                new Error("Name must not be null."),
                new Error("73735342019 is invalid."),
                new Error("TradeName is required for Legal person.")
        ).containsAll(result.getNotification().getErrors()));
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
    }

    static Stream<Arguments> providerInvalidNaturalPerson() {
        return Stream.of(
                Arguments.of("12/08/202623", "Edson Jose de Souza", "27305672246", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Lucineia", "47820019590", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Juliana Souza", "85628684828", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "João Silva", "92212939043", null),
                Arguments.of(null, "Jose Ribeiro", "96639666022", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Pedro @Silva", "74427945562", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "An", "75628144657", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", null, "99467334470", TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Rachel Green", null, TypePerson.NATURAL_PERSON),
                Arguments.of("12/08/2026", "Robert Green", "73735342019", TypePerson.LEGAL_PERSON)
        );
    }

    // valid legal person
    @ParameterizedTest
    @MethodSource("providerLegalPerson")
    public void givenValidLegalPerson_shouldReturnOwner(
            final String keyOwnershipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        var result = Owner.getInstanceLegalPerson(keyOwnershipDate, name, taxIdNumber, name, type);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertInstanceOf(Instant.class, result.getKeyOwnerShipDate());
        Assertions.assertEquals(name, result.getName().getName());
        Assertions.assertEquals(taxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
        Assertions.assertEquals(type, result.getType());
    }

    static Stream<Arguments> providerLegalPerson() {
        return Stream.of(
                Arguments.of("12/08/2026", "Velo", "17KM7BNM000151", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Zeta", "CL10Z5JZ000134", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Aura", "XEHX5J79000191", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Nova", "V86XNGWX000196", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Flux", "6B8S3VTB000122", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Apex", "RGB71A0H000169", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Bold", "VZALTZY2000144", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Cred", "2KJZRGZP000173", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Axis", "003LAMMX000174", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Vibe", "J7BBACJG000159", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Spur Company", "49457308000150", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Shift", "04540088000195", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Core", "22990545000125", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Pace", "02571632000130", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Grid", "65349509000190", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Echo", "68654016000151", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Loom", "90205091000194", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Fuse", "73587047000177", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Prism", "67208003000196", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Onyx", "62352987000160", TypePerson.LEGAL_PERSON)
        );
    }

    // invalid legal person
    @ParameterizedTest
    @MethodSource("providerInvalidLegalPerson")
    public void givenInvalidLegalPerson_shouldReturnOwnerWithNotification(
            final String keyOwnershipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        var result = Owner.getInstanceLegalPerson(keyOwnershipDate, name, taxIdNumber, name, type);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertTrue(Arrays.asList(
                new Error("KeyOwnershipDate is required."),
                new Error("Must be a full name."),
                new Error(taxIdNumber + " is invalid."),
                new Error("Person type is required."),
                new Error("Name should not have special characters"),
                new Error("Min size of name is 3, Max size is 100."),
                new Error("Invalid value."),
                new Error(taxIdNumber + "Invalid value."),
                new Error("Type person is required."),
                new Error("Name must not be null"),
                new Error("TypePerson must not be null."),
                new Error("Name should not have special characters."),
                new Error("Name must not be null."),
                new Error("73735342019 is invalid."),
                new Error("TradeName is required for Legal person.")
        ).containsAll(result.getNotification().getErrors()));
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
    }

    static Stream<Arguments> providerInvalidLegalPerson() {
        return Stream.of(
                Arguments.of("12/08/202622", "Aura", "XEHX5J79000191", TypePerson.LEGAL_PERSON), // invalid keyOwnership
                Arguments.of("12/08/2026", "Nova --", "V86XNGWX000196", TypePerson.LEGAL_PERSON), // invalid name
                Arguments.of("12/08/2026", "Flux __", "6B8S3VTB000122", TypePerson.LEGAL_PERSON), // invalid name
                Arguments.of("12/08/2026", "Apex", "RGB71t0H000169", TypePerson.LEGAL_PERSON), // invalid taxIdNumber
                Arguments.of("12/08/2026", "Bold", "VZALT2Y2000144", TypePerson.LEGAL_PERSON), // invalid taxIdNumber
                Arguments.of("12/08/2026", " ", "2KJZRGZP000173", TypePerson.LEGAL_PERSON), // invalid name
                Arguments.of("12/08/2026", "", "003LAMMX000174", TypePerson.LEGAL_PERSON), // invalid name
                Arguments.of("12/08/2026", "Vibe", "J7BBAC9G000159", TypePerson.LEGAL_PERSON), // invalid taxIdNumber
                Arguments.of("12/08/2026", "Spur @Company", "49457308000150", TypePerson.LEGAL_PERSON), // invalid name
                Arguments.of("34/08/2026", "Shift", "04540078000195", TypePerson.LEGAL_PERSON), // invalid keyOwnership
                Arguments.of("12/08/2026", "Core", null, TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", null, "02571632000130", TypePerson.LEGAL_PERSON),
                Arguments.of(null, "Grid", "65349509000190", TypePerson.LEGAL_PERSON),
                Arguments.of("12/08/2026", "Echo", "68654016000151", null),
                Arguments.of("12/08/2026", "Loom", "90205191000194", TypePerson.LEGAL_PERSON), // taxIdNumber invalid
                Arguments.of("12/08/2026", "Fu", "73587047000177", TypePerson.LEGAL_PERSON), // name invalid
                Arguments.of("12/13/2026", "Prism", "67208003000196", TypePerson.LEGAL_PERSON), // keyOwnership invalid
                Arguments.of("12/08/2026", "Onyx", "62352987000160", TypePerson.NATURAL_PERSON)
        );
    }

    @Test
    public void givenInvalidLegalPersonWithNameAndTradeNameDifferent_shouldReturnOwner() {
        var expectedKeyOwnership = "23/08/2026";
        var expectedName = "Company name";
        var expectedTaxIdNumber = "8RH4HUBO000174";
        var expectedTradeName = "Some words";
        var result = Owner.getInstanceLegalPerson(expectedKeyOwnership, expectedName, expectedTaxIdNumber, expectedTradeName, TypePerson.LEGAL_PERSON);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertNull(result.getNotification());
        Assertions.assertEquals(expectedName, result.getName().getName());
        Assertions.assertEquals(expectedTradeName, result.getTradeName().getName());
        Assertions.assertEquals(expectedTaxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
    }
}
