package org.general.state.core.domain.richmenu.richmenu;

import org.general.state.core.domain.richmenu.RichMenuPublishAction;
import org.general.state.core.domain.richmenu.RichMenuEvent;
import org.general.state.core.domain.richmenu.richmenu.state.RichMenuState;
import org.general.state.core.domain.richmenu.StateManager;

public class RichMenu {
  private final StateManager<RichMenuState, RichMenuEvent> stateManager =
      new StateManager<RichMenuState, RichMenuEvent>()
          .start()
          .from(RichMenuState.DRAFT, when ->
              when.when(RichMenuEvent.PUBLISH)
              .to(RichMenuState.PUBLISHED)
              .andDo(new RichMenuPublishAction())
              .when(RichMenuEvent.SCHEDULE)
              .to(RichMenuState.SCHEDULED)
              .build())
          .from(RichMenuState.PUBLISHED, when ->
              when.when(RichMenuEvent.HALT)
              .to(RichMenuState.HALTED)
              .build())
          .from(RichMenuState.SCHEDULED, when ->
              when.when(RichMenuEvent.HALT)
              .to(RichMenuState.HALTED)
              .when(RichMenuEvent.FINISH)
              .to(RichMenuState.FINISHED)
              .build())
          .from(RichMenuState.HALTED, when ->
              when.when(RichMenuEvent.PUBLISH)
              .to(RichMenuState.PUBLISHED)
              .when(RichMenuEvent.SCHEDULE)
              .to(RichMenuState.SCHEDULED)
              .when(RichMenuEvent.DELETE)
              .to(RichMenuState.DELETED)
              .build())
          .from(RichMenuState.FINISHED, when ->
              when.when(RichMenuEvent.PUBLISH)
              .to(RichMenuState.PUBLISHED)
              .when(RichMenuEvent.SCHEDULE)
              .to(RichMenuState.SCHEDULED)
              .when(RichMenuEvent.DELETE)
              .to(RichMenuState.DELETED)
              .build())
          .end();
  private RichMenuState state;

  public RichMenu() {
    state = RichMenuState.DRAFT;
  }

  public void publish() {
    state = stateManager.on(state, RichMenuEvent.PUBLISH);
  }

  public void halt() {
    state = stateManager.on(state, RichMenuEvent.HALT);
  }

  public void schedule() {
    state = stateManager.on(state, RichMenuEvent.SCHEDULE);
  }

  public void finish() {
    state = stateManager.on(state, RichMenuEvent.FINISH);
  }

  public void delete() {
    state = stateManager.on(state, RichMenuEvent.DELETE);
  }

  public RichMenuState getState() {
    return state;
  }
}
