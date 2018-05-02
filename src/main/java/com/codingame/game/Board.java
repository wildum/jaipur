package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	
	public void pickCamels(String[] cards, List<Camel> camels) {
		List<Camel> boardCamels = this.flippedCards.stream().filter(u -> u instanceof Camel).map(u -> (Camel) u).collect(Collectors.toList());
		if (cards.length  == boardCamels.size()) {
			List<Card> cardsToRefill = cardsRefill(cards);
			if (cardsToRefill != null) {
				this.flippedCards.removeIf(u -> u instanceof Camel);
				this.flippedCards.addAll(cardsToRefill);
			} else {
				// error refill
			}
		} else {
			// not the good number of cards
		}
	}
	
	public void pickGoods(String choice, String[] cards, List<Goods> hand) {
		Optional<Card> oc = this.flippedCards.stream().filter(u -> u.getType().equals(choice)).findFirst();
		if (oc.isPresent()) {
			Card c = oc.get();
			if (c instanceof Goods) {
				if (cards.length == 1) {
					Optional<Card> r = cardsRefill(cards).stream().findFirst();
					if (r.isPresent()) {
						this.flippedCards.remove(c);
						this.flippedCards.add(r.get());
						hand.add((Goods) c);
					} else {
						// error can't refill
					}
				} else {
					// not the good number of args
				}
			} else {
				// is not a goods
			}
		} else {
			// card type not in the list
		}
	}
	
	/*
	 * - trade avec les types
	 * - set de l'un dans l'autre -> pas d'intersection!
	 * - check la taille du deck est bien 6
	 */
	public void tradeCards(String[] cards, List<Goods> hand, List<Camel> camels) {
		List<String> cardsOut = Arrays.asList(Arrays.copyOfRange(cards, 0, cards.length/2));
		List<String> cardsIn = Arrays.asList(Arrays.copyOfRange(cards, cards.length/2, cards.length));
		
		List<String> intersection = new ArrayList<>(cardsOut);
		intersection.retainAll(cardsIn);
		if (intersection.isEmpty()) {
			if (allowMoveWithHand(cardsOut, hand, camels) && allowMoveWithDeck(cardsIn)) {
				if (hand.size() - cardsOut.size() + cardsOut.stream().filter(c -> c.equals("CAMEL")).count() + cardsIn.size() - cardsIn.stream().filter(c -> c.equals("CAMEL")).count() <= 7) {
					
				} else {
					// hand size overflow
				}
			} else {
				// invalid cards
			}
		} else {
			// can't trade the same type of card
		}
	}
	
	public List<Card> cardsRefill(String[] args) {
		List<Card> cardsToRefill = new ArrayList<Card>();
		List<String> cards = Arrays.asList(args);
		if(!allowMoveWithDeck(cards)) return null;
		for (String type : cards) {
			if ("CAMEL".equals(type)) {
				cardsToRefill.add(new Camel(type, Card.nextId++));
			} else {
				cardsToRefill.add(new Goods(type, Card.nextId++));
			}
			this.getDeck().getCardsMap().compute(type, (k, v) -> v--);
		}
		return cardsToRefill;
	}
	
	public boolean allowMoveWithDeck(List<String> cardsInit) {
		List<String> cards = new ArrayList<>(cardsInit);
		while (!cards.isEmpty()) {
			String t = cards.get(0);
			int prevSize = cards.size();
			cards = cards.stream().filter(u -> !u.equals(t)).collect(Collectors.toList());
			Integer n = this.getDeck().getCardsMap().get(t);
			if (n == null || n < prevSize - cards.size()) {
				// error cards not available
				return false;
			}
		}
		return true;
	}
	
	public boolean allowMoveWithHand(List<String> cardsInit, List<Goods> hand, List<Camel> camels) {
		List<String> handString = hand.stream().map(u -> u.getType()).collect(Collectors.toList());
		List<String> cards = new ArrayList<>(cardsInit);
		int nbCamels = camels.size();
		while (!cards.isEmpty()) {
			String t = cards.get(0);
			if ("CAMEL".equals(t)) {
				if (--nbCamels < 0)
					return false;
			} else {
				int prevSize = cards.size();
				cards = cards.stream().filter(u -> !u.equals(t)).collect(Collectors.toList());
				Integer n = Collections.frequency(handString, t);
				if (n < prevSize - cards.size()) {
					// error cards not available
					return false;
				}
			}
		}
		return true;
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
