package cards;

import java.security.IdentityScope;

public abstract class Card {
	
	public static int nextId = 2;
	
	protected String type;
	protected int id;
	
	public Card(String type, int id) {
		this.type = type;
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
