package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

  private final Game game;

  @Autowired
  public BlackjackController(Game game) {
    this.game = game;
  }

  @PostMapping("/start-game")
  public String startGame() {
    game.initialDeal();
    return "redirect:/game";
  }

  @GetMapping("/game")
  public String gameView(Model model) {
    populateGameViewInto(model);
    return "blackjack";
  }

  @PostMapping("/hit")
  public String hitCommand() {
    game.playerHits();
    if (game.isPlayerDone()) { // game.currentState: IN_PROGRESS, GAME_OVER, SPLIT, INSURANCE, SURRENDER
      return "redirect:/done";
    }
    return "redirect:/game";
  }

  @GetMapping("/done")
  public String viewDone(Model model) {
    populateGameViewInto(model);
    model.addAttribute("outcome", game.determineOutcome().toString());
    return "done";
  }

  @PostMapping("/stand")
  public String standCommand() {
    game.playerStands();
    return "redirect:/done";
  }

  public void populateGameViewInto(Model model) {
    GameView gameView = GameView.of(game);
    model.addAttribute("gameView", gameView);
  }

}
