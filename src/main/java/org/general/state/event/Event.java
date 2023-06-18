package org.general.state.event;

import org.general.state.Stateful;

public interface Event<S, E> {
  boolean succeed(Stateful<S, E> stateful);
}
