package com.E3N.soap.mapper.claim;

import com.E3N.pix.soap.contract.ConfirmClaimResponse;
import com.E3N.pix.soap.mapper.claim.DtoToConfirmClaimResponseMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mocks.claim.ClaimOutputDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DtoToConfirmClaimResponseMapperTest extends UnitTest {

    @Test
    void givenValidOutput_whenCalling_getConfirmResponse_shouldReturnConfirmClaimResponse(){
        var dto = ClaimOutputDtoMock.mockClaimOutputDto();
        var result = DtoToConfirmClaimResponseMapper.getConfirmResponse(dto);
        Assertions.assertInstanceOf(ConfirmClaimResponse.class, result);
        Assertions.assertEquals(dto.id(), result.getClaim().getId());
    }
}
