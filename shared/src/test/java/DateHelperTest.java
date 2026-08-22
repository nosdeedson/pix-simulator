import com.E3N.shared.utils.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.stream.Stream;

public class DateHelperTest extends UnitTest{

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidDates_shouldReturnAnInstant(final String format, final String dateString){
        var result = DateHelper.getDateFrom(dateString, format);
        Assertions.assertInstanceOf(Instant.class, result);
    }

    @ParameterizedTest
    @MethodSource("inValidProvider")
    public void givenInvalidDates_shouldReturnNull(final String format, final String dateString){
        var result = DateHelper.getDateFrom(dateString, format);
        Assertions.assertNull(result);
    }

    static Stream<Arguments> provider(){
        return Stream.of(
                Arguments.of("dd/MM/yyyy HH:mm:ss", "20/08/2026 14:14:44"),
                Arguments.of("dd/MM/yyyy", "20/08/2026"),
                Arguments.of("dd-MM-yyyy HH:mm:ss", "20-08-2026 14:14:44"),
                Arguments.of("dd/MM/yyyy HH:mm", "20/08/2026 14:14"),
                Arguments.of("yyyy-MM-dd'T'HH:mm:ss", "2026-08-22T14:12:44"),
                Arguments.of("yyyy-MM-dd", "2026-08-22"),
                Arguments.of("MM/dd/yyyy HH:mm:ss", "08/22/2026 14:12:44"),
                Arguments.of("MM/dd/yyyy", "08/22/2026")
        );
    }

    static Stream<Arguments> inValidProvider(){
        return Stream.of(
                Arguments.of("dd/MM/yyyy HH:mm:ss", "32/08/2026 14:14:44"),
                Arguments.of("dd/MM/yyyy", "20/15/2026"),
                Arguments.of("dd-MM-yyyy HH:mm:ss", "20-08-2026 24:14:44"),
                Arguments.of("dd/MM/yyyy HH:mm", "20/08/20926 14:14"),
                Arguments.of("yyyy-MM-dd'T'HH:mm:ss", "2026-08-22T14:12:74"),
                Arguments.of("yyyy-MM-dd", "2026-18-22"),
                Arguments.of("MM/dd/yyyy HH:mm:tt", "08/22/2026 14:12:44"),
                Arguments.of("MM/dd/yyyy", "08/78/2026"),
                Arguments.of("MM/dd/iii", "08/22/2026"),
                Arguments.of("aa/dd/yyyy", "08/22/2026")
        );
    }
}
