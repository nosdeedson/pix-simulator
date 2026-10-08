package com.E3N.soap.mapper.claim;

import com.E3N.pix.soap.contract.CreateClaimResponse;
import com.E3N.pix.soap.mapper.claim.DtoToClaimResponseMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.claim.ClaimOutputDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DtoToClaimResponseTest extends UnitTest {

    @Test
    void givenDto_whenCalling_toResponse_shouldReturnClaimResponse() {
        var dto = ClaimOutputDtoMock.mockClaimOutputDto();
        var result = DtoToClaimResponseMapper.toResponse(dto);
        Assertions.assertInstanceOf(CreateClaimResponse.class, result);
        Assertions.assertEquals(dto.claimerDto().taxIdNumber(), result.getClaim().getClaimer().getTaxIdNumber());
    }
}
