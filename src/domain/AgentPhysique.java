package domain;

import java.awt.Graphics;

public abstract class AgentPhysique extends Agent {

	protected static int taille = 5;
	protected int posX;
	protected int posY;

	public AgentPhysique(Environnement env, SMA sma, int posX, int posY) {
		super(env, sma);
		this.posX = posX;
		this.posY = posY;
	}

	public static int getTaille() {
		return taille;
	}

	public static void setTaille(int rayon) {
		AgentPhysique.taille = rayon;
	}
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	@Override
	public boolean isPhysique() {
		return true;
	}
	
	public abstract void dessine(Graphics g);
}
