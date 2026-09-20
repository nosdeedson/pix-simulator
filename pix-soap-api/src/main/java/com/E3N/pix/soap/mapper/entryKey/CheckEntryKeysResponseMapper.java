package com.E3N.pix.soap.mapper.entryKey;

import com.E3N.pix.application.ownership.ExistentKeysDto;
import com.E3N.pix.soap.contract.CheckKeysRequest;
import com.E3N.pix.soap.contract.CheckKeysResponse;
import com.E3N.pix.soap.contract.ExistKeyType;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public abstract class CheckEntryKeysResponseMapper {

    public static CheckKeysResponse getCheckKeysResponse(List<ExistentKeysDto> keys){
        var response = new CheckKeysResponse();
        response.setKeys(setKeys(keys));
        response.setResponseTime(DateHelper.fromInstant(Instant.now()));
        response.setSignature(UUID.randomUUID().toString());
        response.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        return response;
    }

    private static ExistKeyType setKeys(List<ExistentKeysDto> keys){
        var keysType = new ExistKeyType();
        for (ExistentKeysDto dto : keys){
            ExistKeyType.Key k = new ExistKeyType.Key();
            k.setHasEntry(dto.hasEntry());
            k.setValue(dto.key());
            keysType.getKey().add(k);
        }
        return keysType;
    }

}
