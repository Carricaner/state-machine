package org.general.state.event;

import org.general.state.Stateful;

public enum EventOOO implements Event {
  a,b,c,e,d;

  @Override
  public boolean succeed(Stateful stateful) {
    return true;
  }
}
