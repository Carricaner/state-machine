package org.example.state;

/** An action to be executed when transiting between status */
@FunctionalInterface
public interface Action {

  void act();
}
