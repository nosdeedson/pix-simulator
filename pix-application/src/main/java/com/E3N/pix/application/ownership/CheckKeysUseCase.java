package com.E3N.pix.application.ownership;

import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;

import java.util.ArrayList;
import java.util.List;

public class CheckKeysUseCase {

    private final OwnerRepositoryInterface ownerRepository;

    public CheckKeysUseCase(OwnerRepositoryInterface ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<ExistentKeysDto> execute(List<String> keys){
        var foundKeys = this.ownerRepository.findByKeys(keys);
        var existentKeys = new ArrayList<ExistentKeysDto>(keys.size());
        for (String k : keys){
            if (foundKeys.contains(k)){
                existentKeys.add(new ExistentKeysDto(true, k));
            }else {
                existentKeys.add(new ExistentKeysDto(false, k));
            }
        }
        return existentKeys;
    }
}
