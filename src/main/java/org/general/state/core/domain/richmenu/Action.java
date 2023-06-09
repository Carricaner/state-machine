package org.general.state.core.domain.richmenu;

/** An action to be executed when transiting between status */
@FunctionalInterface
public interface Action {

  void act();
}
