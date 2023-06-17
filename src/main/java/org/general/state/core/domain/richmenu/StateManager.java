package org.general.state.core.domain.richmenu;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;


public class StateManager<S extends Enum<S>, E extends Enum<E>> {

  private final Map<S, Map<E, SimpleImmutableEntry<S,  Action>>> transitions = new HashMap<>();

  Transition<S, E> transition;

  public Transition.From<S, E> start() {
    transition = new Transition<>(this);

    return new Transition.From<>(transition);
  }

  boolean hasSameSourceState(S sourceState) {
    return transitions.containsKey(sourceState);
  }

  /**
   * Transit to the state on the specific event
   *
   * @param event
   * @return next state
   */
  public S on(S currentState, E event) throws StateManagerBuildException {
    if (transitions.isEmpty()) {
      throw new StateTransitionException("No transitions are set, please config transitions first");
    }

    return fire(currentState, event);
  }

  /**
   * Fire an event to change state, throw an exception when the source state or the event is not
   * set.
   *
   * @param currentState
   * @param event
   * @return the state changes after
   */
  private S fire(S currentState, E event) throws StateManagerBuildException {
    if (!transitions.containsKey(currentState)) {
      throw new StateTransitionException(
          "The current state doesn't exist in the transitions, current state: " + currentState);
    }

    Map<E, SimpleImmutableEntry<S,  Action>> t = transitions.get(currentState);

    if (!t.containsKey(event)) {
      throw new StateTransitionException(
          "The event doesn't exist in the transitions, event: " + event);
    }

    SimpleImmutableEntry<S,  Action> s = t.get(event);

    Action a = s.getValue();
    if (a != null) {
      a.act();
    }

    return s.getKey();
  }

  void addTransition() throws StateManagerBuildException {
    if (transition == null) {
      throw new StateManagerBuildException("Please call the from method first");
    }

    S sourceState = transition.from;
    if (sourceState == null) {
      throw new StateManagerBuildException("Source state is not set by the from method");
    }

    transition.transitions.forEach(
        (key, value) -> {
          Map<E, SimpleImmutableEntry<S,  Action>> map =
              transitions.getOrDefault(sourceState, new HashMap<>());
          map.put(key, value);
          transitions.put(sourceState, map);
        });
  }


}
