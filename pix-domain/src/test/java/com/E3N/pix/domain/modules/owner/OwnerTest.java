package com.E3N.pix.domain.modules.owner;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.mocks.AccountMock;
import com.E3N.pix.domain.mocks.EntryKeyMock;
import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

public class OwnerTest extends UnitTest {
    // valid natural person
    @ParameterizedTest
    @MethodSource("providerNaturalPerson")
    public void givenValidNaturalPerson_shouldReturnOwner(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        var result = Owner.getInstance(name, null, taxIdNumber, type, account);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertEquals(name, result.getName().getName());
        Assertions.assertEquals(taxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
        Assertions.assertEquals(type, result.getType());
    }

    static Stream<Arguments> providerNaturalPerson() {
        return Stream.of(
                Arguments.of(
                        "Edson Jose de Souza",
                        "27305672246",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("27305672246"))),
                Arguments.of(
                        "Lucineia Souza",
                        "47820019590",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("47820019590"))),
                Arguments.of(
                        "Juliana Souza",
                        "85628685828",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("85628685828"))),
                Arguments.of(
                        "João Silva",
                        "92212939043",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("92212939043"))),
                Arguments.of(
                        "Jose Ribeiro",
                        "96639666022",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("96639666022"))),
                Arguments.of(
                        "Pedro Silva",
                        "74427945562",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("74427945562"))),
                Arguments.of(
                        "Antonio Silva",
                        "75628144657",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("75628144657"))),
                Arguments.of(
                        "Jussara Vaconcelos",
                        "99467334470",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("99467334470"))),
                Arguments.of(
                        "Rachel Green",
                        "75291652160",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("75291652160"))),
                Arguments.of(
                        "Robert Green",
                        "73735342019",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("73735342019")))
        );
    }

    //    // invalid natural person
    @ParameterizedTest
    @MethodSource("providerInvalidNaturalPerson")
    public void givenInvalidNaturalPerson_shouldReturnOwnerWithNotification(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        var result = Owner.getInstance(name, null, taxIdNumber, type, account);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertTrue(Arrays.asList(
                "Must be a full name.",
                "TaxIdNumber is invalid.",
                "Person type is required.",
                "Name should not have special characters",
                "Min size of name is 3, Max size is 100.",
                "Invalid value.",
                taxIdNumber + "Invalid value.",
                "Type person is required.",
                "Name must not be null",
                "TypePerson must not be null.",
                "Name should not have special characters.",
                "Name must not be null.",
                "73735342019 is invalid.",
                "TradeName is required for Legal person.",
                "Branch is invalid.",
                "Account Number is invalid.",
                "OpeningDate is required.",
                "Participant is invalid.",
                "Account Type is required."
        ).contains(result.getNotification().getViolations().getFirst().reason()));
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
    }

    static Stream<Arguments> providerInvalidNaturalPerson() {
        return Stream.of(
                Arguments.of(
                        "Edson Jose de Souza",
                        "27304672246",  // invalid
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("27304672246"))),
                Arguments.of(
                        "Lucineia", // invalid
                        "47820019590",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("47820019590"))),
                Arguments.of(
                        "Juliana Souza",
                        "85628684828",
                        TypePerson.LEGAL_PERSON, // invalid
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("85628684828"))),
                Arguments.of(
                        "João Silva",
                        "92212939043",
                        null, // invalid
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCpf("92212939043"))),
                Arguments.of(
                        "Jose Ribeiro",
                        "96639666022",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getInvalidOne()),
                Arguments.of(
                        "Pedro @Silva",
                        "74427945562",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj(RandomKeysMock.randomInvalidEmails()))),
                Arguments.of(
                        "An",
                        "75628144657",
                        TypePerson.NATURAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj(RandomKeysMock.randomInvalidNaturalPersonDocuments())))
        );
    }

    //    // valid legal person
    @ParameterizedTest
    @MethodSource("providerLegalPerson")
    public void givenValidLegalPerson_shouldReturnOwner(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        var result = Owner.getInstance(name, name, taxIdNumber, type, account);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertEquals(name, result.getName().getName());
        Assertions.assertEquals(taxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
        Assertions.assertEquals(type, result.getType());
    }

    static Stream<Arguments> providerLegalPerson() {
        return Stream.of(
                Arguments.of("Velo", "17KM7BNM000151", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("17KM7BNM000151"))),
                Arguments.of("Zeta", "CL10Z5JZ000134", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("CL10Z5JZ000134"))),
                Arguments.of("Aura", "XEHX5J79000191", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("XEHX5J79000191"))),
                Arguments.of("Nova", "V86XNGWX000196", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("V86XNGWX000196"))),
                Arguments.of("Flux", "6B8S3VTB000122", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("6B8S3VTB000122"))),
                Arguments.of("Apex", "RGB71A0H000169", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("RGB71A0H000169"))),
                Arguments.of("Bold", "VZALTZY2000144", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("VZALTZY2000144"))),
                Arguments.of("Cred", "2KJZRGZP000173", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("2KJZRGZP000173"))),
                Arguments.of("Axis", "003LAMMX000174", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("003LAMMX000174"))),
                Arguments.of("Vibe", "J7BBACJG000159", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("J7BBACJG000159"))),
                Arguments.of("Spur Company", "49457308000150", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("49457308000150"))),
                Arguments.of("Shift", "04540088000195", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("04540088000195"))),
                Arguments.of("Core", "22990545000125", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("17KM7BNM000151"))),
                Arguments.of("Pace", "02571632000130", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("22990545000125"))),
                Arguments.of("Grid", "65349509000190", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("65349509000190"))),
                Arguments.of("Echo", "68654016000151", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("68654016000151"))),
                Arguments.of("Loom", "90205091000194", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("90205091000194"))),
                Arguments.of("Fuse", "73587047000177", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("73587047000177"))),
                Arguments.of("Prism", "67208003000196", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("67208003000196"))),
                Arguments.of("Onyx", "62352987000160", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("62352987000160")))
        );
    }

    //    // invalid legal person
    @ParameterizedTest
    @MethodSource("providerInvalidLegalPerson")
    public void givenInvalidLegalPerson_shouldReturnOwnerWithNotification(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        var result = Owner.getInstance(name, name, taxIdNumber, type, account);
        Assertions.assertInstanceOf(Owner.class, result);
        String accountsError = "";
        if (account.getEntryKeys().getFirst().getNotification().hasError()) {
            accountsError = account.getEntryKeys().getFirst().getNotification().getViolations().getFirst().reason();
        }
        var expectedErrors = Arrays.asList(
                "Must be a full name.",
                "TaxIdNumber is invalid.",
                "Person type is required.",
                "Name should not have special characters",
                "Min size of name is 3, Max size is 100.",
                "Invalid value.",
                taxIdNumber + " is invalid.",
                "Type person is required.",
                "Name must not be null",
                "TypePerson must not be null.",
                "Name should not have special characters.",
                "Name must not be null.",
                "73735342019 is invalid.",
                "TradeName is required for Legal person.",
                accountsError,
                "Person type is required."
        );
        for (Violation v : result.getNotification().getViolations()) {
            Assertions.assertTrue(expectedErrors.contains(v.reason()));
        }
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
    }

    static Stream<Arguments> providerInvalidLegalPerson() {
        return Stream.of(
                Arguments.of("Aura", "XEHG5J79000191", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("XEHG5J79000191"))), // invalid legal_person
                Arguments.of("Nova --", "V86XNGWX000196", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("V86XNGWX000196"))), // invalid name
                Arguments.of("Flux __", "6B8S3VTB000122", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("6B8S3VTB000122"))), // invalid name
                Arguments.of("Apex", "RGB71t0H000169", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("RGB71t0H000169"))), // invalid taxIdNumber
                Arguments.of("Bold", "VZALT2Y2000144", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("VZALT2Y2000144"))), // invalid taxIdNumber
                Arguments.of(" ", "2KJZRGZP000173", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("2KJZRGZP000173"))), // invalid name
                Arguments.of("", "003LAMMX000174", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("003LAMMX000174"))), // invalid name
                Arguments.of("Vibe", "J7BBAC9G000159", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("J7BBAC9G000159"))), // invalid taxIdNumber
                Arguments.of("Spur @Company", "49457308000150", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("49457308000150"))), // invalid name
                Arguments.of("Shift", "04540078000195", null,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("04540078000195"))), // invalid keyOwnership
                Arguments.of("Core", "PL0ARZK0000159", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyInvalidEmail())),
                Arguments.of("", "02571632000130", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("02571632000130"))), // invalid name
                Arguments.of("Grid", "65349509000190", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("XEHG5J79000191"))), // taxIdNumber is different from key
                Arguments.of("Echo", "68654016000151", null,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("68654016000151"))), // type person invalid
                Arguments.of("Loom", "90205191000194", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("XEHG5J79000191"))), // taxIdNumber invalid
                Arguments.of("Fu", "73587045000177", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("73587045000177"))), // name invalid
                Arguments.of("Prism", "67208003000196", TypePerson.LEGAL_PERSON,
                        AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyCnpj("XEHG5J79000191"))) // keyOwnership invalid
        );
    }

}
