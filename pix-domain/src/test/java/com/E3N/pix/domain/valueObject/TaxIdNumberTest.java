package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TaxIdNumberTest {

    @ParameterizedTest
    @MethodSource("providerNaturalPerson")
    public void givenValidNaturalPersonDocument_shouldReturnOk(
            final TypePerson type,
            final String document
    ){
        var result = TaxIdNumber.getInstance(type, document);
        Assertions.assertInstanceOf(TaxIdNumber.class, result);
        Assertions.assertNull(result.getNotification());
        Assertions.assertEquals(type, result.getTypePerson());
        Assertions.assertEquals(document, result.getTaxIdNumber());
    }

    @ParameterizedTest
    @MethodSource("providerInvalidNaturalPerson")
    public void givenInvalidNaturalPersonDocument_shouldReturnOk(
            final TypePerson type,
            final String document
    ){
        var result = TaxIdNumber.getInstance(type, document);
        Assertions.assertInstanceOf(TaxIdNumber.class, result);
        Assertions.assertNull(result.getTypePerson());
        Assertions.assertNull(result.getTaxIdNumber());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        var expectedMessage = document + " is invalid.";
        Assertions.assertEquals(expectedMessage, result.getNotification().getErrors().getFirst().message());
    }

    @ParameterizedTest
    @MethodSource("providerLegalPerson")
    public void givenValidLegalPersonDocument_shouldReturnOk(
            final TypePerson type,
            final String document
    ){
        var result = TaxIdNumber.getInstance(type, document);
        Assertions.assertInstanceOf(TaxIdNumber.class, result);
        Assertions.assertNull(result.getNotification());
        Assertions.assertEquals(type, result.getTypePerson());
        Assertions.assertEquals(document, result.getTaxIdNumber());
    }

    @ParameterizedTest
    @MethodSource("providerInvalidLegalPerson")
    public void givenInvalidLegalPersonDocument_shouldReturnOk(
            final TypePerson type,
            final String document
    ){
        var result = TaxIdNumber.getInstance(type, document);
        Assertions.assertInstanceOf(TaxIdNumber.class, result);
        Assertions.assertNull(result.getTypePerson());
        Assertions.assertNull(result.getTaxIdNumber());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        var expectedMessage = document + " is invalid.";
        Assertions.assertEquals(expectedMessage, result.getNotification().getErrors().getFirst().message());
    }

    static Stream<Arguments> providerNaturalPerson(){
        return Stream.of(
                Arguments.of(TypePerson.NATURAL_PERSON, "29037866077"),
                Arguments.of(TypePerson.NATURAL_PERSON, "31420919202"),
                Arguments.of(TypePerson.NATURAL_PERSON, "92034257723"),
                Arguments.of(TypePerson.NATURAL_PERSON, "58625198406"),
                Arguments.of(TypePerson.NATURAL_PERSON, "94861912636"),
                Arguments.of(TypePerson.NATURAL_PERSON, "99363736504"),
                Arguments.of(TypePerson.NATURAL_PERSON, "19981877565"),
                Arguments.of(TypePerson.NATURAL_PERSON, "21721338250"),
                Arguments.of(TypePerson.NATURAL_PERSON, "06866706807"),
                Arguments.of(TypePerson.NATURAL_PERSON, "81090961154")
        );
    }

    static Stream<Arguments> providerLegalPerson(){
        return Stream.of(
                Arguments.of(TypePerson.LEGAL_PERSON, "16818539000192"),
                Arguments.of(TypePerson.LEGAL_PERSON, "95773457000145"),
                Arguments.of(TypePerson.LEGAL_PERSON, "68430952000189"),
                Arguments.of(TypePerson.LEGAL_PERSON, "49584681000171"),
                Arguments.of(TypePerson.LEGAL_PERSON, "94411055000138"),
                Arguments.of(TypePerson.LEGAL_PERSON, "3103OY0I000183"),
                Arguments.of(TypePerson.LEGAL_PERSON, "PU91HFKD000153"),
                Arguments.of(TypePerson.LEGAL_PERSON, "VRA8ACJS000180"),
                Arguments.of(TypePerson.LEGAL_PERSON, "BRTL6YVF000156"),
                Arguments.of(TypePerson.LEGAL_PERSON, "D4ZWS3PC000126")
        );
    }

    static Stream<Arguments> providerInvalidNaturalPerson(){
        return Stream.of(
                Arguments.of(TypePerson.NATURAL_PERSON, "037866077"),
                Arguments.of(TypePerson.NATURAL_PERSON, "21420918202"),
                Arguments.of(TypePerson.NATURAL_PERSON, "9203425a723"),
                Arguments.of(TypePerson.NATURAL_PERSON, "5862619s406"),
                Arguments.of(TypePerson.NATURAL_PERSON, "94860912636"),
                Arguments.of(TypePerson.NATURAL_PERSON, "99364736504"),
                Arguments.of(TypePerson.NATURAL_PERSON, "19981777565"),
                Arguments.of(TypePerson.NATURAL_PERSON, "21721438250"),
                Arguments.of(TypePerson.NATURAL_PERSON, "0686670680"),
                Arguments.of(TypePerson.NATURAL_PERSON, "81090964")
        );
    }

    static Stream<Arguments> providerInvalidLegalPerson(){
        return Stream.of(
                Arguments.of(TypePerson.LEGAL_PERSON, "16818538000192"),
                Arguments.of(TypePerson.LEGAL_PERSON, "95773456000145"),
                Arguments.of(TypePerson.LEGAL_PERSON, "68430952000"),
                Arguments.of(TypePerson.LEGAL_PERSON, "495846810001"),
                Arguments.of(TypePerson.LEGAL_PERSON, "94411045000138"),
                Arguments.of(TypePerson.LEGAL_PERSON, "3103OY1I000183"),
                Arguments.of(TypePerson.LEGAL_PERSON, "PU91HFjD000153"),
                Arguments.of(TypePerson.LEGAL_PERSON, "VRA8ACyS000180"),
                Arguments.of(TypePerson.LEGAL_PERSON, "BRTL6YxF000156"),
                Arguments.of(TypePerson.LEGAL_PERSON, "D4ZWS3zC000126")
        );
    }
}
