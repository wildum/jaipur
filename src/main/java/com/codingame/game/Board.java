package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cards.Camel;
import cards.Card;
import cards.Goods;

public class Board {

	private final int nbCardsFlipped = 5;
	private Deck deck;
	private List<Card> flippedCards;
	
	public Board(Deck deck) {
		this.deck = deck;
		this.flippedCards = new ArrayList<Card>();
	}
	
	public long pickCamels(String[] args) {
		int camels = 0;
		if (args.length - 2  == this.flippedCards.stream().filter(u -> u instanceof Camel).count()) {
			this.flippedCards.removeIf(u -> u instanceof Camel);
			camels = nbCardsFlipped - this.flippedCards.size();
			List<Card> cardsToRefill = cardsRefill(args);
			if (cardsToRefill.size() == args.length - 2) {
				for (Card c : cardsToRefill)
					this.flippedCards.add(c);
			} else {
				// refill error
				for (Card c : cardsToRefill)
					this.deck.getCardsMap().compute(c.getType(), (k, v) -> v++);
				camels = 0;
			}
		} else {
			// not the good number of args
		}
		return camels;
	}
	
	public Goods pickGoods(String[] args) {
		Goods g = null;
		Integer index = Integer.parseInt(args[1]);
		if (index >= 0 && index < nbCardsFlipped) {
			Card c = this.flippedCards.get(index);
			if (c instanceof Goods) {
				if (args.length == 3) {
					Optional<Card> r = cardsRefill(args).stream().findFirst();
					if (r.isPresent()) {
						this.flippedCards.remove(c);
						this.flippedCards.add(r.get());
						g = (Goods) c;
					} else {
						// error can't refill
					}
				} else {
					// not the good number of args
				}
			}
		}
		return g;
	}
	
	public void tradeWithCamels(String[] cardsWanted) {
		if (cardsWanted.length <= camels) {
			tradeCards(cardsWanted);
		} else {
			// not enough camels to trade
		}
	}
	
	public void tradeWithGoods(String[] args) {
		
	}
	
	private void tradeCards(String[] cardsWanted) {
		
	}
	
	public List<Card> cardsRefill(String[] args) {
		List<Card> cardsToRefill = new ArrayList<Card>();
		for (int i = 2; i < args.length; i++) {
			Integer n = this.getDeck().getCardsMap().get(args[i]);
			if (n != null && n != 0) {
				if ("CAMEL".equals(args[i])) {
					cardsToRefill.add(new Camel(args[i]));
				} else {
					cardsToRefill.add(new Goods(args[i]));
				}
				this.getDeck().getCardsMap().compute(args[i], (k, v) -> v--);
			}
		}
		return cardsToRefill;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public List<Card> getFlippedCards() {
		return flippedCards;
	}

	public void setFlippedCards(List<Card> flippedCards) {
		this.flippedCards = flippedCards;
	}
	
}
