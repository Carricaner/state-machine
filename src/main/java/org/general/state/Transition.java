package org.general.state;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.general.state.StateManager.StateManagerSecBuilder;
import org.general.state.action.Action;
import org.general.state.action.DefaultAction;
import org.general.state.event.Event;
import org.general.state.state.State;

public class Transition {
  private final StateManagerSecBuilder builder;
  private final State from;
  private Event when;
  private Tuple2<State, State> to;
  private Tuple2<Action, Action> actions =
      Tuple.of(new DefaultAction(), new DefaultAction());

  public Transition(StateManagerSecBuilder builder, State from) {
    this.builder = builder;
    this.from = from;
  }

  public State getFrom() {
    return from;
  }

  public Event getWhen() {
    return when;
  }

  public Tuple2<Tuple2<State, Action>, Tuple2<State, Action>> getToStatesAndActions() {
    return Tuple.of(Tuple.of(to._1, actions._1), Tuple.of(to._2, actions._2));
  }

  public static class When {
    private final Transition transition;

    public When(Transition transition) {
      this.transition = transition;
    }

    public To when(Event event) {
      this.transition.when = event;
      return new To(transition);
    }
  }

  public static class To {
    private final Transition transition;

    public To(Transition transition) {
      this.transition = transition;
    }

    public Act to(State successState, State failureState) {
      this.transition.to = new Tuple2<>(successState, failureState);
      return new Act(transition);
    }

    public Act to(State state) {
      this.transition.to = new Tuple2<>(state, state);
      return new Act(transition);
    }
  }

  public static class Act {
    private final Transition transition;

    public Act(Transition transition) {
      this.transition = transition;
    }

    public Act andDo(Action successAction, Action failureAction) {
      this.transition.actions = Tuple.of(successAction, failureAction);
      return this;
    }

    public To when(Event event) {
      this.transition.builder.addTransition(this.transition);
      erase();
      this.transition.when = event;
      return new To(transition);
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
