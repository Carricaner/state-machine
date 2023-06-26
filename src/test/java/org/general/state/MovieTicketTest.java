package org.general.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.general.state.component.movieticket.MovieTicket;
import org.general.state.component.movieticket.MovieTicketActions.BookFailAction;
import org.general.state.component.movieticket.MovieTicketActions.BookSuccessAction;
import org.general.state.component.movieticket.MovieTicketActions.ReleaseAction;
import org.general.state.component.movieticket.MovieTicketState;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class MovieTicketTest {
  @Test
  @Order(5)
  void test_Transit_From_Script_To_Released() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.DRAFT);

    // Act
    boolean isSuccessful = movieTicket.release();

    // Assert
    assertThat(isSuccessful).isEqualTo(true);
    assertThat(movieTicket)
        .hasFieldOrPropertyWithValue("state", MovieTicketState.RELEASED)
        .hasFieldOrPropertyWithValue("checkString", ReleaseAction.MESSAGE);
  }

  @Test
  @Order(10)
  void test_Transit_From_Released_To_Booked_But_Fail_And_Rollback_To_Released() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.RELEASED);

    // Act
    boolean isSuccessful = movieTicket.book();

    // Assert
    assertThat(isSuccessful).isEqualTo(false);
    assertThat(movieTicket)
        .hasFieldOrPropertyWithValue("state", MovieTicketState.RELEASED)
        .hasFieldOrPropertyWithValue("checkString", BookFailAction.MESSAGE);
  }

  @Test
  @Order(15)
  void test_Transit_From_Released_Successfully() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.RELEASED);

    // Act
    boolean isSuccessful = movieTicket.book();

    // Assert
    assertThat(isSuccessful).isEqualTo(true);
    assertThat(movieTicket)
        .hasFieldOrPropertyWithValue("state", MovieTicketState.BOOKED)
        .hasFieldOrPropertyWithValue("checkString", BookSuccessAction.MESSAGE);
  }

}
