package org.general.state;

import org.general.state.state.State;

public interface Stateful<S, E> {
  void changeToState(State<S> state);
}
