package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.claim.mocks.ClaimDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ClaimServiceTest extends UniTest {

    @InjectMocks
    private ClaimService claimService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var dto = ClaimDtoMock.getDto(TypePerson.LEGAL_PERSON);
        var result = claimService.create(dto);
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertFalse(result.getNotification().hasError());
    }

    @Test
    void givenValidValuesForNaturalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var dto = ClaimDtoMock.getDto(TypePerson.NATURAL_PERSON);
        var result = claimService.create(dto);
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertFalse(result.getNotification().hasError());
    }

    @Test
    void givenInvalidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var dto = ClaimDtoMock.getInvalidDto(TypePerson.LEGAL_PERSON);
        var result = claimService.create(dto);
        var expectedErrorsMessage = List.of(
                "Claim must be opened as OWNERSHIP",
                "TaxIdNumber is invalid.",
                "This kind of Key must open a portability"
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertTrue(expectedErrorsMessage.containsAll(result.getNotification().getViolations().stream().map(Violation::reason).toList()));
        Assertions.assertEquals(3, result.getNotification().getViolations().size());
    }

    @Test
    void givenInvalidValuesForNaturalPerson_whenCalling_getInstance_shouldReturnClaim() {
        var dto = ClaimDtoMock.getInvalidDto(TypePerson.NATURAL_PERSON);
        var result = claimService.create(dto);
        var expectedErrorsMessage = List.of(
                "Claim must be opened as OWNERSHIP",
                "TaxIdNumber is invalid.",
                "This kind of Key must open a portability"
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertTrue(expectedErrorsMessage.containsAll(result.getNotification().getViolations().stream().map(Violation::reason).toList()));
        Assertions.assertEquals(3, result.getNotification().getViolations().size());
    }
}
