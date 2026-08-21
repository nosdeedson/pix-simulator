import com.E3N.shared.utils.ValidateCnpj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("all")
public class ValidateCnpjTest extends UnitTest{

    @ParameterizedTest
    @ValueSource(strings = {
           "PK9R7RP7000123",
            "B84YPYML000155",
            "KGB4GPVM000156",
            "V0N6TGY9000114",
            "W6REERN7000108",
            "Y8YLX0S2000102",
            "461GVXLG000190",
            "X4BRCTJK000111",
            "R058BXS8000115",
            "3TC6A4RV000100"
    })
    public void validateAlphaNumericCnpj(String cnpj){
        boolean result = ValidateCnpj.validate(cnpj);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PK9R7RX7000123",
            "B84YPYXL000153",
            "KGB4GPXM000156",
            "V0N6TGX9000114",
            "W6RE4RN7000108",
            "Y8YL30S2000102",
            "461GXXLG000190",
            "X4BRXTJK000111",
            "R058XXS8000125",
            "3TC6X4RV000100"
    })
    public void invalidateAlphaNumericCnpj(String cnpj){
        boolean result = ValidateCnpj.validate(cnpj);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12754134000187",
            "13926961000173",
            "95949320000107",
            "21242750000140",
            "05274599000175",
            "06042341000106",
            "00830199000185",
            "05067449000190",
            "33066786000107",
            "53136250000110",
    })
    public void validateJustNumericCnpj(String cnpj){
        boolean result = ValidateCnpj.validate(cnpj);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12754124000187",
            "13926951000173",
            "95949220000107",
            "21242550000140",
            "05274199000175",
            "06042321000106",
            "00830149000185",
            "05067469000190",
            "33066756000107",
            "53136250000100",
            "36250000100",
            "53136250000",
    })
    public void invalidateJustNumericCnpj(String cnpj){
        boolean result = ValidateCnpj.validate(cnpj);
        Assertions.assertFalse(result);
    }
}
