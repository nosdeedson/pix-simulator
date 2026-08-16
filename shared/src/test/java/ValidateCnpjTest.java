import com.E3N.shared.utils.ValidateCnpj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidateCnpjTest extends UnitTest{

    @ParameterizedTest
    @ValueSource(strings = {
            "PK9R7RP7000123",
//            "B84YPYML000155",
//            "KGB4GPVM000156",
//            "V0N6TGY9000114",
//            "W6REERN7000108",
//            "Y8YLX0S2000102",
//            "461GVXLG000190",
//            "X4BRCTJK000111",
//            "R058BXS8000115",
//            "3TC6A4RV000100"
    })
    public void validate(String cnpj){
        boolean result = ValidateCnpj.validate(cnpj);
        Assertions.assertTrue(result);
    }
}
