package org.general.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.general.state.component.movieticket.MovieTicket;
import org.general.state.component.movieticket.MovieTicketActions.BookFailAction;
import org.general.state.component.movieticket.MovieTicketActions.BookSuccessAction;
import org.general.state.component.movieticket.MovieTicketActions.DeleteAction;
import org.general.state.component.movieticket.MovieTicketActions.RedeemAction;
import org.general.state.component.movieticket.MovieTicketActions.ReleaseAction;
import org.general.state.component.movieticket.MovieTicketState;
import org.general.state.error.StateMachineException;
import org.general.state.error.TransitionErrorDetails.TransitionErrorDetail;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class MovieTicketTest {
  @Test
  @Order(5)
  void test_Transit_From_Script_To_Released_Successfully() {
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
  void test_Transit_From_Released_To_Booked_Successfully() {
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

  @Test
  @Order(20)
  void test_Transit_From_Booked_To_Redeemed_Successfully() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.BOOKED);

    // Act
    boolean isSuccessful = movieTicket.redeem();

    // Assert
    assertThat(isSuccessful).isEqualTo(true);
    assertThat(movieTicket)
        .hasFieldOrPropertyWithValue("state", MovieTicketState.REDEEMED)
        .hasFieldOrPropertyWithValue("checkString", RedeemAction.MESSAGE);
  }

  @Test
  @Order(25)
  void test_Transit_From_Booked_To_Deleted_Successfully() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.BOOKED);

    // Act
    boolean isSuccessful = movieTicket.delete();

    // Assert
    assertThat(isSuccessful).isEqualTo(true);
    assertThat(movieTicket)
        .hasFieldOrPropertyWithValue("state", MovieTicketState.DELETED)
        .hasFieldOrPropertyWithValue("checkString", DeleteAction.MESSAGE);
  }

  @Test
  void test_Transit_Error_Due_To_Invalid_Event() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.DRAFT);

    // Act & Assert
    assertThatThrownBy(movieTicket::book)
        .isInstanceOf(StateMachineException.class)
        .extracting("detail")
        .isInstanceOf(TransitionErrorDetail.class);
  }

  @Test
  void test_Transit_Error_Due_To_Invalid_Starting_State() {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(MovieTicketState.DELETED);

    // Act & Assert
    assertThatThrownBy(movieTicket::release)
        .isInstanceOf(StateMachineException.class)
        .extracting("detail")
        .isInstanceOf(TransitionErrorDetail.class);
  }
}
