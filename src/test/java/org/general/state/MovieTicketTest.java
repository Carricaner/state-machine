package org.general.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.general.state.component.movieticket.MovieTicket;
import org.general.state.component.movieticket.MovieTicketActions.BookFailAction;
import org.general.state.component.movieticket.MovieTicketActions.BookSuccessAction;
import org.general.state.component.movieticket.MovieTicketActions.DeleteAction;
import org.general.state.component.movieticket.MovieTicketActions.RedeemAction;
import org.general.state.component.movieticket.MovieTicketActions.ReleaseAction;
import org.general.state.component.movieticket.MovieTicketEvent;
import org.general.state.component.movieticket.MovieTicketState;
import org.general.state.error.StateMachineException;
import org.general.state.error.TransitionErrorDetails.TransitionBuildErrorDetail;
import org.general.state.error.TransitionErrorDetails.TransitionErrorDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

class MovieTicketTest {
  @Test
  @DisplayName("Scripted --|Release(O)|--> Released")
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
  @DisplayName("Released --|Book(X)|--> Released")
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
  @DisplayName("Released --|Book(O)|--> Released")
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
  @DisplayName("Booked --|Redeem(O)|--> Redeemed")
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

  @ParameterizedTest
  @EnumSource(
      value = MovieTicketState.class,
      names = {"DRAFT", "RELEASED", "BOOKED"})
  @DisplayName("Certain State --|Delete(O)|--> Deleted")
  void test_Transit_From_Certain_State_To_Deleted_Successfully(MovieTicketState state) {
    // Arrange
    MovieTicket movieTicket = new MovieTicket(state);

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

  @Test
  void test_Build_State_Manager_But_Fail_Due_To_Repeated_Event_Under_The_Same_From_State() {
    // Arrange
    MovieTicket mockMovieTicket = Mockito.mock(MovieTicket.class);

    assertThatThrownBy(
            () ->
                StateManager.builder(mockMovieTicket)
                    .from(
                        MovieTicketState.DRAFT,
                        transition ->
                            transition
                                .when(MovieTicketEvent.RELEASE)
                                .to(MovieTicketState.RELEASED)
                                .when(MovieTicketEvent.RELEASE)
                                .to(MovieTicketState.RELEASED)
                                .finished())
                    .build())
        .isInstanceOf(StateMachineException.class)
        .extracting("detail")
        .isInstanceOf(TransitionBuildErrorDetail.class);
  }
}
