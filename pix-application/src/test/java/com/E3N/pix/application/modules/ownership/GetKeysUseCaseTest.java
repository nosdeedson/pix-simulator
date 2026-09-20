package com.E3N.pix.application.modules.ownership;

import com.E3N.pix.application.UnitTest;
import com.E3N.pix.application.ownership.ExistentKeysDto;
import com.E3N.pix.application.ownership.CheckKeysUseCase;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class GetKeysUseCaseTest extends UnitTest {

    @Mock
    private OwnerRepositoryInterface ownerRepository;


    @InjectMocks
    private CheckKeysUseCase useCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        Mockito.reset(ownerRepository);
    }

    @Test
    void givenNoExistentKeys_whenCalling_execute_shouldReturnEmptyList(){
        List<String> list = new ArrayList<>();
        Mockito.when(ownerRepository.findByKeys(Mockito.anyList()))
                .thenReturn(list);

        var expectedList = List.of("thereIsNoKey", "No-existent");
        var result = useCase.execute(expectedList);
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(2, result.size());
        for (ExistentKeysDto dto : result){
            Assertions.assertFalse(dto.hasEntry());
            Assertions.assertTrue(expectedList.contains(dto.key()));

        }
    }

    @Test
    void givenExistentKeys_whenCalling_execute_shouldReturnListOfKeys(){
        List<String> expectedList = List.of(RandomKeysMock.randomEVP(), RandomKeysMock.randomEmails());
        Mockito.when(ownerRepository.findByKeys(Mockito.anyList()))
                .thenReturn(expectedList);

        var result = useCase.execute(expectedList);
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(2, result.size());
        for (ExistentKeysDto dto : result){
            Assertions.assertTrue(dto.hasEntry());
            Assertions.assertTrue(expectedList.contains(dto.key()));

        }
    }

    @Test
    void givenOneExistentKeyAndOtherNOt_whenCalling_execute_shouldReturnTwoKeysOneHasEntryOtherNo(){
        List<String> expectedList = List.of(RandomKeysMock.randomEVP(), "does-not-exist");
        Mockito.when(ownerRepository.findByKeys(Mockito.anyList()))
                .thenReturn(expectedList);

        var result = useCase.execute(expectedList);
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.getFirst().hasEntry());
        Assertions.assertTrue(result.getLast().hasEntry());
        Assertions.assertEquals(expectedList.getFirst(), result.getFirst().key());
        Assertions.assertEquals(expectedList.getLast(), result.getLast().key());
    }

}
