package org.general.state;



public class Main {
  public static void main(String[] args) {
    Coupon coupon = new Coupon();
//    coupon.changeToState(EventOOO.a);
//    coupon.changeToState(EventOOO.c);
//    coupon.printState();
    System.out.println(coupon.getPossibleEvents());
    System.out.println(coupon.getPossibleStates());
  }
}
