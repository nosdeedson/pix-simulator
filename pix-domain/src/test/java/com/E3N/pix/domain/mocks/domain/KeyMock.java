package com.E3N.pix.domain.mocks.domain;

import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.pix.domain.valueObject.key.TypeKey;

public abstract class KeyMock {
    public static Key builder(
            final String key,
            final TypeKey typeKey
    ) {
        return Key.getInstance(key, typeKey);
    }

    public static Key builder(){
        return Key.getInstance("TU2EDHAK000135", TypeKey.CNPJ);
    }
}
