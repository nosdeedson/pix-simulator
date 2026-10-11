package com.E3N.soap.mapper.mocks.claim;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomParticipant;
import com.E3N.test.Owner.RandomValidName;

import java.time.Instant;
import java.util.UUID;

public abstract class ClaimRequestMock {

    private static AccountType accountTypeMock(){
        var acc = new AccountType();
        acc.setAccountNumber(RandomAccountMock.randomAccountNumber());
        acc.setAccountType(AccountTypeEnum.CACC);
        acc.setBranch(RandomAccountMock.randomBranch());
        acc.setParticipant(RandomParticipant.getParticipant());
        acc.setOpeningDate(DateHelper.fromInstant(Instant.now()));
        return acc;
    }

    private static OwnerType ownerTypeMock(){
        var owner = new OwnerType();
        owner.setName(RandomValidName.randomValidName());
        owner.setTaxIdNumber(RandomCpfMock.getRandomCFP());
        owner.setType(OwnerTypeEnum.NATURAL_PERSON);
        return owner;
    }

    private static ClaimType claimTypeMock(String key, KeyType keyType, TypeClaims typeClaims){
        var claimType = new ClaimType();
        claimType.setClaimerAccount(accountTypeMock());
        claimType.setClaimer(ownerTypeMock());
        claimType.setKey(key);
        claimType.setKeyType(keyType);
        claimType.setType(typeClaims);
        return claimType;
    }

    public static CreateClaimRequest createClaimRequestMock(
            String key, KeyType keyType, TypeClaims typeClaims
    ){
        var request = new CreateClaimRequest();
        request.setClaim(claimTypeMock(key, keyType, typeClaims));
        request.setSignature(UUID.randomUUID().toString());
        return request;
    }

//    FROM OWNER
    private static AccountType from(Account account){
        var acc = new AccountType();
        acc.setAccountNumber(account.getNumber().getNumber());
        acc.setAccountType(AccountTypeEnum.fromValue(account.getType().name()));
        acc.setBranch(account.getBranch().getBranch());
        acc.setParticipant(account.getParticipant().getParticipant());
        acc.setOpeningDate(DateHelper.fromInstant(account.getOpeningDate()));
        return acc;
    }

    private static OwnerType getOwnerType(Owner owner){
        var ownerType = new OwnerType();
        ownerType.setName(owner.getName().getName());
        ownerType.setTaxIdNumber(owner.getTaxIdNumber().getTaxIdNumber());
        ownerType.setType(OwnerTypeEnum.fromValue(owner.getType().name()));
        return ownerType;
    }

    private static ClaimType getClaimType(Owner owner, TypeClaims typeClaims, String key, TypeKey typeKey){
        var claimType = new ClaimType();
        claimType.setClaimerAccount(from(owner.getAccounts().getFirst()));
        claimType.setClaimer(getOwnerType(owner));
        claimType.setKey(key);
        claimType.setKeyType(KeyType.valueOf(typeKey.name()));
        claimType.setType(typeClaims);
        return claimType;
    }

    public static CreateClaimRequest from(Owner owner, TypeClaims typeClaims, String key, TypeKey typeKey){
        var request = new CreateClaimRequest();
        request.setClaim(ClaimRequestMock.getClaimType(owner, typeClaims, key, typeKey));
        request.setSignature(UUID.randomUUID().toString());
        return request;
    }
}
