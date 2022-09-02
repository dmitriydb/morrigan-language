package interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;

public class RunTimeErrorTest {

  @Test
  public void comparingWrongValues() {
    Morrigan morrigan = new Morrigan();
    try {
      morrigan.interpret("morrigan calls a(1,2).");
    } catch (Throwable t) {
      assertThat(morrigan.getStderr()).hasSize(1);
    }
  }
}
