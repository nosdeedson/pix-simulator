package com.E3N.soap.endpoints;

import com.E3N.pix.infrastructure.modules.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.pix.soap.mapper.entryKey.OwnerDtoMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.CreateEntryKeyRequestMock;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


// how to test https://claude.ai/share/f77e47fd-e041-449d-acc4-b53f3902d01d

@ExtendWith(MockitoExtension.class)
public class EntryKeyTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    private EntryKey entryKey;

    @BeforeEach
    void setUp() {
        entryKey = new EntryKey(ownerRepository);
    }

    @Test
    public void shouldBeInstantiated() {
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(entryKey);
    }

    @Test
    public void givenValidRequest_whenCalling_createEntryKey_shouldReturnCreateEntryKeyResponse() {
        var request = CreateEntryKeyRequestMock.createRequest(false);
        var dto = OwnerDtoMapper.from(request);
        var owner = OwnerMock.createOwner(dto);
        when(ownerRepository.save(any()))
                .thenReturn(owner);
        var response = entryKey.createEntryKey(request);
        Assertions.assertInstanceOf(CreateEntryKeyResponse.class, response);
    }

    @Test
    public void givenInvalidRequest_whenCalling_createEntryKey_shouldReturnProblemType() {
        var request = CreateEntryKeyRequestMock.createRequest(true);
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.createEntryKey(request));
    }
}
