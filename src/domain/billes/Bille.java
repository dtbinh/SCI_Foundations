package domain.billes;

import java.awt.Color;
import java.awt.Graphics;

import outils.Randomizer;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;

public class Bille extends AgentBille {

	private Color couleur;

	public Bille(Environnement env, SMA sma, int posX, int posY, Directions direction) {
		super(env, sma, posX, posY, direction);
		int red = Randomizer.randomGenerator.nextInt(255);
		int green = Randomizer.randomGenerator.nextInt(255);
		int blue = Randomizer.randomGenerator.nextInt(255);
		this.couleur = new Color(red, green, blue);
	}

	public void decide() {
		int nextPosX = posX + direction.getDirX();
		int nextPosY = posY + direction.getDirY();
		AgentPhysique[][] locations = env.getLocations();
		AgentBille agentPresent = (AgentBille) locations[nextPosY][nextPosX];
		if (agentPresent != null) {
			this.direction = agentPresent.estRencontrePar(this);
			return;
		}

		// TODO for loop verifier si on sait bouger 8 fois

		locations[posY][posX] = null;
		posX = nextPosX;
		posY = nextPosY;
		locations[posY][posX] = this;
	}

	@Override
	public void dessine(Graphics g) {
		g.setColor(couleur);
		g.fillOval(posX * taille, posY * taille, taille, taille);
	}

	@Override
	public Directions estRencontrePar(AgentBille autre) {
		Directions maDir = this.direction;
		this.direction = autre.getDirection();
		return maDir;
	}

}