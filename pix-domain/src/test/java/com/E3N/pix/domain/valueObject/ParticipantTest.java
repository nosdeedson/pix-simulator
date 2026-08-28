package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.participant.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParticipantTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000",
            "60701190",
            "60746948",
            "90400888",
            "00360305",
            "00416968",
            "18236120",
            "JTNJMBG9",
            "22896431",
            "36864992",
    })
    public void whenReceiveValidParticipantValues_shouldReturnParticipant(final String participant) {
        var resul = Participant.getInstance(participant);
        Assertions.assertInstanceOf(Participant.class, resul);
        Assertions.assertNull(resul.getNotification());
        Assertions.assertEquals(resul.getParticipant(), participant);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000",
            "607190",
            "6074694",
            "9040088",
            "360305",
            "004168",
            "1823612",
            "795423",
            "JTNJMB",
    })
    @NullSource
    public void whenReceiveInvalidParticipantValues_shouldReturnParticipant(final String participant) {
        var resul = Participant.getInstance(participant);
        Assertions.assertInstanceOf(Participant.class, resul);
        Assertions.assertInstanceOf(Notification.class, resul.getNotification());
        String expectedMessage;
        if (participant == null) {
            expectedMessage = "Participant should not be null.";
        } else {
            expectedMessage = "Participant is invalid.";
        }
        Assertions.assertEquals(expectedMessage, resul.getNotification().getViolations().getFirst().message());

    }
}
