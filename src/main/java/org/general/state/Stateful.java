package org.general.state;

import org.general.state.state.State;

public interface Stateful<T> {
  void changeToState(State<T> state);
}
