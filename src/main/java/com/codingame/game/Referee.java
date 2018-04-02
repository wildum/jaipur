package com.codingame.game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private GameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    public Random random;
    public static Board board;
    
    @Override
    public Properties init(Properties params) {
    	try {
            long seed = Long.valueOf(params.getProperty("seed"));
            random = new Random(seed);
        } catch(Exception e) {
            random = new Random(0);
        }
    	Deck deck = new Deck();
    	deck.fill();
    	
    	Board board = new Board(deck);

        //drawBackground();
        //drawHud();
        //drawGrids();

        gameManager.setFrameDuration(600);
        
        gameManager.setMaxTurns(200);

        return params;
    }
    
    private void setWinner(Player player) {
        gameManager.addToGameSummary(GameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
        player.setScore(10);
        endGame();
    }


    @Override
    public void gameTurn(int turn) {
    	Player player = gameManager.getPlayer(turn % gameManager.getPlayerCount());

        // Temporary fix (will be fixed in the SDK):
        if (turn == 0 || turn == 1) {
            gameManager.setTurnMaxTime(1000);
        } else {
            gameManager.setTurnMaxTime(100);
        }

        //sendInputs(player, validActions);
        player.execute();

        // Read inputs
        try {
            final Action action = player.getAction();
            //gameManager.addToGameSummary(String.format("Player %s played (%d %d) %s", action.player.getNicknameToken(), action.row, action.col, player.message.getText()));
            if (!validActions.contains(action)) {
                throw new InvalidAction("Invalid action.");
            }
//
//
//            validActions = getValidActions();
//            if (validActions.isEmpty()) {
//                endGame();
//            }
        } catch (TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
            player.deactivate(player.getNicknameToken() + " timeout!");
            player.setScore(-1);
            endGame();
        } catch (InvalidAction e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " eliminated: " + e.getMessage()));
            player.deactivate(e.getMessage());
            player.setScore(-1);
            endGame();
        }
    }
    
    private void endGame() {
        gameManager.endGame();

        Player p0 = gameManager.getPlayers().get(0);
        Player p1 = gameManager.getPlayers().get(1);
        if (p0.getScore() > p1.getScore()) {
            //p1.hud.setAlpha(0.3);
        }
        if (p0.getScore() < p1.getScore()) {
            //p0.hud.setAlpha(0.3);
        }
    }
}
