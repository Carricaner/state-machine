package org.example.state;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represent transitions in a state machine
 *
 * @param <S> State enumeration
 * @param <E> Event enumeration
 */
public class Transition<S extends Enum<S>, E extends Enum<E>> {
  StateManager<S, E> stateManager;
  final Map<E, SimpleImmutableEntry<S,  Action>> transitions = new HashMap<>();

  /*
   * Start state
   */
   S from;

  /*
   * Event that triggers the transition
   */
   E when;

  /*
   * Destination state
   */
   S to;

  public Transition(StateManager<S, E> stateManager) {
    this.stateManager = stateManager;
  }

  /**
   * Represent the start state of the transition
   *
   * @param <S>
   * @param <E>
   */
  public static class From<S extends Enum<S>, E extends Enum<E>> {
    private final Transition<S, E> transition;

    public From(Transition<S, E> transition) {
      this.transition = transition;
    }

    public From<S, E> from(S sourceState, Consumer<When<S, E>> when) {
      if (transition.stateManager.hasSameSourceState(sourceState)) {
        throw new StateManagerBuildException("The same from state has been set");
      }

      transition.from = sourceState;

      when.accept(new When<>(transition));

      return transition.stateManager.start();
    }

    /**
     * To finish the whole building of the state machine
     *
     * @return
     */
    public StateManager<S, E> end() {
      return transition.stateManager;
    }
  }

  /**
   * Represent the event that triggers the transition
   *
   * @param <S>
   * @param <E>
   */
  public static class When<S extends Enum<S>, E extends Enum<E>> {
    private final Transition<S, E> transition;

    public When(Transition<S, E> transition) {
      this.transition = transition;
    }

    public To<S, E> when(E event) {
      if (transition.transitions.containsKey(event)) {
        throw new StateManagerBuildException("The same event has been set");
      }

      transition.when = event;

      return new To<>(transition);
    }
  }

  /**
   * Represent the destination state of the transition
   *
   * @param <S>
   * @param <E>
   */
  public static class To<S extends Enum<S>, E extends Enum<E>> {
    private final Transition<S, E> transition;

    public To(Transition<S, E> transition) {
      this.transition = transition;
    }

    public Act<S, E> to(S targetState) {
      if (transition.when == null) {
        throw new StateManagerBuildException("Event is not set by the when method");
      }

      transition.transitions.put(transition.when, new SimpleImmutableEntry<>(targetState, null));
      transition.to = targetState;

      return new Act<>(transition);
    }
  }

  /**
   * Represent the action taken when transiting between states
   *
   * @param <S>
   * @param <E>
   */
  public static class Act<S extends Enum<S>, E extends Enum<E>> {
    private final Transition<S, E> transition;

    public Act(Transition<S, E> transition) {
      this.transition = transition;
    }

    public Act<S, E> andDo(Action action) {
      if (transition.when == null) {
        throw new StateManagerBuildException("Event is not set by the when method");
      }

      if (transition.transitions.get(transition.when).getValue() != null) {
        throw new StateManagerBuildException("Only one action can be set in a transition");
      }

      if (transition.to == null) {
        throw new StateManagerBuildException("Target state is not set by the to method");
      }

      transition.transitions.put(
          transition.when, new SimpleImmutableEntry<>(transition.to, action));

      return this;
    }

    public To<S, E> when(E event) {
      if (transition.transitions.containsKey(event)) {
        throw new StateManagerBuildException("The same event has been set");
      }

      transition.when = event;

      return new To<>(transition);
    }

    /**
     * To finish the building of the transition
     *
     * @return
     */
    public From<S, E> build() {
      transition.stateManager.addTransition();

      return transition.stateManager.start();
    }
  }
}
