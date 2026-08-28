package com.E3N.pix.domain.modules.entryKey;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.account.AccountType;
import com.E3N.pix.domain.validation.Notification;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

public class AccountTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidAttributes_shouldReturnAnAccount(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String opening
    ) {
        var expectedBranch = StringUtils.leftPad(branch, 4, "0");
        var result = Account.getInstance(expectedBranch,
                number,
                participant,
                type,
                opening
        );
        Assertions.assertInstanceOf(Account.class, result);
        Assertions.assertEquals(type, result.getType());
        Assertions.assertEquals(number, result.getNumber().getNumber());
        Assertions.assertEquals(expectedBranch, result.getBranch().getBranch());
        Assertions.assertInstanceOf(Instant.class, result.getOpeningDate());
        Assertions.assertEquals(participant, result.getParticipant().getParticipant());
    }

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenInvalidAttributes_shouldReturnAnAccountWithNotification(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String opening
    ) {
        var result = Account.getInstance(branch,
                number,
                participant,
                type,
                opening
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
                        "Participant is invalid."
                )
                .contains(result.getNotification().getViolations().getFirst().message()));
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("1", "1234", "00000000", AccountType.CACC, "12/08/1980"),
                Arguments.of("2", "1234", "04332281", AccountType.OTHR, "12/08/1980"),
                Arguments.of("01", "1234", "08357240", AccountType.SLRY, "12/08/1990"),
                Arguments.of("1", "12345678998765432112", "13673855", AccountType.SVGS, "01/02/2026"),
                Arguments.of("1", "1234", "18188384", AccountType.TRAN, "12/08/2022"),
                Arguments.of("0001", "1234X", "18236120", AccountType.TRAN, "12/08/1999"),
                Arguments.of("1000", "1234x", "18236120", AccountType.OTHR, "12/09/2023"),
                Arguments.of("1", "1234", "27351731", AccountType.SLRY, "12/11/1980"),
                Arguments.of("1", "1234", "30507541", AccountType.SVGS, "12/12/1980"),
                Arguments.of("1", "1234", "57HWH4JZ", AccountType.CACC, "12/10/1980")
        );
    }

    static Stream<Arguments> invalidProvider() {
        return Stream.of(
//                Arguments.of("100000", "1234", "00000000", AccountType.CACC, "12/08/1980"),
//                Arguments.of("2", "qqqq", "04332281", AccountType.OTHR, "32/08/1980"),
//                Arguments.of("01", "1234", "08357240", AccountType.SLRY, "12/13/1990"),
//                Arguments.of("1", "12345678998765432112a", "13673855", AccountType.SVGS, "01/02/2026"),
//                Arguments.of("1", "1234", "18188384", AccountType.TRAN, "12/08/202222"),
                Arguments.of("00014", "1234X", "18236120", AccountType.TRAN, "12/08/1999")
//                Arguments.of("1000", "1234xgg", "1823612022", AccountType.OTHR, "12/09/2023"),
//                Arguments.of("1", "1234", "27351731", null, "12/11/1980"),
//                Arguments.of("1", "1234", "3050754100", AccountType.SVGS, "12/12/1980"),
//                Arguments.of("1", "1234", null, AccountType.CACC, "12/10/1980")
        );
    }
}
