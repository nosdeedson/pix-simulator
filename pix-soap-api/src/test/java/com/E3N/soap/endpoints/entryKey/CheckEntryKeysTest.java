package com.E3N.soap.endpoints.entryKey;

import com.E3N.pix.application.ownership.ExistentKeysDto;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.CheckKeysResponse;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.EntryKeyRequestMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CheckEntryKeysTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    private EntryKey entryKey;

    @BeforeEach
    void setUp(){
        entryKey = new EntryKey(ownerRepository);
    }

    @Test
    void givenNoExistentKeys_whenCalling_checkKeysExistence_shouldReturnEmptyResponse(){
        var expectedListRequest = List.of(RandomKeysMock.randomPhone(), RandomKeysMock.randomEmails());
        List<String> expectedListFromBD = new ArrayList<>(0);
        Mockito.when(ownerRepository.findByKeys(Mockito.anyList()))
                .thenReturn(expectedListFromBD);
        var request = EntryKeyRequestMock.getCheckKeysRequest(expectedListRequest);
        var result = entryKey.checkKeysExistence(request);
        Assertions.assertInstanceOf(CheckKeysResponse.class, result);
        result.getKeys().getKey().forEach(it -> {
            Assertions.assertFalse(it.isHasEntry());
            Assertions.assertTrue(expectedListRequest.contains(it.getValue()));
        });
    }

    @Test
    void givenExistentKeys_whenCalling_checkKeysExistence_shouldReturnEmptyResponse(){
        var expectedList = List.of(RandomKeysMock.randomPhone(), RandomKeysMock.randomEmails());
        Mockito.when(ownerRepository.findByKeys(Mockito.anyList()))
                .thenReturn(expectedList);
        var request = EntryKeyRequestMock.getCheckKeysRequest(expectedList);
        var result = entryKey.checkKeysExistence(request);
        Assertions.assertInstanceOf(CheckKeysResponse.class, result);
        Assertions.assertFalse(result.getKeys().getKey().isEmpty());
        Assertions.assertTrue(expectedList.containsAll(request.getKeys().getKey()));
        result.getKeys().getKey().forEach(it -> {
            Assertions.assertTrue(it.isHasEntry());
            Assertions.assertTrue(expectedList.contains(it.getValue()));
        });
    }
}
