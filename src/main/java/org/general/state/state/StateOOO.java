package org.general.state.state;

import java.util.Map;
import java.util.Optional;

public enum StateOOO implements State<String> {
  A("A"),
  B("B"),
  C("C"),
  D("D"),
  E("E");

  private final String name;
  private static final Map<String, StateOOO> map = Map.ofEntries(
      Map.entry("A", A),
      Map.entry("B", B),
      Map.entry("C", C),
      Map.entry("D", D),
      Map.entry("E", E)
  );

  StateOOO(String name) {
    this.name = name;
  }

  @Override
  public String getStateInfo() {
    return name;
  }

  public static Optional<StateOOO> fromName(String name) {
    return Optional.ofNullable(map.get(name));
  }
}
