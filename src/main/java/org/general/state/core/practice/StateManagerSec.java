package org.general.state.core.practice;

import io.vavr.Tuple;
import java.util.HashMap;
import java.util.Map;
import org.general.state.core.practice.TransitionII.From;

public class StateManagerSec {
  private final Map<StateII, Map<EventII, Tuple>> map;

  private StateManagerSec(Map<StateII, Map<EventII, Tuple>> map) {
    this.map = map;
  }

  public static StateManagerSecBuilder builder() {
    return new StateManagerSecBuilder();
  }

  // Builder
  public static class StateManagerSecBuilder {
    private Map<StateII, Map<EventII, Tuple>> map;

    public From from(StateManagerSecBuilder builder, StateII from) {
      return new From(builder,  from);
    }

    private StateManagerSecBuilder() {
      map = new HashMap<>();
    }

    public StateManagerSec build() {
      return new StateManagerSec(new HashMap<>());
    }
  }
}
