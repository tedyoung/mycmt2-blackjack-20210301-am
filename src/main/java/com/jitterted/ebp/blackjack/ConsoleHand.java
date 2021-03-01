package com.jitterted.ebp.blackjack;

public class ConsoleHand {

  static String displayFirstCard(Hand hand) { // DOMAIN (Hand) --> Adapter (String)
    return ConsoleCard.display(hand.cards().get(0));
  }

}
