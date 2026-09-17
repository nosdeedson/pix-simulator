package com.E3N.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.service.ownership.dto.OwnerDto;
import com.E3N.pix.soap.contract.ReasonType;
import com.E3N.pix.soap.mapper.entryKey.CreateEntryKeyRequestToDtoMapper;
import com.E3N.pix.soap.mapper.entryKey.UpdateEntryKeyRequestToDtoMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.EntryKeyRequestMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntryKeyRequestToDtoMapperTest extends UnitTest {

    @Test
    public void givenValidRequest_whenCalling_OwnerDtoMapper_from_shouldReturnOwnerDto() {
        var request = EntryKeyRequestMock.createRequest(false);
        var dto = CreateEntryKeyRequestToDtoMapper.from(request);
        Assertions.assertInstanceOf(OwnerDto.class, dto);
        Assertions.assertEquals(request.getEntry().getOwner().getTaxIdNumber(), dto.taxIdNumber());
    }

    @Test
    public void givenValidUpdateRequest_whenCalling_UpdateKeyDtoMapper_shouldReturnDto() {
        var request = EntryKeyRequestMock.createUpdateRequest(ReasonType.USER_REQUESTED);
        var dto = UpdateEntryKeyRequestToDtoMapper.getDto(request);
        Assertions.assertInstanceOf(UpdateEntryKeyDto.class, dto);
        Assertions.assertEquals(request.getKey(), dto.key());
    }
}
