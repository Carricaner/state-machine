package org.example.state;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import org.example.state.Transition.From;


public class StateManager<S extends Enum<S>, E extends Enum<E>> {

  private final Map<S, Map<E, SimpleImmutableEntry<S,  Action>>> transitions = new HashMap<>();

  Transition<S, E> transition;

  public From<S, E> start() {
    transition = new Transition<>(this);

    return new From<>(transition);
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

  /**
   * Create a transition from sourceState to targetState on event
   *
   * @deprecated Use `from` to define transition instead
   * @param sourceState
   * @param event
   * @param targetState
   * @return
   */
  @Deprecated(forRemoval = true)
  public StateManager<S, E> fromTo(S sourceState, E event, S targetState)
      throws StateManagerBuildException {
    fromTo(sourceState, event, targetState, null);
    return this;
  }

  /**
   * Create a transition from sourceState to targetState on event and execute an action when
   * transiting
   *
   * @deprecated Use `from` to define transition instead
   * @param sourceState
   * @param event
   * @param targetState
   * @param action
   * @return
   */
  @Deprecated(forRemoval = true)
  public StateManager<S, E> fromTo(S sourceState, E event, S targetState,  Action action)
      throws StateManagerBuildException {
    Map<E, SimpleImmutableEntry<S,  Action>> s =
        transitions.getOrDefault(sourceState, new HashMap<>());
    if (s.containsKey(event)) {
      throw new StateManagerBuildException("There is a same event already");
    }
    s.put(event, new SimpleImmutableEntry<>(targetState, action));
    transitions.put(sourceState, s);

    return this;
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
