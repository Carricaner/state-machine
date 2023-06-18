package org.general.state.core.practice.exception;

public class StateMachineException extends RuntimeException {
  public StateMachineException(ErrorDetail<?> detail) {
    super(detail.getOverallMessage());
  }
}
