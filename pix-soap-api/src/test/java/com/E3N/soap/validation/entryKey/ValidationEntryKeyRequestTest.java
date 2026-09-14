package com.E3N.soap.validation.entryKey;

import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.validation.entryKey.ValidationEntryKeyRequest;
import com.E3N.soap.UnitTest;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ValidationEntryKeyRequestTest extends UnitTest {

    @Test
    void givenNoNullValues_shouldNotThrowException() {
        Assertions.assertDoesNotThrow(() -> ValidationEntryKeyRequest.validateHeaderGetKey(
                RandomParticipant.getParticipant(), RandomCpfMock.getRandomCFP(), "endToEndId")
        );
    }

    @ParameterizedTest()
    @CsvSource(
            nullValues = "NULL",
            value = {
                    "NULL, doesNotMatter, doesNotMatter",
                    "doesNotMatter, NULL, doesNorMatter",
                    "doesNotMatter, doesNorMatter, NULL",
                    "NULL, NULL, NULL"
            })
    void givenNullValues_shouldThrowException(String piRequestingParticipant, String piPayerId, String piEndToEndId) {
        Assertions.assertThrows(SoapFaultException.class, () -> ValidationEntryKeyRequest.validateHeaderGetKey(
                piRequestingParticipant, piPayerId, piEndToEndId
        ));
    }

    @ParameterizedTest()
    @CsvSource(
            nullValues = "NULL",
            delimiter = '|',
            value = {
                    "NULL | doesNotMatter | doesNotMatter",
                    "doesNotMatter | NULL | doesNorMatter",
                    "doesNotMatter | doesNorMatter | NULL ",
                    "NULL | NULL | NULL "
            })
    void givenNullValues_shouldThrowExceptionAgain(final String piRequestingParticipant, String piPayerId, String piEndToEndId) {
        Assertions.assertThrows(SoapFaultException.class, () -> ValidationEntryKeyRequest.validateHeaderGetKey(
                piRequestingParticipant, piPayerId, piEndToEndId
        ));
    }
}
