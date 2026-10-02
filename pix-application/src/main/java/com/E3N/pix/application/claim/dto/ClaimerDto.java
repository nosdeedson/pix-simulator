package com.E3N.pix.application.claim.dto;

import com.E3N.pix.domain.shared.TypePerson;

public record ClaimerDto(
        TypePerson typePerson,
        String taxIdNumber,
        String name,
        String tradeName
) {
}
