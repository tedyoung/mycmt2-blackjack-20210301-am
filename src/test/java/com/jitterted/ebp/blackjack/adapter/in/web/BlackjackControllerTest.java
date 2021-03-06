package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameOutcome;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

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

  @Test
  public void gameViewPopulatesViewModel() throws Exception {
    Deck stubDeck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                         new Card(Suit.HEARTS, Rank.TWO),
                                         new Card(Suit.DIAMONDS, Rank.KING),
                                         new Card(Suit.CLUBS, Rank.THREE)));
    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.gameView(model);

    GameView gameView = (GameView) model.getAttribute("gameView");

    assertThat(gameView.getDealerCards())
        .containsExactly("2♥", "3♣");

    assertThat(gameView.getPlayerCards())
        .containsExactly("10♦", "K♦");
  }

  @Test
  public void hitCommandDealsAdditionalCardToPlayer() throws Exception {
    Deck playerHitsDoesNotGoBustDeck = StubDeck.playerHitsDoesNotGoBustDeck();
    Game game = new Game(playerHitsDoesNotGoBustDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String redirectPage = blackjackController.hitCommand();

    assertThat(redirectPage)
        .isEqualTo("redirect:/game");

    assertThat(game.playerHand().cards())
        .hasSize(3);
  }

  @Test
  public void playerHitsGoesBustThenRedirectToDonePage() throws Exception {
    Deck playerHitsGoesBustDeck = StubDeck.createPlayerHitsGoesBustDeck();
    Game game = new Game(playerHitsGoesBustDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String redirectPage = blackjackController.hitCommand();

    assertThat(redirectPage)
        .isEqualTo("redirect:/done");
  }

  @Test
  public void donePageShowsFinalGameViewWithOutcome() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.viewDone(model);

    assertThat(model.containsAttribute("gameView"))
        .isTrue();

    String outcome = (String) model.getAttribute("outcome");

    assertThat(outcome)
        .isNotBlank();
  }

  @Test
  public void standResultsInGamePlayerIsDone() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String redirectPage = blackjackController.standCommand();

    assertThat(redirectPage)
        .isEqualTo("redirect:/done");

    assertThat(game.isPlayerDone())
        .isTrue();
  }

  @Test
  // This test is redundant, the behavior we're checking is contained in the DOMAIN
  public void standResultsInPlayerLosesToDealerWhoDrewAdditionalCard() throws Exception {
    Deck dealerBeatsPlayerAfterDrawingAdditionalCardDeck = new StubDeck(Rank.TEN, Rank.QUEEN,
                                                                        Rank.EIGHT, Rank.FIVE,
                                                                        Rank.SIX);
    Game game = new Game(dealerBeatsPlayerAfterDrawingAdditionalCardDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    blackjackController.standCommand();

    assertThat(game.determineOutcome())
        .isEqualByComparingTo(GameOutcome.PLAYER_LOSES);
  }

}