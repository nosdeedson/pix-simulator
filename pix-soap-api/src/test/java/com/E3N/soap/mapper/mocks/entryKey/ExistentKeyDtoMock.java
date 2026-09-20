package com.E3N.soap.mapper.mocks.entryKey;

import com.E3N.pix.application.ownership.ExistentKeysDto;
import com.E3N.test.Owner.RandomKeysMock;

import java.util.ArrayList;
import java.util.List;

public abstract class ExistentKeyDtoMock {

    public static List<ExistentKeysDto> getExistentDto(boolean hasEntry){
        List<ExistentKeysDto> dtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dtoList.add(new ExistentKeysDto(hasEntry, RandomKeysMock.randomEVP()));
        }
        return dtoList;
    }
}
