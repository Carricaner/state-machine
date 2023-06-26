package org.general.state.component.movieticket;

import org.general.state.action.Action;

public class MovieTicketActions {
  public static class ReleaseAction implements Action {
    public static final String MESSAGE = "Released!";
    private final MovieTicket movieTicket;

    public ReleaseAction(MovieTicket movieTicket) {
      this.movieTicket = movieTicket;
    }

    @Override
    public void run() {
      movieTicket.setCheckString(MESSAGE);
    }
  }

  public static class DeleteAction implements Action {
    public static final String MESSAGE = "Deleted.";
    private final MovieTicket movieTicket;

    public DeleteAction(MovieTicket movieTicket) {
      this.movieTicket = movieTicket;
    }

    @Override
    public void run() {
      movieTicket.setCheckString(MESSAGE);
    }
  }

  public static class BookSuccessAction implements Action {
    public static final String MESSAGE = "Successfully booked!";
    private final MovieTicket movieTicket;

    public BookSuccessAction(MovieTicket movieTicket) {
      this.movieTicket = movieTicket;
    }

    @Override
    public void run() {
      movieTicket.setCheckString(MESSAGE);
    }
  }

  public static class BookFailAction implements Action {
    public static final String MESSAGE = "Booking failed...";
    private final MovieTicket movieTicket;

    public BookFailAction(MovieTicket movieTicket) {
      this.movieTicket = movieTicket;
    }

    @Override
    public void run() {
      movieTicket.setCheckString(MESSAGE);
    }
  }

  public static class RedeemAction implements Action {
    public static final String MESSAGE = "Redeem!";
    private final MovieTicket movieTicket;

    public RedeemAction(MovieTicket movieTicket) {
      this.movieTicket = movieTicket;
    }

    @Override
    public void run() {
      movieTicket.setCheckString(MESSAGE);
    }
  }
}
