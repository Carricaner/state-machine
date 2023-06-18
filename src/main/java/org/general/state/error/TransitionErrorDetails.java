package org.general.state.error;

import io.vavr.Tuple2;
import java.util.function.Function;
import org.general.state.Transition;
import org.general.state.event.Event;
import org.general.state.state.State;

public class TransitionErrorDetails {
  public static class TransitionBuildErrorDetail extends ErrorDetail<Transition> {
    public static final String NAME = "Transition build error";

    public TransitionBuildErrorDetail(Transition detail) {
      super(NAME, detail);
    }

    public TransitionBuildErrorDetail(Transition detail,
        Function<Transition, String> function) {
      super(NAME, detail, function);
    }
  }

  public static class TransitionErrorDetail extends ErrorDetail<Tuple2<State, Event>> {
    public static final String NAME = "Transition error";

    public TransitionErrorDetail(String message) {
      super(NAME, message);
    }
  }
}
