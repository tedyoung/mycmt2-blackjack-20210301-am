package com.jitterted.ebp.blackjack.domain.port;

import com.jitterted.ebp.blackjack.domain.Game;

// HEXAGONAL ARCH: PORT INTERFACE
public interface GameMonitor {
  void roundCompleted(Game game);
}
