package org.general.state.core.practice.exception;

import java.util.function.Function;
import org.general.state.core.practice.TransitionII;

public class TransitionErrorDetails {
  public static class TransitionBuildErrorDetail extends ErrorDetail<TransitionII> {
    public static final String message = "Transition build error";

    public TransitionBuildErrorDetail(TransitionII detail) {
      super(message, detail);
    }

    public TransitionBuildErrorDetail(TransitionII detail,
        Function<TransitionII, String> function) {
      super(message, detail, function);
    }
  }
}
