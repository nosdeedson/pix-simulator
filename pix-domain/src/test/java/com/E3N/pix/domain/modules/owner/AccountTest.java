package com.E3N.pix.domain.modules.owner;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.mocks.AccountMock;
import com.E3N.pix.domain.mocks.EntryKeyMock;
import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AccountTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidAttributes_shouldReturnAnAccount(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String opening,
            final EntryKey entryKey
    ) {
        var expectedBranch = StringUtils.leftPad(branch, 4, "0");
        var result = Account.getInstance(expectedBranch,
                number,
                participant,
                type,
                opening,
                entryKey
        );
        Assertions.assertInstanceOf(Account.class, result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(type, result.getType());
        Assertions.assertEquals(number, result.getNumber().getNumber());
        Assertions.assertEquals(expectedBranch, result.getBranch().getBranch());
        Assertions.assertInstanceOf(Instant.class, result.getOpeningDate());
        Assertions.assertEquals(participant, result.getParticipant().getParticipant());
        Assertions.assertEquals(1, result.getEntryKeys().size());
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("1", "1234", "00000000", AccountType.CACC, "12/08/1980", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("2", "1234", "04332281", AccountType.OTHR, "12/08/1980", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("01", "1234", "08357240", AccountType.SLRY, "12/08/1990", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("1", "12345678998765432112", "13673855", AccountType.SVGS, "01/02/2026", EntryKeyMock.getEntryKeyEVP()),
                Arguments.of("1", "1234", "18188384", AccountType.TRAN, "12/08/2022", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("0001", "1234X", "18236120", AccountType.TRAN, "12/08/1999", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("1000", "1234x", "18236120", AccountType.OTHR, "12/09/2023", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("1", "1234", "27351731", AccountType.SLRY, "12/11/1980", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("1", "1234", "30507541", AccountType.SVGS, "12/12/1980", EntryKeyMock.getEntryKeyEVP()),
                Arguments.of("1", "1234", "57HWH4JZ", AccountType.CACC, "12/10/1980", EntryKeyMock.getEntryKeyPhone())
        );
    }

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenInvalidAttributes_shouldReturnAnAccountWithNotification(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String opening,
            final EntryKey entryKey
    ) {
        var result = Account.getInstance(
                branch,
                number,
                participant,
                type,
                opening,
                entryKey
        );
        Assertions.assertInstanceOf(Account.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(Arrays.asList(
                        "Branch is required.",
                        "Branch is invalid.",
                        "Account Number is required.",
                        "Account Number is invalid.",
                        "OpeningDate is required.",
                        "Account Type is required.",
                        "Participant should not be null.",
                        "Participant is invalid.",
                        "11111111111111 is invalid."
                )
                .contains(result.getNotification().getViolations().getFirst().reason()));
    }

    static Stream<Arguments> invalidProvider() {
        return Stream.of(
                Arguments.of("100000", "1234", "00000000", AccountType.CACC, "12/08/1980", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("2", "qqqq", "04332281", AccountType.OTHR, "32/08/1980", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("01", "1234", "08357240", AccountType.SLRY, "12/13/1990", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1", "12345678998765432112a", "13673855", AccountType.SVGS, "01/02/2026", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1", "1234", "18188384", AccountType.TRAN, "12/08/202222", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("00014", "1234X", "18236120", AccountType.TRAN, "12/08/1999", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1000", "1234xgg", "1823612022", AccountType.OTHR, "12/09/2023", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1", "1234", "27351731", null, "12/11/1980", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1", "1234", "3050754100", AccountType.SVGS, "12/12/1980", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("1", "1234", null, AccountType.CACC, "12/10/1980", EntryKeyMock.getInvalidEntryKeyCnpj(1))
        );
    }

    @Test
    public void givenAValidEntryKey_whenCallingAddKey_shouldAddItToAccount() {
        var key = EntryKeyMock.getEntryKeyCnpj();
        var account = AccountMock.getRandomAccountWithSpecificEntryKey(key);
        Assertions.assertEquals(key.getKey().getKey(), account.getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(1, account.getEntryKeys().size());
        var anotherKey = EntryKeyMock.getEntryKeyEmail();
        account.addKey(anotherKey);
        Assertions.assertEquals(2, account.getEntryKeys().size());
        Assertions.assertEquals(key.getKey().getKey(), account.getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(anotherKey.getKey().getKey(), account.getEntryKeys().getLast().getKey().getKey());
        Assertions.assertFalse(account.getNotification().hasError());
    }

    @Test
    public void givenInvalidEntryKey_whenCallingAddKey_shouldReturnAccountWithNotification() {
        var key = EntryKeyMock.getEntryKeyCnpj();
        var account = AccountMock.getRandomAccountWithSpecificEntryKey(key);
        Assertions.assertEquals(key.getKey().getKey(), account.getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(1, account.getEntryKeys().size());
        var invalidKey = EntryKeyMock.getEntryKeyInvalidEmail();
        account.addKey(invalidKey);
        Assertions.assertEquals(2, account.getEntryKeys().size());
        Assertions.assertEquals(key.getKey().getKey(), account.getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertNull(account.getEntryKeys().getLast().getKey());
        Assertions.assertTrue(account.getNotification().hasError());
    }

    @Test
    public void givenExistingAccountNumberAndParticipant_whenCallingSameParticipant_shouldReturnTrue() {
        var entryKey = EntryKeyMock.getEntryKeyCnpj();
        var account = AccountMock.getRandomAccountWithSpecificEntryKey(entryKey);
        var expectedAccountNumber = account.getNumber().getNumber();
        var expectedParticipant = account.getParticipant().getParticipant();
        Assertions.assertTrue(account.sameParticipant(expectedParticipant, expectedAccountNumber));
    }

    @Test
    public void givenNotExistingAccountNumberAndParticipant_whenCallingSameParticipant_shouldReturnFalse() {
        var entryKey = EntryKeyMock.getEntryKeyCnpj();
        var account = AccountMock.getRandomAccountWithSpecificEntryKey(entryKey);
        var expectedAccountNumber = RandomAccountMock.randomAccountNumber();
        var expectedParticipant = RandomParticipant.getParticipant();
        Assertions.assertFalse(account.sameParticipant(expectedParticipant, expectedAccountNumber));
    }

    @Test
    public void givenValidValues_whenCallingGetInstanceWithId_shouldReturnInstanceWithSameId() {
        var requestIdExpected = UUID.randomUUID().toString();
        var entryKeys = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, requestIdExpected);
        var idExpected = UUID.randomUUID();
        var createdAtExpected = Instant.parse("2007-12-03T10:15:30.00Z");
        var updatedAtExpected = Instant.parse("2007-12-03T10:15:30.00Z");

        var oldAccount = Account.getInstance(idExpected, createdAtExpected, updatedAtExpected, null, "1234",
                RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, Instant.now(), List.of(entryKeys));
        Assertions.assertNotNull(oldAccount);
        Assertions.assertInstanceOf(Account.class, oldAccount);
        Assertions.assertEquals(idExpected, oldAccount.getId());
        Assertions.assertEquals(createdAtExpected, oldAccount.getCreatedAt());
        Assertions.assertNull(oldAccount.getDeletedAt());
        Assertions.assertEquals(entryKeys.getId().toString(), oldAccount.getEntryKeys().getFirst().getId().toString());
    }
}
