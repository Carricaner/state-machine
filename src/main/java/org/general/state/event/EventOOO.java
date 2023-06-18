package org.general.state.event;

import org.general.state.Stateful;

public enum EventOOO implements Event<String, Integer> {
  a,b,c,e,d;

  @Override
  public boolean succeed(Stateful<String, Integer> stateful) {
    return true;
  }
}
