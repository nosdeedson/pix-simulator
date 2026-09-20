package com.E3N.soap.mapper.entryKey;

import com.E3N.pix.application.ownership.ExistentKeysDto;
import com.E3N.pix.soap.contract.CheckKeysResponse;
import com.E3N.pix.soap.mapper.entryKey.CheckEntryKeysResponseMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.ExistentKeyDtoMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ToCheckKeyResponseTest extends UnitTest {

    @Test
    void givenListOfDtoAllHasEntryFalse_whenCalling_getCheckKeysResponse_shouldReturnIt(){
        var listKeys = ExistentKeyDtoMock.getExistentDto(false);

        var result = CheckEntryKeysResponseMapper.getCheckKeysResponse(listKeys);
        Assertions.assertInstanceOf(CheckKeysResponse.class, result);
        Assertions.assertEquals(3, result.getKeys().getKey().size());
        result.getKeys().getKey().forEach(it -> {
            Assertions.assertFalse(it.isHasEntry());
            Assertions.assertTrue(listKeys.stream().map(ExistentKeysDto::key).toList().contains(it.getValue()));
        });
    }

    @Test
    void givenListOfDtoAllHasEntryTrue_whenCalling_getCheckKeysResponse_shouldReturnIt(){
        var listKeys = ExistentKeyDtoMock.getExistentDto(true);

        var result = CheckEntryKeysResponseMapper.getCheckKeysResponse(listKeys);
        Assertions.assertInstanceOf(CheckKeysResponse.class, result);
        Assertions.assertEquals(3, result.getKeys().getKey().size());
        result.getKeys().getKey().forEach(it -> {
            Assertions.assertTrue(it.isHasEntry());
            Assertions.assertTrue(listKeys.stream().map(ExistentKeysDto::key).toList().contains(it.getValue()));
        });
    }

    @Test
    void givenEmptyList_whenCalling_getCheckKeysResponse_shouldReturnIt(){
        List<ExistentKeysDto> list = new ArrayList<>();

        var result = CheckEntryKeysResponseMapper.getCheckKeysResponse(list);
        Assertions.assertInstanceOf(CheckKeysResponse.class, result);
        Assertions.assertTrue(result.getKeys().getKey().isEmpty());
    }
}
