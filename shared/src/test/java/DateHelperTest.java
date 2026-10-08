import com.E3N.shared.utils.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public class DateHelperTest extends UnitTest {

    static List<Arguments> providerInvalidIsoString() {
        return List.of(
                Arguments.of("2026-10-06 22:15:30.000Z"),
                Arguments.of("2026-13-06 22:15:30"),
                Arguments.of("06-10-2026T22:15:30.000Z"),
                Arguments.of("2026/10/06T22:15:30.000Z"),
                Arguments.of("2026-02-30T12:00:00.000Z"),
                Arguments.of("2026-13-01T08:30:00.000Z"),
                Arguments.of("2025-06-31T15:00:00.000Z"),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("14/05/2026")
        );
    }

    @ParameterizedTest
    @MethodSource("providerInvalidIsoString")
    void givenInvalidIsoString_whenCalling_fromString_shouldReturnAXMLGregorianCalendar(String isoString) {
        var result = DateHelper.fromString(isoString);
        Assertions.assertNull(result);
    }

    static List<Arguments> providerValidIsoString() {
        return List.of(
                Arguments.of("2026-10-06T22:15:30.000Z"),
                Arguments.of("2026-01-01T00:00:00.000Z"),
                Arguments.of("2025-12-25T08:30:00.123Z"),
                Arguments.of("2024-02-29T12:00:00.000Z"),
                Arguments.of("2023-07-04T20:45:15.999Z")
        );
    }

    @ParameterizedTest
    @MethodSource("providerValidIsoString")
    void givenValidIsoString_whenCalling_fromString_shouldReturnAXMLGregorianCalendar(String isoString) {
        var result = DateHelper.fromString(isoString);
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(XMLGregorianCalendar.class, result);
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidDates_shouldReturnAnInstant(final String format, final String dateString) {
        var result = DateHelper.getDateFrom(dateString, format);
        Assertions.assertInstanceOf(Instant.class, result);
    }

    @ParameterizedTest
    @MethodSource("inValidProvider")
    public void givenInvalidDates_shouldReturnNull(final String format, final String dateString) {
        var result = DateHelper.getDateFrom(dateString, format);
        Assertions.assertNull(result);
    }

    static Stream<Arguments> provider() {
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

    static Stream<Arguments> inValidProvider() {
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
