package org.general.state.event;

import org.general.state.Stateful;

public interface Event<T> {
  boolean succeed(Stateful<T> stateful);
}
