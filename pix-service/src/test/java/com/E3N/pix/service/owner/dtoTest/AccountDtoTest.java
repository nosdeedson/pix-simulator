package com.E3N.pix.service.owner.dtoTest;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.owner.dto.AccountDto;
import com.E3N.pix.service.owner.dto.EntryKeyDto;
import com.E3N.pix.service.owner.mocks.dto.EntryKeyDtoMock;
import com.E3N.pix.service.owner.mocks.dto.RandomAccountTypeMock;
import com.E3N.test.Owner.RandomAccountNumberMock;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

public class AccountDtoTest extends UniTest {
    private final static Random random = new Random();

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(
                        RandomAccountNumberMock.randomBranch(),
                        RandomAccountNumberMock.randomAccountNumber(),
                        RandomDateMock.getRandomDate(),
                        RandomParticipant.getParticipant(),
                        RandomAccountTypeMock.getAccountType(),
                        EntryKeyDtoMock.getEntryKeyDto(TypeKey.EMAIL)
                ),
                Arguments.of(
                        RandomAccountNumberMock.randomBranch(),
                        RandomAccountNumberMock.randomAccountNumber(),
                        RandomDateMock.getRandomDate(),
                        RandomParticipant.getParticipant(),
                        RandomAccountTypeMock.getAccountType(),
                        EntryKeyDtoMock.getEntryKeyDto(TypeKey.CNPJ)
                ),
                Arguments.of(
                        RandomAccountNumberMock.randomBranch(),
                        RandomAccountNumberMock.randomAccountNumber(),
                        RandomDateMock.getRandomDate(),
                        RandomParticipant.getParticipant(),
                        RandomAccountTypeMock.getAccountType(),
                        EntryKeyDtoMock.getEntryKeyDto(TypeKey.CPF)
                ),
                Arguments.of(
                        RandomAccountNumberMock.randomBranch(),
                        RandomAccountNumberMock.randomAccountNumber(),
                        RandomDateMock.getRandomDate(),
                        RandomParticipant.getParticipant(),
                        RandomAccountTypeMock.getAccountType(),
                        EntryKeyDtoMock.getEntryKeyDto(TypeKey.PHONE)
                ),
                Arguments.of(
                        RandomAccountNumberMock.randomBranch(),
                        RandomAccountNumberMock.randomAccountNumber(),
                        RandomDateMock.getRandomDate(),
                        RandomParticipant.getParticipant(),
                        RandomAccountTypeMock.getAccountType(),
                        EntryKeyDtoMock.getEntryKeyDto(TypeKey.EVP)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidValues_shouldInstantiateAccountDto(
            final String branch,
            final String accountNumber,
            final String openingDate,
            final String participant,
            final AccountType type,
            final EntryKeyDto dto
    ) {
        var result = new AccountDto(branch, accountNumber, openingDate, participant, type, dto);
        Assertions.assertInstanceOf(AccountDto.class, result);
        Assertions.assertEquals(branch, result.branch());
        Assertions.assertEquals(accountNumber, result.accountNumber());
        Assertions.assertEquals(openingDate, result.openingDate());
        Assertions.assertEquals(participant, result.participant());
        Assertions.assertEquals(type, result.accountType());
        Assertions.assertInstanceOf(EntryKeyDto.class, result.entryKeyDto());
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidAccountDto_shouldReturnAccountEntity(
            final String branch,
            final String accountNumber,
            final String openingDate,
            final String participant,
            final AccountType type,
            final EntryKeyDto dto
    ) {
        var accountDto = new AccountDto(branch, accountNumber, openingDate, participant, type, dto);
        var result = accountDto.toEntity();
        Assertions.assertInstanceOf(Account.class, result);
        Assertions.assertInstanceOf(EntryKey.class, result.getEntryKeys().getFirst());
    }

}
