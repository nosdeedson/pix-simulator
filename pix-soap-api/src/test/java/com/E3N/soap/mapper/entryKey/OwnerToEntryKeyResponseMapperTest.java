package com.E3N.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.soap.contract.CreateEntryKeyResponse;
import com.E3N.pix.soap.contract.GetEntryKeyResponse;
import com.E3N.pix.soap.contract.UpdateEntryKeyResponse;
import com.E3N.pix.soap.mapper.entryKey.OwnerToEntryKeyResponseMapper;
import com.E3N.shared.utils.DateHelper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OwnerToEntryKeyResponseMapperTest extends UnitTest {

    @Test
    public void givenValidOwnerNaturalPerson_whenCalling_EntryKeyResponseMapper_from_shouldReturnEntryKeyResponse() {
        var owner = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var response = OwnerToEntryKeyResponseMapper.from(owner);
        Assertions.assertInstanceOf(CreateEntryKeyResponse.class, response);
        Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), response.getEntry().getOwner().getTaxIdNumber());
        Assertions.assertEquals(DateHelper.fromInstant(owner.getAccounts().getLast().getEntryKeys().getLast().getResponseTime()), response.getResponseTime());
    }

    @Test
    public void givenValidOwnerLegalPerson_whenCalling_EntryKeyResponseMapper_from_shouldReturnEntryKeyResponse() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var response = OwnerToEntryKeyResponseMapper.from(owner);
        Assertions.assertInstanceOf(CreateEntryKeyResponse.class, response);
        Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), response.getEntry().getOwner().getTaxIdNumber());
        Assertions.assertEquals(DateHelper.fromInstant(owner.getAccounts().getLast().getEntryKeys().getLast().getResponseTime()), response.getResponseTime());
    }

    @Test
    void givenValidOwner_whenCalling_getEntryKeyResponse_shouldReturnGeTEntryKeyResponse() {
        var owner = OwnerMock.createOwner(TypePerson.NATURAL_PERSON);
        var response = OwnerToEntryKeyResponseMapper.getEntryKeyResponse(owner);
        Assertions.assertInstanceOf(GetEntryKeyResponse.class, response);
        Assertions.assertEquals(owner.getTaxIdNumber().getTaxIdNumber(), response.getEntry().getOwner().getTaxIdNumber());
        Assertions.assertNotNull(response.getCorrelationId());
        Assertions.assertNotNull(response.getResponseTime());
        Assertions.assertNotNull(response.getSignature());
    }

    @Test
    void givenValidOwner_whenCalling_getUpdateEntryKeyResponse_shouldReturnUpdateResponse() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        var response = OwnerToEntryKeyResponseMapper.getUpdateEntryKeyResponse(owner);
        Assertions.assertInstanceOf(UpdateEntryKeyResponse.class, response);
        Assertions.assertEquals(owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(), response.getEntry().getKey());
    }
}
