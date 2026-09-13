package com.E3N.pix.application.modules.owner;

import com.E3N.pix.application.GetEntryKeyUseCase;
import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.modules.owner.mocks.OwnerDtoMock;
import com.E3N.pix.application.modules.owner.mocks.OwnerMock;
import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.Either;
import com.E3N.test.Owner.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

public class GetEntryKeyUseCaseTest extends UnitTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;

    @InjectMocks
    private GetEntryKeyUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @Test
    void givenUseCase_shouldBeInstantiated(){
        Assertions.assertNotNull(useCase);
    }

    @Test
    void givenValidKey_whenCallingGetEntryKey_shouldReturnEitherOwner(){
        var expectedOwner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKey(Mockito.any()))
                .thenReturn(Optional.of(expectedOwner));
        var expectedKey = expectedOwner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        Notification notification = null;
        Owner owner = null;
        var result = useCase.getEntrykey(expectedKey);
        if (result instanceof Either.Left<Notification, Owner> (Notification value)){
            notification = value;
        } else if ( result instanceof Either.Right<Notification, Owner>(Owner value)){
            owner = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertNull(notification);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(expectedKey, owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(1, owner.getAccounts().getFirst().getEntryKeys().size());
    }

    @Test
    void givenInvalidKey_whenCallingGetEntryKey_shouldReturnEitherWithNotification(){
        Mockito.when(ownerRepository.findByKey(Mockito.any()))
                .thenReturn(Optional.empty());
        var expectedKey = "no-existent-key";
        var expectedDetail = "Entry key does not exist";
        Notification notification = null;
        Owner owner = null;
        var result = useCase.getEntrykey(expectedKey);
        if (result instanceof Either.Left<Notification, Owner>(Notification value)){
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value)){
            owner = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertNull(owner);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertEquals(expectedDetail, notification.getDetail());
        Assertions.assertEquals(404, notification.getStatus());
    }
}
