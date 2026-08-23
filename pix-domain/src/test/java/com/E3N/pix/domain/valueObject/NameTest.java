package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.name.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.stream.Stream;

public class NameTest extends UnitTest {

//    valid name legal person
    @ParameterizedTest
    @MethodSource("providerValidNamePerson")
    public void givenValidNames_shouldReturnName(final String name, final TypePerson typePerson) {
        var result = Name.getInstance(name, typePerson);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertEquals(result.getName(), name);
        Assertions.assertNull(result.getNotification());
    }

    static Stream<Arguments> providerValidNamePerson(){
        return Stream.of(
                Arguments.of("Maria Eduarda Silva", TypePerson.NATURAL_PERSON),
                Arguments.of("João Pedro Oliveira", TypePerson.NATURAL_PERSON),
                Arguments.of("Ana Carolina Santos", TypePerson.NATURAL_PERSON),
                Arguments.of("José Carlos Ferreira", TypePerson.NATURAL_PERSON),
                Arguments.of("Emily Rose Johnson", TypePerson.NATURAL_PERSON),
                Arguments.of("James William Smith", TypePerson.NATURAL_PERSON),
                Arguments.of("María José García", TypePerson.NATURAL_PERSON),
                Arguments.of("Juan Carlos Rodríguez", TypePerson.NATURAL_PERSON),
                Arguments.of("Sofía Isabella Martínez", TypePerson.NATURAL_PERSON),
                Arguments.of("Carlos Eduardo de Souza", TypePerson.NATURAL_PERSON)
        );
    }

    // invalid name for legal person
    @ParameterizedTest
    @MethodSource("providerInvalidNamePerson")
    public void givenInvalidNames_shouldReturnNull(final String name, final TypePerson typePerson) {
        var result = Name.getInstance(name, typePerson);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertNull(result.getName());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        var messagesErrors = Arrays.asList(
                "Name must not be null.",
                "Name should not have special characters.",
                "TypePerson must not be null.",
                "Must be a full name.",
                "Min size of name is 3, Max size is 100."
        );
        Assertions.assertTrue(messagesErrors.contains(result.getNotification().getErrors().getFirst().message()));
    }

    static Stream<Arguments> providerInvalidNamePerson(){
        return Stream.of(
                Arguments.of("Maria", TypePerson.NATURAL_PERSON),
                Arguments.of("João Pedro Oliveira@", TypePerson.NATURAL_PERSON),
                Arguments.of(" ", TypePerson.NATURAL_PERSON),
                Arguments.of("Jo", TypePerson.NATURAL_PERSON),
                Arguments.of("Você gostaria de saber como funciona o processo para mudar de nome diretamente no cartório ou quer ver.", TypePerson.NATURAL_PERSON),
                Arguments.of("James @@William Smith", TypePerson.NATURAL_PERSON),
                Arguments.of("María#", TypePerson.NATURAL_PERSON),
                Arguments.of("Juan Carlos$ Rodríguez", TypePerson.NATURAL_PERSON),
                Arguments.of("", TypePerson.NATURAL_PERSON)
        );
    }

    // validate names for legal person
    @ParameterizedTest
    @MethodSource("providerValidNameForLegalPerson")
    public void givenValidTradesNames_shouldReturnName(final String name, final TypePerson typePerson) {
        var result = Name.getInstance(name, typePerson);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertEquals(result.getName(), name);
        Assertions.assertNull(result.getNotification());
    }

    static Stream<Arguments> providerValidNameForLegalPerson(){
        return Stream.of(
                Arguments.of("Velo", TypePerson.LEGAL_PERSON),
                Arguments.of("Zeta", TypePerson.LEGAL_PERSON),
                Arguments.of("Aura", TypePerson.LEGAL_PERSON),
                Arguments.of("Nova", TypePerson.LEGAL_PERSON),
                Arguments.of("Flux", TypePerson.LEGAL_PERSON),
                Arguments.of("Apex", TypePerson.LEGAL_PERSON),
                Arguments.of("Cred", TypePerson.LEGAL_PERSON),
                Arguments.of("Vibe", TypePerson.LEGAL_PERSON),
                Arguments.of("Bold", TypePerson.LEGAL_PERSON),
                Arguments.of("Axis Souza", TypePerson.LEGAL_PERSON)
        );
    }

// invalid legal person names
    @ParameterizedTest
    @MethodSource("providerInvalidNameForLegalPerson")
    public void givenInvalidNamesForLegalPerson_shouldReturnNameWithNotification(final String name, final TypePerson typePerson) {
        var result = Name.getInstance(name, typePerson);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertNull(result.getName());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        var messagesErrors = Arrays.asList(
                "Name must not be null.",
                "Name should not have special characters.",
                "TypePerson must not be null.",
                "Must be a full name.",
                "Min size of name is 3, Max size is 100."
        );
        Assertions.assertTrue(messagesErrors.contains(result.getNotification().getErrors().getFirst().message()));
    }


    static Stream<Arguments> providerInvalidNameForLegalPerson(){
        return Stream.of(
                Arguments.of("Velo@", TypePerson.LEGAL_PERSON),
                Arguments.of("Zeta#", TypePerson.LEGAL_PERSON),
                Arguments.of("Au", TypePerson.LEGAL_PERSON),
                Arguments.of(" ", TypePerson.LEGAL_PERSON),
                Arguments.of("", TypePerson.LEGAL_PERSON),
                Arguments.of("Você gostaria de saber como funciona o processo para mudar de nome diretamente no cartório ou quer ver.", TypePerson.LEGAL_PERSON),
                Arguments.of("Cred __", TypePerson.LEGAL_PERSON),
                Arguments.of("Vibe #", TypePerson.LEGAL_PERSON),
                Arguments.of(null, TypePerson.LEGAL_PERSON),
                Arguments.of("Vibe", null),
                Arguments.of("Axis$ Souza", TypePerson.LEGAL_PERSON)
        );
    }
}
