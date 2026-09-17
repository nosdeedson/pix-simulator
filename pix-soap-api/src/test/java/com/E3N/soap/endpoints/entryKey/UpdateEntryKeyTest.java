package com.E3N.soap.endpoints.entryKey;

import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.ReasonType;
import com.E3N.pix.soap.contract.UpdateEntryKeyResponse;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.EntryKeyRequestMock;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UpdateEntryKeyTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    private EntryKey entryKey;

    @BeforeEach
    void setUp() {
        entryKey = new EntryKey(ownerRepository);
    }

    @Test
    void shouldBeInstantiated() {
        Assertions.assertNotNull(ownerRepository);
        Assertions.assertNotNull(entryKey);
    }

    @Test
    void givenKeyDifferentFromBodyKey_whenCalling_updateEntryKey_shouldThrowSoapFaultException() {
        var request = EntryKeyRequestMock.createUpdateRequest(ReasonType.USER_REQUESTED);
        var differentKey = "does-not-matter";
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.updateEntryKey(differentKey, request));
    }

    @Test
    void givenNoExistentKey_whenCalling_updateEntryKey_shouldThrowSoapFaultException() {
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.anyString(), Mockito.any()))
                .thenReturn(Optional.empty());
        var request = EntryKeyRequestMock.createUpdateRequest(ReasonType.USER_REQUESTED);
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.updateEntryKey(request.getKey(), request));
    }

    @Test
    void givenParticipantDifferentFromOwner_whenCalling_updateEntryKey_shouldThrowSoapFaultException() {
        var owner = OwnerMock.createOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.anyString(), Mockito.any()))
                .thenReturn(Optional.of(owner));
        var request = EntryKeyRequestMock.createUpdateRequest(ReasonType.USER_REQUESTED);
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.updateEntryKey(request.getKey(), request));
    }

    @ParameterizedTest
    @MethodSource("provideValidValues")
    void givenValidRequest_whenCalling_updateEntryKey_shouldReturnUpdateEntryKeyResponse(Owner owner, Reason reason) {
        var request = EntryKeyRequestMock.from(reason, owner);
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.anyString(), Mockito.any()))
                .thenReturn(Optional.of(owner));
        Mockito.when(ownerRepository.update(Mockito.any()))
                .thenReturn(owner);
        UpdateEntryKeyResponse result = entryKey.updateEntryKey(request.getKey(), request);
        Assertions.assertInstanceOf(UpdateEntryKeyResponse.class, result);
    }

    static List<Arguments> provideValidValues() {
        return List.of(
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.RFB_VALIDATION, TypeKey.EMAIL), Reason.RFB_VALIDATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.RECONCILIATION, TypeKey.PHONE), Reason.RECONCILIATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.USER_REQUESTED, TypeKey.CNPJ), Reason.USER_REQUESTED),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.BRANCH_TRANSFER, TypeKey.EMAIL), Reason.BRANCH_TRANSFER),

                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.RFB_VALIDATION, TypeKey.EVP), Reason.RFB_VALIDATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.RECONCILIATION, TypeKey.EVP), Reason.RECONCILIATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.BRANCH_TRANSFER, TypeKey.EVP), Reason.BRANCH_TRANSFER),

                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.RFB_VALIDATION, TypeKey.EMAIL), Reason.RFB_VALIDATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.RECONCILIATION, TypeKey.PHONE), Reason.RECONCILIATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.USER_REQUESTED, TypeKey.CNPJ), Reason.USER_REQUESTED),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.BRANCH_TRANSFER, TypeKey.EMAIL), Reason.BRANCH_TRANSFER),

                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.RFB_VALIDATION, TypeKey.EVP), Reason.RFB_VALIDATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.RECONCILIATION, TypeKey.EVP), Reason.RECONCILIATION),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.BRANCH_TRANSFER, TypeKey.EVP), Reason.BRANCH_TRANSFER)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    void givenInvalidValues_whenCalling_updateEntryKey_shouldThrowSoapFaultException(Owner owner, Reason reason) {
        Mockito.when(ownerRepository.findByKeyAndTaxIdNumber(Mockito.anyString(), Mockito.any()))
                .thenReturn(Optional.of(owner));
        var request = EntryKeyRequestMock.from(reason, owner);
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.updateEntryKey(request.getKey(), request));
    }

    static List<Arguments> provideInvalidValues() {
        return List.of(
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.ACCOUNT_CLOSURE, TypeKey.EMAIL), Reason.ACCOUNT_CLOSURE),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.FRAUD, TypeKey.PHONE), Reason.FRAUD),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.PARTICIPANT_EXCLUSION, TypeKey.CNPJ), Reason.PARTICIPANT_EXCLUSION),

                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.ACCOUNT_CLOSURE, TypeKey.EVP), Reason.ACCOUNT_CLOSURE),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.FRAUD, TypeKey.EVP), Reason.FRAUD),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.PARTICIPANT_EXCLUSION, TypeKey.EVP), Reason.PARTICIPANT_EXCLUSION),
                Arguments.of(OwnerMock.createOwner(TypePerson.LEGAL_PERSON, Reason.USER_REQUESTED, TypeKey.EVP), Reason.USER_REQUESTED),

                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.ACCOUNT_CLOSURE, TypeKey.EMAIL), Reason.ACCOUNT_CLOSURE),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.FRAUD, TypeKey.PHONE), Reason.FRAUD),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.PARTICIPANT_EXCLUSION, TypeKey.CNPJ), Reason.PARTICIPANT_EXCLUSION),

                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.ACCOUNT_CLOSURE, TypeKey.EVP), Reason.ACCOUNT_CLOSURE),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.FRAUD, TypeKey.EVP), Reason.FRAUD),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.PARTICIPANT_EXCLUSION, TypeKey.EVP), Reason.PARTICIPANT_EXCLUSION),
                Arguments.of(OwnerMock.createOwner(TypePerson.NATURAL_PERSON, Reason.USER_REQUESTED, TypeKey.EVP), Reason.USER_REQUESTED)
        );
    }
}
