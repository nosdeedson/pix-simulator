package com.E3N.pix.infrastructure.modules.ownership.integrationTest;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.infrastructure.IntegrationTest;
import com.E3N.pix.infrastructure.modules.ownership.owner.OwnerRepositoryImpl;
import com.E3N.test.Owner.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OwnerRepositoryImpIT extends IntegrationTest {

    @Autowired
    private OwnerRepositoryImpl repository;

    private Owner getOwner(TypePerson typePerson) {
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
            var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
            return Owner.getInstance(RandomValidName.randomValidCompanyName(), RandomValidName.randomValidCompanyName(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account);
        }
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
        return Owner.getInstance(RandomValidName.randomValidName(), null, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }

    @Test
    void givenCorrectInjection_shouldRepositoryBeInstantiated() {
        Assertions.assertNotNull(repository);
    }

    @Test
    void givenValidTaxIdNumber_whenCalling_findByTaxIdNumber_shouldFindOwner() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedTaxIdNumber = owner.getTaxIdNumber();
        var ownerJpa = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, ownerJpa);
        var findOne = repository.findByTaxIdNumber(expectedTaxIdNumber.getTaxIdNumber());
        Assertions.assertInstanceOf(Optional.class, findOne);
        Assertions.assertInstanceOf(Owner.class, findOne.get());
        Assertions.assertEquals(expectedTaxIdNumber.getTaxIdNumber(), findOne.get().getTaxIdNumber().getTaxIdNumber());
    }

    @Test
    void givenValidKey_whenCalling_findByKey_shouldFindOwner() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedKeyToFind = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        owner = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(1, owner.getAccounts().getFirst().getEntryKeys().size());
        var newKey = EntryKey.getInstance(RandomKeysMock.randomEVP(), TypeKey.EVP, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        owner.getAccounts().getFirst().getEntryKeys().add(newKey);
        owner = repository.save(owner);

        var requestedOwner = repository.findByKey(expectedKeyToFind);
        Assertions.assertInstanceOf(Optional.class, requestedOwner);
        Assertions.assertInstanceOf(Owner.class, requestedOwner.get());
        Assertions.assertEquals(2, requestedOwner.get().getAccounts().getFirst().getEntryKeys().size());

    }

    @Test
    void givenInvalidKey_whenCalling_findByKey_shouldOptionalEmpty() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedKeyToFind = "does-not-exist";
        owner = repository.save(owner);

        var requestedOwner = repository.findByKey(expectedKeyToFind);
        Assertions.assertInstanceOf(Optional.class, requestedOwner);
        Assertions.assertTrue(requestedOwner.isEmpty());
    }

    @Test
    void givenValidOwner_whenCalling_save_shouldReturnNaturalPerson() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedTaxIdNumber = owner.getTaxIdNumber().getTaxIdNumber();
        var result = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        var validation = repository.findById(owner.getId());
        Assertions.assertInstanceOf(Optional.class, validation);
        Assertions.assertInstanceOf(Owner.class, validation.get());
        Assertions.assertEquals(expectedTaxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
    }

    @Test
    void givenValidOwner_whenCalling_save_shouldReturnLegalPerson() {
        var owner = getOwner(TypePerson.LEGAL_PERSON);
        var expectedTaxIdNumber = owner.getTaxIdNumber().getTaxIdNumber();
        var result = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        var validation = repository.findById(owner.getId());
        Assertions.assertInstanceOf(Optional.class, validation);
        Assertions.assertInstanceOf(Owner.class, validation.get());
        Assertions.assertEquals(expectedTaxIdNumber, result.getTaxIdNumber().getTaxIdNumber());
    }

    @Test
    void givenValidId_whenCalling_delete_shouldDelete() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedId = owner.getId();
        var result = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        repository.delete(expectedId);
        var shouldBeEmpty = repository.findById(expectedId);
        Assertions.assertInstanceOf(Optional.class, shouldBeEmpty);
        Assertions.assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    void shouldFindAll() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var result = repository.save(owner);
        var owner1 = getOwner(TypePerson.NATURAL_PERSON);
        var result1 = repository.save(owner1);
        Assertions.assertInstanceOf(Owner.class, result);
        Assertions.assertInstanceOf(Owner.class, result1);
        var results = repository.findAll();
        Assertions.assertInstanceOf(List.class, results);
        Assertions.assertEquals(2, results.size());
    }

    @Test
    void givenValidAccount_whenCalling_addingNewAccount_shouldReturnOwnerWithTwoAccounts() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var result = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var newAccount = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
        result.addNewAccountOrNewKey(newAccount);
        result = repository.update(result);
        Assertions.assertInstanceOf(Owner.class, result);
        var afterUpdate = repository.findById(result.getId());
        Assertions.assertEquals(2, afterUpdate.get().getAccounts().size());
    }

    @Test
    void givenValidKey_whenCalling_addingNewAccount_shouldJustAddNewKey() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var result = repository.save(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = owner.getAccounts().getLast();
        account.addKey(key);
        result = repository.update(owner);
        Assertions.assertInstanceOf(Owner.class, result);
        var afterUpdate = repository.findById(result.getId());
        Assertions.assertEquals(2, afterUpdate.get().getAccounts().getFirst().getEntryKeys().size());
    }

    @Test
    void givenValidValues_whenCalling_findByAccount_shouldReturnOwner() {
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        var expectedKey = owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var expectedTaxIdNumber = owner.getTaxIdNumber().getTaxIdNumber();
        repository.save(owner);
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var newAccount = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.SLRY, "2026-09-14 16:17:55", key);
        owner.addNewAccountOrNewKey(newAccount);
        var expectedOwner = repository.update(owner);
        Assertions.assertEquals(2, expectedOwner.getAccounts().size());

        var wantedOwner = repository.findByKeyAndTaxIdNumber(expectedKey, expectedTaxIdNumber);

        Assertions.assertInstanceOf(Owner.class, wantedOwner.get());
        Assertions.assertEquals(expectedKey, wantedOwner.get().getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(expectedOwner.getTaxIdNumber().getTaxIdNumber(), wantedOwner.get().getTaxIdNumber().getTaxIdNumber());
    }

    @Test
    void givenValidValues_whenCalling_findByAccount_shouldReturnEmpty() {
        var expectedKey = RandomKeysMock.randomEmails();
        var expectedTaxIdNumber = RandomCpfMock.getRandomCFP();
        var owner = getOwner(TypePerson.NATURAL_PERSON);
        repository.save(owner);
        var wantedOwner = repository.findByKeyAndTaxIdNumber(expectedKey, expectedTaxIdNumber);
        Assertions.assertInstanceOf(Optional.class, wantedOwner);
        Assertions.assertTrue(wantedOwner.isEmpty());
    }

}
