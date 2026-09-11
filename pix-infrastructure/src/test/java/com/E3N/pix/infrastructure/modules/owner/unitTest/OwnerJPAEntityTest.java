package com.E3N.pix.infrastructure.modules.owner.unitTest;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.infrastructure.UnitTest;
import com.E3N.pix.infrastructure.modules.owner.OwnerJPAEntity;
import com.E3N.test.Owner.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class OwnerJPAEntityTest extends UnitTest {

    @Test
    public void givenValidValuesForNaturalPerson_whenCallingGetInstanceWithId_shouldReturnSameId() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, "09/08/2026", key);
        var owner = Owner.getInstance(RandomValidName.randomValidName(), null, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
        var entity = OwnerJPAEntity.from(owner);
        Assertions.assertInstanceOf(OwnerJPAEntity.class, entity);
        Assertions.assertNotNull(entity.getId());
        Assertions.assertEquals(owner.getId().toString(), entity.getId());
    }

    @Test
    public void givenValidValuesForLegalPerson_whenCallingGetInstanceWithId_shouldReturnSameId() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, "09/08/2026", key);
        var expectedName = RandomValidName.randomValidCompanyName();
        var owner = Owner.getInstance(expectedName, expectedName, RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account);
        var entity = OwnerJPAEntity.from(owner);
        Assertions.assertInstanceOf(OwnerJPAEntity.class, entity);
        Assertions.assertNotNull(entity.getId());
        Assertions.assertEquals(owner.getId().toString(), entity.getId());
    }

    @Test
    public void givenValidOwnerJPAAsNaturalPerson_whenCallingGetInstanceWithId_shouldReturnOwner() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, "09/08/2026", key);
        var expectedOwner = Owner.getInstance(RandomValidName.randomValidName(), null, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
        var entity = OwnerJPAEntity.from(expectedOwner);
        var owner = OwnerJPAEntity.from(entity);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(owner.getId().toString(), entity.getId());
        Assertions.assertEquals(entity.getAccounts().getFirst().getId(), owner.getAccounts().getFirst().getId().toString());
    }

    @Test
    public void givenValidOwnerJPAAsLegalPerson_whenCallingGetInstanceWithId_shouldReturnSameId() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, "09/08/2026", key);
        var expectedName = RandomValidName.randomValidCompanyName();
        var expectedOwner = Owner.getInstance(expectedName, expectedName, RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account);
        var entity = OwnerJPAEntity.from(expectedOwner);
        var owner = OwnerJPAEntity.from(entity);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(owner.getId().toString(), entity.getId());
        Assertions.assertEquals(entity.getAccounts().getFirst().getId(), owner.getAccounts().getFirst().getId().toString());
    }
}
