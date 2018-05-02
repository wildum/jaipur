package com.codingame.game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.logging.log4j.CloseableThreadContext.Instance;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.module.entities.Text;

import cards.Goods;
import cards.Camel;

public class Player extends AbstractPlayer {
	
	public List<Goods> hand = new ArrayList<Goods>();
	public List<Camel> camels = new ArrayList<Camel>();
	public Text message;
	
    @Override
    public int getExpectedOutputLines() {
    	return 1;
    }
    
    public void getAction() throws TimeoutException, InvalidAction {
    	
        try {
        	String playerOutput = getOutputs().get(0);
            String[] splittedOutput = playerOutput.split(";", 2);
        	
            // in game message
        	String msg = splittedOutput.length > 1 ? splittedOutput[1] : "";
        	if (msg.length() < 20) message.setText(msg);
        	else message.setText(msg.substring(0, 17) + "...");
        	
        	// player command
        	String[] outputValues = splittedOutput[0].split(" ");
        	String command = outputValues[0];
        	int arguments = outputValues.length;
        	
        	if (command.equals("TAKE") && arguments > 2) {
        		take(outputValues);
        	} else if (command.equals("SELL") && arguments == 2) {
        		sell(outputValues[1]);
            } else if(command.equals("TRADE") && arguments > 2){
            	trade(outputValues);
            } else {
                throw new InvalidAction("Invalid output.");
            }
        } catch (TimeoutException | InvalidAction e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidAction("Invalid output.");
        }
    }
    
    public void take(String[] args) {
    	String[] cards = Arrays.copyOfRange(args, 2, args.length);
    	if ("CAMEL".equals(args[1])) {
    		Referee.board.pickCamels(cards, camels);
    	} else if (hand.size() < 7) {
    		Referee.board.pickGoods(args[1], cards, hand);
    	} else {
    		// take not allowed
    	}
    }
    
    public void sell(String type) {
    	long nbCardsToSell = hand.stream()
    			.filter(c -> c.getType().equals(type) && c instanceof Goods)
    			.count();
    	if (nbCardsToSell > 0) {
    		
    	} else {
    		// sell not allowed
    	}
    }
    
    public void trade(String[] args) {
    	String[] cards = Arrays.copyOfRange(args, 1, args.length);
		if (cards.length%2 == 0 && cards.length <= 10) {
			Referee.board.tradeCards(cards, hand, camels);
		} else {
			// trade not allowed
		}
    }
}
