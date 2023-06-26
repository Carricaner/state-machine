package org.general.state;

import java.util.List;
import org.general.state.event.Event;
import org.general.state.state.State;

public interface Stateful<S, E> {
  State<S> getState();
  List<Event<S, E>> getPossibleEvents();
  List<State<S>> getPossibleStates();
}
