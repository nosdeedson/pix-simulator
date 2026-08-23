import com.E3N.shared.utils.ValidateUUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidateUUIDTest extends UnitTest{

    @ParameterizedTest
    @ValueSource(strings = {
            "f47ac10b-58cc-4372-a567-0e02b2c3d479",
            "9b2e1a3c-6f4d-4e8a-b1c2-3d4e5f6a7b8c",
            "c3d4e5f6-a7b8-49c0-91d2-e3f4a5b6c7d8",
            "1a2b3c4d-5e6f-4789-a0b1-c2d3e4f5a6b7",
            "7e8f9a0b-1c2d-4e3f-8a9b-0c1d2e3f4a5b",
            "d0e1f2a3-b4c5-46d7-98e9-f0a1b2c3d4e5",
            "5f6a7b8c-9d0e-41f2-a3b4-c5d6e7f8a9b0",
            "e2f3a4b5-c6d7-48e9-90f1-a2b3c4d5e6f7",
            "3b4c5d6e-7f8a-49b0-81c2-d3e4f5a6b7c8",
            "a9b0c1d2-e3f4-4506-97a8-b9c0d1e2f3a4",
    })
    public void validateEVP(final String EVP) {
        var key = ValidateUUID.isValid(EVP);
        Assertions.assertTrue(key);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "f47ac10b-58cc-4372-a567-0e02b2c3d47",      // missing one character (too short)
            "f47ac10b-58cc-4372-a567-0e02b2c3d4799",    // one character too many
            "f47ac10b58cc-4372-a567-0e02b2c3d479",      // missing a dash
            "f47ac10b-58cc-4372-a567-0e02b2c3d47g",     // contains invalid character 'g'
            "f47ac10b_58cc_4372_a567_0e02b2c3d479",     // underscores instead of dashes
            "f47ac10b-58cc-4372-a567",                  // missing last segment
            "-58cc-4372-a567-0e02b2c3d479",             // missing first segment
            "f47ac10b--58cc-4372-a567-0e02b2c3d479",    // double dash
            "F47AC10B-58CC-4372-A567-0E02B2C3D479Z",    // extra trailing character
            "not-a-uuid-at-all-1234",                   // completely wrong format
    })
    @NullSource
    public void invalidateEVP(final String EVP) {
        var key = ValidateUUID.isValid(EVP);
        Assertions.assertFalse(key);
    }
}
