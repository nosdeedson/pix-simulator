package com.E3N.soap.mapper.entryKey;

import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.pix.soap.mapper.entryKey.OwnerDtoMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.CreateEntryKeyRequestMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OwnerDtoMapperTest extends UnitTest {

    @Test
    public void givenValidRequest_whenCalling_OwnerDtoMapper_from_shouldReturnOwnerDto() {
        var request = CreateEntryKeyRequestMock.createRequest(false);
        var dto = OwnerDtoMapper.from(request);
        Assertions.assertInstanceOf(OwnerDto.class, dto);
        Assertions.assertEquals(request.getEntry().getOwner().getTaxIdNumber(), dto.taxIdNumber());
    }
}
