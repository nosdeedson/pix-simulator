package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomValidName;

public abstract class OwnerMock {

    public static Owner getOwner(TypePerson typePerson, String taxIdNumber) {
        String tradeName = null;
        String name = RandomValidName.randomValidName();
        taxIdNumber = taxIdNumber != null ? taxIdNumber : RandomCpfMock.getRandomCFP();
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            taxIdNumber = RandomCNPJMock.getRandomCNPJ();
           name = RandomValidName.randomValidCompanyName();
           tradeName = name;
        }
        return Owner.getInstance(
                name,
                tradeName,
                taxIdNumber,
                typePerson,
                AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyEVP())
        );
    }

    public static Owner getOwnerWithMaxKeys(TypePerson typePerson, int qtdKeys){
        Owner owner = getOwner(typePerson, null);
        if (TypePerson.LEGAL_PERSON.equals(typePerson)){
            for (int i = 0; i < qtdKeys; i++) {
                owner.addNewAccountOrNewKey(AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyEVP()));
            }
        } else {
            for (int i = 0; i < qtdKeys; i++) {
                owner.addNewAccountOrNewKey(AccountMock.getRandomAccountWithSpecificEntryKey(EntryKeyMock.getEntryKeyEVP()));
            }
        }
        return owner;
    }
}
