package org.general.state;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.general.state.action.Action;
import org.general.state.event.Event;
import org.general.state.state.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StateManagerTest {

  @BeforeAll
  static void before_All() {
    System.out.println("Before all@");
  }

  @Test
  void test() {
    System.out.println("Here's a simple test!");
  }



  private static class Coupon implements Stateful<String, Integer> {
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
    private State<String> state = StateOOO.A;
  
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

  private enum StateOOO implements State<String> {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E");

    private final String name;
    private static final Map<String, StateOOO> map = Map.ofEntries(
        Map.entry("A", A),
        Map.entry("B", B),
        Map.entry("C", C),
        Map.entry("D", D),
        Map.entry("E", E)
    );

    StateOOO(String name) {
      this.name = name;
    }

    @Override
    public String getStateInfo() {
      return name;
    }

    public static Optional<StateOOO> fromName(String name) {
      return Optional.ofNullable(map.get(name));
    }
  }

  public enum EventOOO implements Event<String, Integer> {
    a,b,c,e,d;

    @Override
    public boolean succeed(Stateful<String, Integer> stateful) {
      return true;
    }
  }

  private static class OneAction implements Action {

    @Override
    public void run() {

    }
  }

  private static class TwoAction implements Action {

    @Override
    public void run() {

    }
  }
}
