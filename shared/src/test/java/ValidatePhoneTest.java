import com.E3N.shared.utils.ValidatePhone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidatePhoneTest extends UnitTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "+5511987654321",
            "+5521998765432",
            "+5531976543210",
            "+5541965432109",
            "+5551954321098",
            "+5561943210987",
            "+5571932109876",
            "+5581921098765",
            "+5585910987654",
            "+5591909876543",
    })
    public void validatePhone(final String phone) {
        var key = ValidatePhone.isValid(phone);
        Assertions.assertTrue(key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+551198765432",     // missing one digit (too short)
            "+55119876543210",   // one digit too many (too long)
            "5511987654321",     // missing the + sign
            "+5511887654321",    // missing the mandatory 9 prefix
            "+55119876543",      // 9 present but only 7 digits after (too short)
            "+5500987654321",    // invalid DDD (00 doesn't exist)
            "+5511a87654321",    // contains a letter
            "+55 11 98765-4321", // has spaces and a dash (not normalized)
            "+1511987654321",    // wrong country code (should be 55, not 15+1)
            "++5511987654321",   // double plus sign
    })
    @NullSource
    public void invalidatePhone(final String phone) {
        var key = ValidatePhone.isValid(phone);
        Assertions.assertFalse(key);
    }
}
