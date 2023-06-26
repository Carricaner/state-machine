package org.general.state;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.general.state.StateManager.StateManagerSecBuilder;
import org.general.state.action.Action;
import org.general.state.action.DefaultAction;
import org.general.state.event.Event;
import org.general.state.state.State;

public class Transition<S, E> {
  private final StateManagerSecBuilder<S, E> builder;
  private final State<S> from;
  private Event<S, E> when;
  private Tuple2<State<S>, State<S>> to;
  private Tuple2<Action, Action> actions =
      Tuple.of(new DefaultAction(), new DefaultAction());

  public Transition(StateManagerSecBuilder<S, E> builder, State<S> from) {
    this.builder = builder;
    this.from = from;
  }

  public State<S> getFrom() {
    return from;
  }

  public Event<S, E> getWhen() {
    return when;
  }

  public Tuple2<Tuple2<State<S>, Action>, Tuple2<State<S>, Action>> getToStatesAndActions() {
    return Tuple.of(Tuple.of(to._1, actions._1), Tuple.of(to._2, actions._2));
  }

  public static class When<S, E> {
    private final Transition<S, E> transition;

    public When(Transition<S, E> transition) {
      this.transition = transition;
    }

    public To<S, E> when(Event<S, E> event) {
      this.transition.when = event;
      return new To<>(transition);
    }
  }

  public static class To<S, E> {
    private final Transition<S, E> transition;

    public To(Transition<S, E> transition) {
      this.transition = transition;
    }

    public Act<S, E> to(State<S> successState, State<S> failureState) {
      this.transition.to = new Tuple2<>(successState, failureState);
      return new Act<>(transition);
    }

    public Act<S, E> to(State<S> state) {
      this.transition.to = new Tuple2<>(state, state);
      return new Act<>(transition);
    }
  }

  public static class Act<S, E> {
    private final Transition<S, E> transition;

    public Act(Transition<S, E> transition) {
      this.transition = transition;
    }

    public Act<S, E> andDo(Action successAction) {
      return andDo(successAction, successAction);
    }

    public Act<S, E> andDo(Action successAction, Action failureAction) {
      this.transition.actions = Tuple.of(successAction, failureAction);
      return this;
    }

    public To<S, E> when(Event<S, E> event) {
      this.transition.builder.addTransition(this.transition);
      erase();
      this.transition.when = event;
      return new To<>(transition);
    }

    private void erase() {
      this.transition.when = null;
      this.transition.to = null;
      this.transition.actions = Tuple.of(new DefaultAction(), new DefaultAction());
    }

    public void finished() {
      this.transition.builder.addTransition(this.transition);
    }
  }
}
