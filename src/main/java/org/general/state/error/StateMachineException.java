package org.general.state.error;

public class StateMachineException extends RuntimeException {

  private final ErrorDetail<?> detail;

  public StateMachineException(ErrorDetail<?> detail) {
    super(detail.getOverallMessage());
    this.detail = detail;
  }
}
