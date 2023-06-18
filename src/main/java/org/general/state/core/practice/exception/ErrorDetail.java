package org.general.state.core.practice.exception;

import java.util.function.Function;

public class ErrorDetail<T> {
  private static final long serialVersionUID = 1L;

  private final String name;
  private final String message;

  public ErrorDetail(String name, String message) {
    this.name = name;
    this.message = message;
  }

  public ErrorDetail(String name, T detail) {
    this.name = name;
    this.message = detail.toString();
  }

  public ErrorDetail(String name, T detail, Function<T, String> function) {
    this.name = name;
    this.message = function.apply(detail);
  }

  public String getOverallMessage() {
    return name + " -- " + message;
  }
}
