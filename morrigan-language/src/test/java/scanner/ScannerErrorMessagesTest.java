package scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;

public class ScannerErrorMessagesTest {

    @Test
    public void stderr_ShouldContainScanErrorMessages() {
        Morrigan morrigan = new Morrigan();
        assertThrows(Throwable.class, () -> {
            morrigan.interpret("###");
        });
        assertThat(morrigan.getStderr().get(0)).contains("#", "line 1", "position 1", "35");
    }

    @Test
    public void stderr_ShouldContainScanErrorMessages_multiline() {
        Morrigan morrigan = new Morrigan();
        assertThrows(Throwable.class, () -> {
            morrigan.interpret("morrigan says that a is 3. \n"
                + "###");
        });
        assertThat(morrigan.getStderr().get(0)).contains("#", "line 2", "position 1", "35");
    }


}
