package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.name.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NameTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "Maria Eduarda Silva",
            "João Pedro Oliveira",
            "Ana Carolina Santos",
            "José Carlos Ferreira",
            "Emily Rose Johnson",
            "James William Smith",
            "María José García",
            "Juan Carlos Rodríguez",
            "Sofía Isabella Martínez",
            "Carlos Eduardo de Souza"
    })
    public void givenValidNames_shouldReturnName(final String name) {
        var result = Name.getInstance(name);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertEquals(result.getName(), name);
        Assertions.assertNull(result.getNotification());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Maria",
            "João Pedro Oliveira@",
            " ",
            "Jo",
            "Você gostaria de saber como funciona o processo para mudar de nome diretamente no cartório ou quer ver exemplos de nomes reais",
            "James @@William Smith",
            "María#",
            "Juan Carlo$ Rodríguez",
            "",
    })
    @NullSource
    public void givenInvalidNames_shouldReturnNull(final String name) {
        var result = Name.getInstance(name);
        Assertions.assertInstanceOf(Name.class, result);
        Assertions.assertNull(result.getName());
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
    }
}
