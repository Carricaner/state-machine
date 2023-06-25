package org.general.state;

import io.vavr.Tuple2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.general.state.Transition.When;
import org.general.state.action.Action;
import org.general.state.error.StateMachineException;
import org.general.state.error.TransitionErrorDetails.TransitionBuildErrorDetail;
import org.general.state.error.TransitionErrorDetails.TransitionErrorDetail;
import org.general.state.event.Event;
import org.general.state.state.State;

public class StateManager<S, E> {
  private final Map<
          State<S>, Map<Event<S, E>, Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>>>>
      map;

  private final Stateful<S, E> stateful;

  private StateManager(
      Map<State<S>, Map<Event<S, E>, Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>>>>
          map,
      Stateful<S, E> stateful) {
    this.map = map;
    this.stateful = stateful;
  }

  public static <S, E> StateManagerSecBuilder<S, E> builder(Stateful<S, E> stateful) {
    return new StateManagerSecBuilder<>(stateful);
  }

  /**
   * The method is to transfer the state from "from" through "event".
   *
   * @param from the from state
   * @param event the event
   * @return boolean - represents whether the event is successful or not
   */
  public boolean transfer(State<S> from, Event<S, E> event) {
    if (!map.containsKey(from)) {
      throw new StateMachineException(
          new TransitionErrorDetail("Cannot transfer from: " + from.toString()));
    }
    if (!map.get(from).containsKey(event)) {
      throw new StateMachineException(
          new TransitionErrorDetail("Cannot transfer through: " + event.toString()));
    }
    Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>> tuple = map.get(from).get(event);
    boolean success = event.succeed(stateful);
    if (success) {
      tuple._1._2.run();
      stateful.changeToState(tuple._1._1);
    } else {
      tuple._2._2.run();
      stateful.changeToState(tuple._2._1);
    }
    return success;
  }

  public List<Event<S,E>> getPossibleEvents() {
    State<S> state = stateful.getState();
    if (state != null && map.containsKey(state)) {
      return map.get(stateful.getState()).keySet().stream().toList();
    }
    return List.of();
  }

  public List<State<S>> getPossibleStates() {
    State<S> state = stateful.getState();
    if (state != null && map.containsKey(state)) {
      return map.get(stateful.getState()).values()
          .stream()
          .map(tuple -> tuple.map(_1 -> _1._1, _2 -> _2._1))
          .flatMap(tuple -> Stream.of(tuple._1, tuple._2))
          .distinct()
          .toList();
    }
    return List.of();
  }


  // Builder
  public static class StateManagerSecBuilder<S, E> {
    private final Stateful<S, E> stateful;
    private final Map<
            State<S>, Map<Event<S, E>, Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>>>>
        map;

    private StateManagerSecBuilder(Stateful<S, E> stateful) {
      map = new HashMap<>();
      this.stateful = stateful;
    }

    public StateManagerSecBuilder<S, E> from(State<S> from, Consumer<When<S, E>> when) {
      Transition<S, E> transition = new Transition<>(this, from);
      when.accept(new When<>(transition));
      return this;
    }

    public void addTransition(Transition<S, E> transition) {
      check(transition, map);
      if (!map.containsKey(transition.getFrom())) {
        map.put(transition.getFrom(), new HashMap<>());
      }
      map.get(transition.getFrom()).put(transition.getWhen(), transition.getToStatesAndActions());
    }

    public StateManager<S, E> build() {
      return new StateManager<>(map, stateful);
    }

    public void check(
        Transition<S, E> transition,
        Map<State<S>, Map<Event<S, E>, Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>>>>
            map) {

      if (transition == null || transition.getFrom() == null) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(transition, (trans) -> "Invalid transition"));
      }

      State<S> from = transition.getFrom();
      Event<S, E> when = transition.getWhen();
      if (map.get(from) != null && map.get(from).containsKey(when)) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(
                transition,
                (trans) ->
                    "The identical event, "
                        + transition.getWhen().toString()
                        + ", has been registered."));
      }
      List<State<S>> list =
          List.of(
              transition.getToStatesAndActions()._1._1, transition.getToStatesAndActions()._2._1);
      if (list.contains(from)) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(
                transition,
                (trans) ->
                    "The target states, "
                        + list
                        + ", contains the from state, "
                        + from.toString()));
      }
    }
  }
}
