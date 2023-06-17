package org.general.state.core.practice;

import org.general.state.core.practice.StateManagerSec.StateManagerSecBuilder;

public class TransitionII {
  private final StateManagerSecBuilder builder;
  private final StateII from;
  private EventII when;
  private StateII to;

  public TransitionII(StateManagerSecBuilder builder, StateII from) {
    this.builder = builder;
    this.from = from;
  }


  public static class From {
    private final TransitionII transition;

    public From(StateManagerSecBuilder builder, StateII from) {
      this.transition = new TransitionII(builder, from);
    }
  }

  public static class When<State, Event> {}

  public static class To<State, Event> {}

  public static class Act<State, Event> {}
}
