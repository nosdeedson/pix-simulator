import com.E3N.shared.utils.ValidateCpf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidateCpfTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "67894545007",
            "21215462000",
            "21386982032",
            "31080379002",
            "44785218088",
            "56656495036",
    })
    public void validateCpf(String cpf) {
        boolean result = ValidateCpf.validate(cpf);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "67894j4j007",
            "2121j462000",
            "2138698r032",
            "3108037900t",
            "4478j218088",
            "j6656495036"
    })
    @NullSource
    public void cpfWithLetters(String cpf) {
        boolean result = ValidateCpf.validate(cpf);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "93005422105",
            "13045448405",
            "93217491144",
            "10172472448",
            "34119475419",
            "82556440420",
            "90886470985",
            "06790455846",
            "10240463880",
            "78161407679",
            "61407679",
            "78161407",
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999",
            "00000000000"
    })
    public void invalidsCpf(String cpf) {
        boolean result = ValidateCpf.validate(cpf);
        Assertions.assertFalse(result);
    }
}


