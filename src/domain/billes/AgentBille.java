package domain.billes;

import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;

public abstract class AgentBille extends AgentPhysique {
	
	protected Directions direction;
	
	public AgentBille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY);
		this.direction = direction;
	}

	public Directions getDirection() {
		return direction;
	}

	public void setDirection(Directions direction) {
		this.direction = direction;
	}

	public abstract Directions estRencontrePar(AgentBille autre);
}
