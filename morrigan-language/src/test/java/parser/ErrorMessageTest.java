package parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;

public class ErrorMessageTest {

  @Test
  public void errorMessage_shouldContainLineNumberAndSymbolPosition() {
    Morrigan morrigan = new Morrigan();
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("morrigan says that a");
    });
    assertThat(morrigan.getStderr().get(0)).contains("line 1", "position 20");
  }

  @Test
  public void errorMessage_shouldContainLineNumberAndSymbolPosition_multiline() {
    Morrigan morrigan = new Morrigan();
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("morrigan says that a is 10. \n"
          + "morrigan says that b");
    });
    assertThat(morrigan.getStderr().get(0)).contains("line 2", "position 20");
  }

  @Test
  public void errorMessage_shouldWorkWithMorrigan() {
    Morrigan morrigan = new Morrigan();
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("morrigan abrakadabra");
    });
    assertThat(morrigan.getStderr().get(0)).contains("line 1", "position 9");
  }
}
