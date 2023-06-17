package org.general.state.core.practice;

import org.general.state.core.domain.richmenu.richmenu.state.RichMenuState;

public enum MyEnum implements MyInterface<RichMenuState> {
  A {
    @Override
    public boolean pass(RichMenuState dto) {
      return false;
    }

    @Override
    public String toString() {
      return super.toString();
    }
  };

  MyEnum() {}
}
