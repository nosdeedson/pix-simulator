package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.branch.Branch;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

public class BranchTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "12",
            "123",
            "1234",
            "1111",
            "0001",
            "01",
            "1000",
            "1",
            "1",
            "1"
    })
    public void givenValidBranchValues_shouldReturnAnInstanceWithNotification(final String value) {
        var expectedBranch = StringUtils.leftPad(value, 4, "0");
        var result = Branch.getInstance(value);
        Assertions.assertInstanceOf(Branch.class, result);
        Assertions.assertEquals(expectedBranch, result.getBranch());
        Assertions.assertNull(result.getNotification());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "123s",
            "111122",
            "ag-1",
            "1111-4",
            "0000",
            "1s",
            "1s",
            "x1",
            "00001"
    })
    @NullSource
    public void givenInvalidBranchValues_shouldReturnAnInstance(final String value) {
        var result = Branch.getInstance(value);
        Assertions.assertInstanceOf(Branch.class, result);
        Assertions.assertNull(result.getBranch());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(Arrays.asList("Branch is required.", "Branch is invalid.").contains(result.getNotification().getViolations().getFirst().message()));
    }
}
