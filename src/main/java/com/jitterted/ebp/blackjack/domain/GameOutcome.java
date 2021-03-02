package com.jitterted.ebp.blackjack.domain;

public enum GameOutcome {
  PLAYER_BUSTED("You Busted, so you lose.  💸"),
  DEALER_BUSTED("Dealer went BUST, Player wins! Yay for you!! 💵"),
  PLAYER_BEATS_DEALER("You beat the Dealer! 💵"),
  PLAYER_PUSHES("Push: The house wins, you Lose. 💸"),
  PLAYER_LOSES("You lost to the Dealer. 💸"),
  PLAYER_WINS_BLACKJACK("You won Blackjack!");

  private final String display;

  GameOutcome(String display) {
    this.display = display;
  }

  public String display() {
    return display;
  }
}