package com.E3N.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.ListClaim.ListClaimDto;
import com.E3N.pix.soap.mapper.claim.ListClaimRequestToDtoMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mocks.claim.ListClaimRequestMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListClaimRequestDtoMapperTest extends UnitTest {

    @Test
    void givenValidRequestWithAllAttributes_whenCallingMapper_shouldReturnDtoWithTheBooleanTrue(){
        var participant = RandomParticipant.getParticipant();
        var request = ListClaimRequestMock.getListClaimRequest(Boolean.TRUE, participant);
        var result = ListClaimRequestToDtoMapper.from(request);
        Assertions.assertInstanceOf(ListClaimDto.class, result);
        Assertions.assertEquals(participant, request.getParticipant());
        Assertions.assertTrue(result.isClaimer());
        Assertions.assertTrue(result.isDonor());
        Assertions.assertNotNull(result.statusClaim());
        Assertions.assertNotNull(result.typeClaim());
        Assertions.assertNotNull(result.modifiedAfter());
        Assertions.assertNotNull(result.modifiedBefore());
    }

    @Test
    void givenValidRequestWithAllAttributes_whenCallingMapper_shouldReturnDtoWithAllAttributesWithTheBooleanFalse(){
        var participant = RandomParticipant.getParticipant();
        var request = ListClaimRequestMock.getListClaimRequest(Boolean.FALSE, participant);
        var result = ListClaimRequestToDtoMapper.from(request);
        Assertions.assertInstanceOf(ListClaimDto.class, result);
        Assertions.assertEquals(participant, request.getParticipant());
        Assertions.assertFalse(result.isClaimer());
        Assertions.assertFalse(result.isDonor());
        Assertions.assertNotNull(result.statusClaim());
        Assertions.assertNotNull(result.typeClaim());
        Assertions.assertNotNull(result.modifiedAfter());
        Assertions.assertNotNull(result.modifiedBefore());
    }

    @Test
    void givenValidRequestWithAllAttributes_whenCallingMapper_shouldReturnDtoWithAllOptionalAttributesNull(){
        var participant = RandomParticipant.getParticipant();
        var request = ListClaimRequestMock.getListClaimRequestWithAllNull(participant);
        var result = ListClaimRequestToDtoMapper.from(request);
        Assertions.assertInstanceOf(ListClaimDto.class, result);
        Assertions.assertEquals(participant, request.getParticipant());
        Assertions.assertNull(result.isClaimer());
        Assertions.assertNull(result.isDonor());
        Assertions.assertNull(result.statusClaim());
        Assertions.assertNull(result.typeClaim());
        Assertions.assertNull(result.modifiedAfter());
        Assertions.assertNull(result.modifiedBefore());
    }
}
