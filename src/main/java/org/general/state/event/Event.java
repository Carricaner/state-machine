package org.general.state.event;

public interface Event<S, E> {
  boolean succeed();
}
