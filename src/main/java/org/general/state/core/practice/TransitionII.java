package org.general.state.core.practice;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.general.state.core.practice.StateManagerSec.StateManagerSecBuilder;
import org.general.state.core.practice.action.ActionII;
import org.general.state.core.practice.action.DefaultActionII;
import org.general.state.core.practice.event.EventII;
import org.general.state.core.practice.state.StateII;

public class TransitionII {
  private final StateManagerSecBuilder builder;
  private final StateII from;
  private EventII when;
  private Tuple2<StateII, StateII> to;
  private Tuple2<ActionII, ActionII> actions =
      Tuple.of(new DefaultActionII(), new DefaultActionII());

  public TransitionII(StateManagerSecBuilder builder, StateII from) {
    this.builder = builder;
    this.from = from;
  }

  public StateII getFrom() {
    return from;
  }

  public EventII getWhen() {
    return when;
  }

  public Tuple2<Tuple2<StateII, ActionII>, Tuple2<StateII, ActionII>> getToStatesAndActions() {
    return Tuple.of(Tuple.of(to._1, actions._1), Tuple.of(to._2, actions._2));
  }

  public static class When<S, E> {
    private final TransitionII transition;

    public When(TransitionII transition) {
      this.transition = transition;
    }

    public To<S, E> when(EventII event) {
      this.transition.when = event;
      return new To<>(transition);
    }
  }

  public static class To<S, E> {
    private final TransitionII transition;

    public To(TransitionII transition) {
      this.transition = transition;
    }

    public Act<S, E> to(StateII successState, StateII failureState) {
      this.transition.to = new Tuple2<>(successState, failureState);
      return new Act<>(transition);
    }
  }

  public static class Act<S, E> {
    private final TransitionII transition;

    public Act(TransitionII transition) {
      this.transition = transition;
    }

    public Act<S, E> andDo(ActionII successAction, ActionII failureAction) {
      this.transition.actions = Tuple.of(successAction, failureAction);
      return this;
    }

    public To<S, E> when(EventII event) {
      this.transition.builder.addTransition(this.transition);
      erase();
      this.transition.when = event;
      return new To<>(transition);
    }

    private void erase() {
      this.transition.when = null;
      this.transition.to = null;
      this.transition.actions = Tuple.of(new DefaultActionII(), new DefaultActionII());
    }

    public void finished() {
      this.transition.builder.addTransition(this.transition);
    }
  }
}
