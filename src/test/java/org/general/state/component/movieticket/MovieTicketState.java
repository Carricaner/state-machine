package org.general.state.component.movieticket;

import java.util.Map;
import java.util.Optional;
import org.general.state.state.State;

public enum MovieTicketState implements State<String> {
  DRAFT("draft"),
  RELEASED("released"),
  LOCKED("locked"),
  BOOKED("booked"),
  DELETED("deleted"),
  REDEEMED("redeemed");

  private static final Map<String, MovieTicketState> map =
      Map.ofEntries(
          Map.entry("draft", DRAFT),
          Map.entry("released", RELEASED),
          Map.entry("locked", LOCKED),
          Map.entry("booked", BOOKED),
          Map.entry("deleted", DELETED),
          Map.entry("redeemed", REDEEMED));
  private final String name;

  MovieTicketState(String name) {
    this.name = name;
  }

  @Override
  public String getStateInfo() {
    return name;
  }

  public static Optional<MovieTicketState> fromName(String name) {
    return Optional.ofNullable(map.get(name));
  }
}
