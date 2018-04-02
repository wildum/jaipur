package com.codingame.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cards.Camel;
import cards.Card;
import cards.Goods;

public class Deck {
	private int cardsNumber = 55;
	private Map<String, Integer> cardsMap;
	
	public Deck () {
		this.cardsMap = new HashMap<String, Integer>();
	}
	
	// TODO: random
	public void fill() {

		int nbCamels = 11;
		int [] proportions = {6, 6, 6, 8, 8, 10};
		String [] types = {"DIAMONDS", "GOLD", "SILVER", "CLOTH", "SPICE", "LEATHER"};
		
		cardsMap.put("CAMEL", nbCamels);
		for (int i = 0; i < proportions.length; i++) {
			cardsMap.put(types[i], proportions[i]);
		}
	}

	public Map<String, Integer> getCardsMap() {
		return cardsMap;
	}

	public void setCardsMap(Map<String, Integer> cardsMap) {
		this.cardsMap = cardsMap;
	}

	public int getCapacity() {
		return cardsNumber;
	}
}
