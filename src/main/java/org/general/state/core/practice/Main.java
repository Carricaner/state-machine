package org.general.state.core.practice;

import org.general.state.core.practice.action.OneAction;
import org.general.state.core.practice.action.TwoAction;
import org.general.state.core.practice.event.Event;
import org.general.state.core.practice.state.State;

public class Main {
  public static void main(String[] args) {
    StateManagerSec s =
        StateManagerSec.builder()
            .from(
                State.A,
                trans ->
                    trans
                        .when(Event.a)
                        .to(State.B, State.C)
                        .andDo(new OneAction(), new TwoAction())
                        .when(Event.b)
                        .to(State.B, State.C)
                        .when(Event.c)
                        .to(State.C, State.D)
                        .andDo(new OneAction(), new TwoAction())
                        .finished())
            .from(
                State.B,
                trans ->
                    trans
                        .when(Event.a)
                        .to(State.A, State.C)
                        .andDo(new OneAction(), new TwoAction())
                        .when(Event.b)
                        .to(State.A, State.C)
                        .when(Event.c)
                        .to(State.C, State.D)
                        .andDo(new OneAction(), new TwoAction())
                        .finished())
            .build();

    System.out.println(s);
  }
}
