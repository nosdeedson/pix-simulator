package com.E3N.soap.endpoints.entryKey;

import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.service.Either;
import com.E3N.pix.soap.contract.DeleteEntryKeyResponse;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.EntryKeyRequestMock;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.MalformedObjectNameException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DeleteEntryKeyTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    private EntryKey entryKey;

    @BeforeEach
    void setUp(){
        entryKey = new EntryKey(ownerRepository);
    }

    @Test
    void shouldBeInstantiated(){
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(entryKey);
    }

    @Test
    void givenDifferentKeyFromPathAndBody_whenCalling_deleteEntryKey_shouldThrowException(){
        var expectedPathKey = RandomKeysMock.randomEmails();
        var request = EntryKeyRequestMock.getDeleteRequest(null, null);
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.deleteEntryKey(expectedPathKey, request));

    }

    @Test
    void givenValidRequestWithNoExistentKey_whenCalling_deleteEntryKey_shouldThrowException(){
        var request = EntryKeyRequestMock.getDeleteRequest(null, null);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.deleteEntryKey(request.getKey(), request));
    }

    @Test
    void givenValidRequest_whenCalling_deleteEntryKey_shouldReturnDeleteResponse(){
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedParticipant = owner.getAccounts().getFirst().getParticipant().getParticipant();
        var request = EntryKeyRequestMock.getDeleteRequest(expectedKey, expectedParticipant);
        Mockito.when(ownerRepository.findByKeyAndParticipant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(owner));
        var response = entryKey.deleteEntryKey(expectedKey, request);
        Assertions.assertInstanceOf(DeleteEntryKeyResponse.class, response);
        Assertions.assertEquals(expectedKey, response.getKey());
        Assertions.assertNotNull(response.getCorrelationId());
        Assertions.assertNotNull(response.getResponseTime());

    }
}
