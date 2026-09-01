package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.number.AccountNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

public class AccountNumberTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1234",
            "1234x",
            "12341234123412341234",
            "1234123412341234123X",
            "123455",
            "1234666",
            "1234333",
            "12343",
            "12344",
            "123466",
            "123477",
    })
    public void givenValidAccountNumbers_shouldReturnAnInstance(final String value) {
        var result = AccountNumber.getInstance(value);
        Assertions.assertInstanceOf(AccountNumber.class, result);
        Assertions.assertEquals(value, result.getNumber());
        Assertions.assertFalse(result.getNotification().hasError());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "1234a",
            "123412341234123412344",
            "123455xx",
            " ",
            "q1234333",
            "w12343q",
            "tttt",
            "xxx",
            "123x477",
    })
    public void givenInvalidAccountNumbers_shouldReturnAnInstanceWithNotification(final String value) {
        var result = AccountNumber.getInstance(value);
        Assertions.assertInstanceOf(AccountNumber.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(Arrays.asList("Account Number is required.", "Account Number is invalid.")
                .contains(result.getNotification().getViolations().getFirst().reason()));
    }
}
