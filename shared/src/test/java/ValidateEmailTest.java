import com.E3N.shared.utils.ValidateEmail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidateEmailTest extends UnitTest{

    @ParameterizedTest
    @ValueSource(strings = {
            "joao.silva@gmail.com",
            "maria_santos@outlook.com",
            "carlos-oliveira@empresa.com.br",
            "ana.paula123@yahoo.com",
            "pedro.costa+newsletter@gmail.com",
            "juliana.lima@dominio.co.uk",
            "rafael_souza99@hotmail.com",
            "fernanda.alves@meusite.com.br",
            "lucas.pereira@icloud.com",
            "beatriz.rocha@teste-empresa.org",
    })
    public void validateEmail(final String email) {
        var key = ValidateEmail.isValid(email);
        Assertions.assertTrue(key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@dominio",
            "@dominio.com",
            "usuario@",
            "usuario domínio@dominio.com",
            "usuario@@dominio.com",
            "usuario@.com",
            "usuario@dominio..com",
            "usuário@dominio.com",
            ".usuario@dominio.com",
            "usuario@dominio.c",
    })
    @NullSource
    public void invalidateEmail(final String email) {
        var key = ValidateEmail.isValid(email);
        Assertions.assertFalse(key);
    }
}
