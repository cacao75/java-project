package darkchoco.rwsd;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;

class BankStatementCSVParserTest {

    private final BankStatementParser bankStatementParser = new BankStatementCSVParser();

    @Test
    public void shouldParseOneCorrectLine() throws Exception {
        String line = "30-01-2017,-50,Tesco";

        BankTransaction result = bankStatementParser.parseFrom(line);

        BankTransaction expected =
                new BankTransaction(
                        LocalDate.of(2017, Month.JANUARY, 30),
                        -50,
                        "Tesco");
        assertThat(expected.date()).isEqualTo(result.date());
        assertThat(expected.amount()).isEqualTo(result.amount());
        assertThat(expected.description()).isEqualTo(result.description());
    }
}