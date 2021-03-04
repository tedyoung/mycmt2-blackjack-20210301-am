package com.jitterted.ebp.blackjack.domain.port;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class GameMonitorTest {

  @Test
  public void playerStandsCompletesGameSendsResultToMonitor() throws Exception {
    // creates the spy based on the interface
    GameMonitor gameMonitorSpy = spy(GameMonitor.class);
    Game game = new Game(new Deck(), gameMonitorSpy);
    game.initialDeal();

    // when player stands
    game.playerStands();

    // verify that the roundCompleted method was called with any instance of a Game class
    verify(gameMonitorSpy).roundCompleted(any(Game.class));
  }

  @Test
  public void playerHitsGoesBustThenGameSendsResultToMonitor() throws Exception {
    Deck playerHitsGoesBustDeck = StubDeck.createPlayerHitsGoesBustDeck();
    GameMonitor gameMonitorSpy = spy(GameMonitor.class);
    Game game = new Game(playerHitsGoesBustDeck, gameMonitorSpy);
    game.initialDeal();

    game.playerHits(); // they'll go bust

    verify(gameMonitorSpy).roundCompleted(any(Game.class));
  }

  @Test
  public void playerHitsDoesNotBustThenNoResultSentToMonitor() throws Exception {
    Deck playerHitsDoesNotGoBustDeck = StubDeck.playerHitsDoesNotGoBustDeck();
    GameMonitor gameMonitorSpy = spy(GameMonitor.class);
    Game game = new Game(playerHitsDoesNotGoBustDeck, gameMonitorSpy);
    game.initialDeal();

    game.playerHits(); // does not bust

    verify(gameMonitorSpy, never()).roundCompleted(any());
  }

}