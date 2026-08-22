package com.E3N.pix.domain.valueObject.participant;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

import java.util.regex.Pattern;

public class ParticipantValidator extends Validator {
    private static final String PARTICIPANT_REGEX = "(?i)^[a-z0-9]{8}$";
    private static final Pattern pattern = Pattern.compile(PARTICIPANT_REGEX);

    private final Participant participant;

    public ParticipantValidator(Participant participant) {
        super(participant.getNotification());
        this.participant = participant;
    }

    @Override
    public ValidationHandler validate() {
        if (participant.getParticipant() == null) {
            validationHandler().append("Participant should not be null.");
            return participant.getNotification();
        }
        var inValid = !pattern.matcher(participant.getParticipant()).matches();
        if (inValid) validationHandler().append("Participant is invalid.");
        return participant.getNotification();
    }
}
