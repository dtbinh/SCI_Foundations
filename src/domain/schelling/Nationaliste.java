package domain.schelling;

import java.awt.Color;
import java.awt.Graphics;

import outils.Randomizer;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;

public class Nationaliste extends AgentPhysique {

	private float tolerance;
	private Nationalite nationalite;
	private boolean insatisfait = false;
	private float ratioVoisinsSympas = 0;

	public enum Nationalite {
		ROUGE(Color.RED), VERT(Color.GREEN);

		private Color couleur;

		Nationalite(Color couleur) {
			this.couleur = couleur;
		}
	}

	public Nationaliste(Environnement env, SMA sma, int posX, int posY, float tolerance,
			Nationalite nationalite) {
		super(env, sma, posX, posY);
		this.tolerance = tolerance;
		this.nationalite = nationalite;
	}

	public float getTolerance() {
		return tolerance;
	}

	public float getRatioVoisinsSympas() {
		return ratioVoisinsSympas;
	}

	public boolean isInsatisfait() {
		return this.insatisfait;
	}

	@Override
	public void decide() {
		AgentPhysique[][] locations = env.getLocations();
		ratioVoisinsSympas = visiteVoisins(locations);
		insatisfait = ratioVoisinsSympas < tolerance;
		if (insatisfait) {
			this.move(locations);
		}
	}

	private float visiteVoisins(AgentPhysique[][] locations) {
		float nbVoisins = 0;
		int nbVoisinsSympas = 0;
		for (int i = ((this.posY - 1) < 0 ? 0 : this.posY - 1); i <= this.posY + 1
				&& i < locations.length; i++) {
			for (int j = ((this.posX - 1) < 0 ? 0 : this.posX - 1); j <= this.posX + 1
					&& j < locations[0].length; j++) {
				AgentPhysique agentPresent = locations[i][j];
				if (agentPresent != null && agentPresent != this) {
					if (((Nationaliste) agentPresent).nationalite
							.equals(this.nationalite)) {
						nbVoisinsSympas++;
					}
					nbVoisins++;
				}
			}
		} 
//		System.out.print(nbVoisins + " dont " + nbVoisinsSympas + " = " + nbVoisinsSympas / nbVoisins);
		return (nbVoisins == 0 ? 100 : nbVoisinsSympas / nbVoisins * 100);
	}

	private void move(AgentPhysique[][] locations) {
		int nextPosX;
		int nextPosY;
		do {
			nextPosX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
			nextPosY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
		} while (locations[nextPosY][nextPosX] != null);

		locations[posY][posX] = null;
		posX = nextPosX;
		posY = nextPosY;
		locations[posY][posX] = this;
	}

	@Override
	public void dessine(Graphics g) {
		g.setColor(this.nationalite.couleur);
		g.fillOval(posX * taille, posY * taille, taille, taille);
	}
}
