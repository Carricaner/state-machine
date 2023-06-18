package org.general.state;


import org.general.state.event.EventOOO;

public class Main {
  public static void main(String[] args) {
    Coupon coupon = new Coupon();
    coupon.changeToState(EventOOO.a);
    coupon.changeToState(EventOOO.c);
    coupon.printState();
  }
}
