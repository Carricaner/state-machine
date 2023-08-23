package org.general.state.component.movieticket;

import org.general.state.Stateful;
import org.general.state.event.Event;

public enum MovieTicketEvent implements Event<String, Integer> {
  RELEASE,
  BOOK {
    private int releaseTimes = 0;

    @Override
    public boolean succeed() {
      releaseTimes++;
      return releaseTimes > 1;
    }
  },
  DELETE,
  REDEEM;

  @Override
  public boolean succeed() {
    return true;
  }
}
