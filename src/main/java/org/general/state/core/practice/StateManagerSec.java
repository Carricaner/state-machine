package org.general.state.core.practice;

import io.vavr.Tuple2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.general.state.core.practice.TransitionII.When;
import org.general.state.core.practice.action.ActionII;
import org.general.state.core.practice.event.EventII;
import org.general.state.core.practice.exception.StateMachineException;
import org.general.state.core.practice.exception.TransitionErrorDetails.TransitionBuildErrorDetail;
import org.general.state.core.practice.state.StateII;

public class StateManagerSec {
  private final Map<
          StateII, Map<EventII, Tuple2<Tuple2<StateII, ActionII>, Tuple2<StateII, ActionII>>>>
      map;

  private StateManagerSec(
      Map<StateII, Map<EventII, Tuple2<Tuple2<StateII, ActionII>, Tuple2<StateII, ActionII>>>>
          map) {
    this.map = map;
  }

  public static StateManagerSecBuilder builder() {
    return new StateManagerSecBuilder();
  }

  // Builder
  public static class StateManagerSecBuilder {
    private Map<StateII, Map<EventII, Tuple2<Tuple2<StateII, ActionII>, Tuple2<StateII, ActionII>>>>
        map;

    private StateManagerSecBuilder() {
      map = new HashMap<>();
    }

    public StateManagerSecBuilder from(StateII from, Consumer<When> when) {
      TransitionII transition = new TransitionII(this, from);
      when.accept(new When(transition));
      return this;
    }

    public void addTransition(TransitionII transition) {
      check(transition, map);
      if (!map.containsKey(transition.getFrom())) {
        map.put(transition.getFrom(), new HashMap<>());
      }
      map.get(transition.getFrom()).put(transition.getWhen(), transition.getToStatesAndActions());
    }

    public StateManagerSec build() {
      return new StateManagerSec(map);
    }

    public void check(
        TransitionII transition,
        Map<StateII, Map<EventII, Tuple2<Tuple2<StateII, ActionII>, Tuple2<StateII, ActionII>>>>
            map) {

      if (transition == null || transition.getFrom() == null) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(transition, (trans) -> "Invalid transition"));
      }

      StateII from = transition.getFrom();
      EventII when = transition.getWhen();
      if (map.get(from) != null && map.get(from).containsKey(when)) {
        throw new StateMachineException(
            new TransitionBuildErrorDetail(
                transition,
                (trans) ->
                    "The identical event, "
                        + transition.getWhen().toString()
                        + ", has been registered."));
      }
      List<StateII> list =
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
