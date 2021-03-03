package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

  @Test
  public void startGameResultsInCardsDealt() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);

    String redirectPage = blackjackController.startGame();

    assertThat(redirectPage)
        .isEqualTo("redirect:/game");

    assertThat(game.playerHand().cards())
        .hasSize(2);
  }

}