package com.E3N.soap.endpoints.entryKey;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.infrastructure.modules.owner.OwnerRepositoryImpl;
import com.E3N.pix.soap.contract.GetEntryKeyResponse;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.entryKey.OwnerMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetEntryKeyTest extends UnitTest {

    @Mock
    private OwnerRepositoryImpl ownerRepository;

    private EntryKey entryKey;

    @BeforeEach
    void setUp() {
        entryKey = new EntryKey(ownerRepository);
    }

    @Test
    void shouldBeInstantiated() {
        Assertions.assertNotNull(entryKey);
        Assertions.assertNotNull(ownerRepository);
    }

    @Test
    void givenRequestWithoutHeaders_whenCallingGetEntryKey_shouldReturnNotification() {
        Assertions.assertThrows(SoapFaultException.class, () ->
                entryKey.getEntryKey("doesNotMatter", false, null, null, null)
        );
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                    "false | 00000000 | 49051557205 | E00000000202609141200abc1234567x | NATURAL_PERSON",
                    "false | 00000208 | 05217333839 | E12345678202609141530xyz98765432 | LEGAL_PERSON",
                    "false | 00556603 | 47419604630 | E99999999202601010000test0000E2E | NATURAL_PERSON",
                    "false | 01073966 | 04542909930 | E09089356202605201145APIcdbe38b4 | LEGAL_PERSON",
                    "false | 01658426 | 12367109249 | E710278662026083023591234567891P | NATURAL_PERSON",
            }
    )
    void givenValidValues_whenCallingGetEntryWithValidKey_shouldReturnGetEntryKeyResponse(
            Boolean statistics, String piRequestingParticipant,
            String piPayerId, String piE2E, String typePerson
    ) {
        TypePerson type = TypePerson.valueOf(typePerson);
        var owner = OwnerMock.createOwner(type);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        Mockito.when(ownerRepository.findByKey(expectedKey))
                .thenReturn(Optional.of(owner));
        GetEntryKeyResponse result = entryKey.getEntryKey(expectedKey, statistics, piRequestingParticipant, piPayerId, piE2E);
        Assertions.assertInstanceOf(GetEntryKeyResponse.class, result);
        Assertions.assertEquals(expectedKey, result.getEntry().getKey());
        Assertions.assertNotNull(result.getCorrelationId());
        Assertions.assertNotNull(result.getResponseTime());
        Assertions.assertNotNull(result.getSignature());
        Assertions.assertEquals(type.name(), result.getEntry().getOwner().getType().name());
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                    "noExist | false | 00000000 | 49051557205 | E00000000202609141200abc1234567x",
                    "noExist | false | 00000208 | 05217333839 | E12345678202609141530xyz98765432",
                    "noExist | false | 00556603 | 47419604630 | E99999999202601010000test0000E2E",
                    "noExist | false | 01073966 | 04542909930 | E09089356202605201145APIcdbe38b4",
                    "noExist | false | 01658426 | 12367109249 | E710278662026083023591234567891P",
            }
    )
    void givenValidValues_whenCallingGetEntryWithValidKey_shouldThrowException(
            String key, Boolean statistics, String piRequestingParticipant,
            String piPayerId, String piE2E
    ) {
        Mockito.when(ownerRepository.findByKey(key))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(SoapFaultException.class, () -> entryKey.getEntryKey(key, statistics, piRequestingParticipant, piPayerId, piE2E));
    }

    @ParameterizedTest
    @CsvSource(
            delimiter = '|',
            value = {
                    "true | 00000000 | 49051557205 | E00000000202609141200abc1234567x",
                    "true | 00000208 | 05217333839 | E12345678202609141530xyz98765432",
                    "true | 00556603 | 47419604630 | E99999999202601010000test0000E2E",
                    "true | 01073966 | 04542909930 | E09089356202605201145APIcdbe38b4",
                    "true | 01658426 | 12367109249 | E710278662026083023591234567891P",
            }
    )
    void givenValidValues_whenCallingGetEntryWithStatisticsTrue_shouldReturnNotification(
            Boolean statistics, String piRequestingParticipant,
            String piPayerId, String piE2E
    ) {
        var expectedKey = "doesNotMatter";
        Assertions.assertThrows(SoapFaultException.class, () ->
                entryKey.getEntryKey(expectedKey, statistics, piRequestingParticipant, piPayerId, piE2E));
    }


    @ParameterizedTest
    @CsvSource(
            nullValues = "NULL",
            delimiter = '|',
            value = {
                    "NULL | 00000000 | 49051557205 | E00000000202609141200abc1234567x",
                    "false | NULL | 05217333839 | E12345678202609141530xyz98765432",
                    "false | 00556603 | NULL | E99999999202601010000test0000E2E",
                    "false | 01073966 | 04542909930 | NULL",
                    "false | 01658426 | NULL | E710278662026083023591234567891P",
                    "NULL | NULL | NULL | NULL",
            }
    )
    void givenInvalidValues_whenCallingGetEntryWithValidKey_shouldReturnNotification(
            Boolean statistics, String piRequestingParticipant,
            String piPayerId, String piE2E
    ) {
        var expectedKey = "doesNotMatter";
        Assertions.assertThrows(SoapFaultException.class, () ->
                entryKey.getEntryKey(expectedKey, statistics, piRequestingParticipant, piPayerId, piE2E));
    }
}
