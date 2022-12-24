import org.example.core.domain.richmenu.RichMenu;
import org.example.core.domain.richmenu.state.RichMenuState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RichMenuTest {
  @Test
  void testRichMenuTransfer() {

    RichMenu richMenu = new RichMenu();

    richMenu.publish();

    Assertions.assertEquals(RichMenuState.PUBLISHED, richMenu.getState());

    richMenu.halt();

    Assertions.assertEquals(RichMenuState.HALTED, richMenu.getState());

    richMenu.delete();

    Assertions.assertEquals(RichMenuState.DELETED, richMenu.getState());
  }
}
