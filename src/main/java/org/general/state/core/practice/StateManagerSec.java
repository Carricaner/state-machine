package org.general.state.core.practice;

import io.vavr.Tuple2;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.general.state.core.practice.TransitionII.When;
import org.general.state.core.practice.action.ActionII;
import org.general.state.core.practice.event.EventII;
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

    public StateManagerSecBuilder from(StateII from, Consumer<When<StateII, EventII>> when) {
      TransitionII transition = new TransitionII(this, from);
      when.accept(new When<>(transition));
      return this;
    }

    public void addTransition(TransitionII transition) {
      if (transition == null || transition.getFrom() == null) {
        // TODO throw exception
        return;
      }
      if (!map.containsKey(transition.getFrom())) {
        map.put(transition.getFrom(), new HashMap<>());
      }

      map.get(transition.getFrom()).put(transition.getWhen(), transition.getToStatesAndActions());
    }

    public StateManagerSec build() {
      return new StateManagerSec(map);
    }
  }
}
