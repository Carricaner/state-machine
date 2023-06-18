package org.general.state;

import io.vavr.Tuple2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.general.state.Transition.When;
import org.general.state.action.Action;
import org.general.state.error.StateMachineException;
import org.general.state.error.TransitionErrorDetails.TransitionBuildErrorDetail;
import org.general.state.error.TransitionErrorDetails.TransitionErrorDetail;
import org.general.state.event.Event;
import org.general.state.state.State;

public class StateManager {
  private final Map<State, Map<Event, Tuple2<Tuple2<State, Action>, Tuple2<State, Action>>>> map;

  private final Stateful stateful;

  private StateManager(
      Map<State, Map<Event, Tuple2<Tuple2<State, Action>, Tuple2<State, Action>>>> map,
      Stateful stateful) {
    this.map = map;
    this.stateful = stateful;
  }

  public static StateManagerSecBuilder builder(Stateful stateful) {
    return new StateManagerSecBuilder(stateful);
  }

  public void transfer(State from, Event event) {
    if (!map.containsKey(from)) {
      throw new StateMachineException(
          new TransitionErrorDetail("Cannot transfer from: " + from.toString()));
    }
    if (!map.get(from).containsKey(event)) {
      throw new StateMachineException(
          new TransitionErrorDetail("Cannot transfer through: " + event.toString()));
    }
    Tuple2<Tuple2<State, Action>, Tuple2<State, Action>> tuple = map.get(from).get(event);
    if (event.succeed(stateful)) {
      tuple._1._2.run();
      stateful.changeToState(tuple._1._1);
    } else {
      tuple._2._2.run();
      stateful.changeToState(tuple._2._1);
    }
  }

  // Builder
  public static class StateManagerSecBuilder {
    private Map<State, Map<Event, Tuple2<Tuple2<State, Action>, Tuple2<State, Action>>>> map;
    private final Stateful stateful;

    private StateManagerSecBuilder(Stateful stateful) {
      map = new HashMap<>();
      this.stateful = stateful;
    }

    public StateManagerSecBuilder from(State from, Consumer<When> when) {
      Transition transition = new Transition(this, from);
      when.accept(new When(transition));
      return this;
    }

    public void addTransition(Transition transition) {
      check(transition, map);
      if (!map.containsKey(transition.getFrom())) {
        map.put(transition.getFrom(), new HashMap<>());
      }
      map.get(transition.getFrom()).put(transition.getWhen(), transition.getToStatesAndActions());
    }

    public StateManager build() {
      return new StateManager(map, stateful);
    }

    public void check(
        Transition transition,
        Map<State, Map<Event, Tuple2<Tuple2<State, Action>, Tuple2<State, Action>>>> map) {

      if (transition == null || transition.getFrom() == null) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(transition, (trans) -> "Invalid transition"));
      }

      State from = transition.getFrom();
      Event when = transition.getWhen();
      if (map.get(from) != null && map.get(from).containsKey(when)) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(
                transition,
                (trans) ->
                    "The identical event, "
                        + transition.getWhen().toString()
                        + ", has been registered."));
      }
      List<State> list =
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
