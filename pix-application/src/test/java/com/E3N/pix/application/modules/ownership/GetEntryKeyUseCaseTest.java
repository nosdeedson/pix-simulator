package com.E3N.pix.application.modules.ownership;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.modules.ownership.mocks.OwnerMock;
import com.E3N.pix.application.ownership.GetEntryKeyUseCase;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @Test
    void givenUseCase_shouldBeInstantiated() {
        Assertions.assertNotNull(useCase);
    }

    @Test
    void givenValidKey_whenCallingGetEntryKey_shouldReturnEitherOwner() {
        var expectedOwner = OwnerMock.getOwner(TypePerson.LEGAL_PERSON);
        Mockito.when(ownerRepository.findByKey(Mockito.any()))
                .thenReturn(Optional.of(expectedOwner));
        var expectedKey = expectedOwner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        Notification notification = null;
        Owner owner = null;
        var result = useCase.getEntryKey(expectedKey, false);
        if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertNull(notification);
        Assertions.assertInstanceOf(Owner.class, owner);
        Assertions.assertEquals(expectedKey, owner.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey());
        Assertions.assertEquals(1, owner.getAccounts().getFirst().getEntryKeys().size());
    }

    @Test
    void givenInvalidKey_whenCallingGetEntryKey_shouldReturnEitherWithNotification() {
        Mockito.when(ownerRepository.findByKey(Mockito.any()))
                .thenReturn(Optional.empty());
        var expectedKey = "no-existent-key";
        var expectedDetail = "Entry key does not exist";
        Notification notification = null;
        Owner owner = null;
        var result = useCase.getEntryKey(expectedKey, false);
        if (result instanceof Either.Left<Notification, Owner>(Notification value)) {
            notification = value;
        } else if (result instanceof Either.Right<Notification, Owner>(Owner value)) {
            owner = value;
        }
        Assertions.assertInstanceOf(Either.class, result);
        Assertions.assertNull(owner);
        Assertions.assertInstanceOf(Notification.class, notification);
        Assertions.assertEquals(expectedDetail, notification.getDetail());
        Assertions.assertEquals(404, notification.getStatus());
    }

    @Test
    void givenValidKey_whenAskingForStatistics_shouldReturnEitherWithNotification() {
        var doesNotMatter = UUID.randomUUID().toString();
        var askedForStatistics = true;
        var expectedDetail = "Functionality not done yet.";
        Either<Notification, Owner> result = useCase.getEntryKey(doesNotMatter, askedForStatistics);
        result.fold(
                notification -> {
                    Assertions.assertNotNull(notification);
                    Assertions.assertEquals(expectedDetail, notification.getDetail());
                    return notification;
                },
                owner -> {
                    Assertions.assertNull(owner);
                    return null;
                }
        );
    }
}
