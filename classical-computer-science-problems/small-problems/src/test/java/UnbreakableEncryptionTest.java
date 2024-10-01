import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnbreakableEncryptionTest {

    @Test
    public void givenData_whenEncrypted_thenTheSameAfterDecrypted() {
        String original = "One Time Pad!";

        KeyPair kp = UnbreakableEncryption.encrypt(original);
        assertThat(original).isEqualTo(UnbreakableEncryption.decrypt(kp));
    }
}
