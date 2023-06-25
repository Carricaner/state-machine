package org.general.state;

import java.util.List;
import org.general.state.action.OneAction;
import org.general.state.action.TwoAction;
import org.general.state.event.Event;
import org.general.state.event.EventOOO;
import org.general.state.state.State;
import org.general.state.state.StateOOO;

public class Coupon implements Stateful<String, Integer> {
  private State<String> state = StateOOO.A;
  private final StateManager<String, Integer> s =
      StateManager.builder(this)
          .from(
              StateOOO.A,
              trans ->
                  trans
                      .when(EventOOO.a)
                      .to(StateOOO.B, StateOOO.C)
                      .andDo(new OneAction(), new TwoAction())
                      .when(EventOOO.b)
                      .to(StateOOO.B, StateOOO.C)
                      .when(EventOOO.c)
                      .to(StateOOO.C, StateOOO.D)
                      .andDo(new OneAction(), new TwoAction())
                      .when(EventOOO.d)
                      .to(StateOOO.D)
                      .finished())
          .from(
              StateOOO.B,
              trans ->
                  trans
                      .when(EventOOO.a)
                      .to(StateOOO.A, StateOOO.C)
                      .andDo(new OneAction(), new TwoAction())
                      .when(EventOOO.b)
                      .to(StateOOO.A, StateOOO.C)
                      .when(EventOOO.c)
                      .to(StateOOO.C, StateOOO.D)
                      .andDo(new OneAction(), new TwoAction())
                      .finished())
          .build();

  public void changeToState(Event<String, Integer> event) {
    s.transfer(state, event);
  }

  public void changeToState(State<String> newState) {
    this.state = newState;
  }

  @Override
  public State<String> getState() {
    return state;
  }

  public List<Event<String, Integer>> getPossibleEvents() {
    return s.getPossibleEvents();
  }

  public List<State<String>> getPossibleStates() {
    return s.getPossibleStates();
  }

  void printState() {
    System.out.println(state);
  }
}
