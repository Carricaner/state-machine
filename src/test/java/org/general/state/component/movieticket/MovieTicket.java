package org.general.state.component.movieticket;

import io.vavr.Tuple2;
import java.util.List;
import org.general.state.StateManager;
import org.general.state.Stateful;
import org.general.state.component.movieticket.MovieTicketActions.BookFailAction;
import org.general.state.component.movieticket.MovieTicketActions.BookSuccessAction;
import org.general.state.component.movieticket.MovieTicketActions.DeleteAction;
import org.general.state.component.movieticket.MovieTicketActions.RedeemAction;
import org.general.state.component.movieticket.MovieTicketActions.ReleaseAction;
import org.general.state.event.Event;
import org.general.state.state.State;

public class MovieTicket implements Stateful<String, Integer> {
  private final StateManager<String, Integer> s =
      StateManager.builder(this)
          .from(
              MovieTicketState.DRAFT,
              transition ->
                  transition
                      .when(MovieTicketEvent.RELEASE)
                      .to(MovieTicketState.RELEASED)
                      .andDo(new ReleaseAction(this))
                      .when(MovieTicketEvent.DELETE)
                      .to(MovieTicketState.DELETED)
                      .andDo(new DeleteAction(this))
                      .finished())
          .from(
              MovieTicketState.RELEASED,
              transition ->
                  transition
                      .when(MovieTicketEvent.BOOK)
                      .to(MovieTicketState.BOOKED, MovieTicketState.RELEASED)
                      .andDo(new BookSuccessAction(this), new BookFailAction(this))
                      .when(MovieTicketEvent.DELETE)
                      .to(MovieTicketState.DELETED)
                      .andDo(new DeleteAction(this))
                      .finished())
          .from(
              MovieTicketState.BOOKED,
              transition ->
                  transition
                      .when(MovieTicketEvent.REDEEM)
                      .to(MovieTicketState.REDEEMED)
                      .andDo(new RedeemAction(this))
                      .when(MovieTicketEvent.DELETE)
                      .to(MovieTicketState.DELETED)
                      .andDo(new DeleteAction(this))
                      .finished())
          .build();

  private State<String> state = MovieTicketState.DRAFT;

  private String checkString = "";

  public MovieTicket(State<String> state) {
    this.state = state;
  }

  public boolean release() {
    Tuple2<Boolean, State<String>> tuple = s.transfer(state, MovieTicketEvent.RELEASE);
    state = tuple._2;
    return tuple._1;
  }

  public boolean book() {
    Tuple2<Boolean, State<String>> tuple = s.transfer(state, MovieTicketEvent.BOOK);
    state = tuple._2;
    return tuple._1;
  }

  public boolean redeem() {
    Tuple2<Boolean, State<String>> tuple = s.transfer(state, MovieTicketEvent.REDEEM);
    state = tuple._2;
    return tuple._1;
  }

  public boolean delete() {
    Tuple2<Boolean, State<String>> tuple = s.transfer(state, MovieTicketEvent.DELETE);
    state = tuple._2;
    return tuple._1;
  }

  @Override
  public State<String> getState() {
    return state;
  }

  @Override
  public List<Event<String, Integer>> getPossibleEvents() {
    return s.getPossibleEvents();
  }

  @Override
  public List<State<String>> getPossibleStates() {
    return s.getPossibleStates();
  }

  public String getCheckString() {
    return checkString;
  }

  public void setCheckString(String checkString) {
    this.checkString = checkString;
  }
}
