package org.general.state.error;

public class StateMachineException extends RuntimeException {
  public StateMachineException(ErrorDetail<?> detail) {
    super(detail.getOverallMessage());
  }
}
