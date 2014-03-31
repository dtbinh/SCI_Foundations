package domain.wator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import outils.Randomizer;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;

public class Animal extends AgentPhysique {

	private Race race;
	private int age;
	private int joursDeJeune;

	public enum Race {
		REQUIN(Color.RED, 10, 3), POISSON(Color.BLUE, 2, Integer.MAX_VALUE);

		private Color couleur;
		private int ageReproduction;
		private int nbJoursSubsistance;
		private Set<Race> proies;

		Race(Color couleur, int ageReproduction, int nbJoursSubsistance) {
			this.couleur = couleur;
			this.ageReproduction = ageReproduction;
			this.nbJoursSubsistance = nbJoursSubsistance;
			this.proies = new HashSet<Race>();
		}

		public Set<Race> getProies() {
			return proies;
		}

		public void setProies(Set<Race> proies) {
			this.proies = proies;
		}

		public void setAgeReproduction(int ageReproduction) {
			this.ageReproduction = ageReproduction;
		}

		public void setNbJoursSubsistance(int nbJoursSubsistance) {
			this.nbJoursSubsistance = nbJoursSubsistance;
		}

		public Color getCouleur() {
			return couleur;
		}
	}

	public Animal(Environnement env, SMA sma, int posX, int posY, Race race) {
		super(env, sma, posX, posY);
		this.race = race;
		this.age = Randomizer.randomGenerator.nextInt(this.race.ageReproduction);
		this.joursDeJeune = 0;
	}

	public Set<Race> getProies() {
		return this.race.proies;
	}

	public Race getRace() {
		return race;
	}

	@Override
	public void decide() {
		AgentPhysique[][] locations = env.getLocations();

		// Calcul du déplacement
		int nextPosXPotentielle, nextPosX = posX;
		int nextPosYPotentielle, nextPosY = posY;
		Animal animalPresent;
		boolean repus = false;
		for (int i = 0; i < 8; i++) {
			nextPosXPotentielle = posX
					+ (Randomizer.randomGenerator.nextInt(3) - 1);
			nextPosXPotentielle = (nextPosXPotentielle < 0 ? locations[0].length - 1
					: nextPosXPotentielle % locations[0].length);
			nextPosYPotentielle = posY
					+ (Randomizer.randomGenerator.nextInt(3) - 1);
			nextPosYPotentielle = (nextPosYPotentielle < 0 ? locations[0].length - 1
					: nextPosYPotentielle % locations[0].length);
			animalPresent = (Animal) locations[nextPosYPotentielle][nextPosXPotentielle];
			if (animalPresent != null && animalPresent != this
					&& this.race.proies.contains(animalPresent.getRace())) {
				// On a trouvé une proie
				animalPresent.meurt(locations);
				this.joursDeJeune = 0;
				repus = true;
				nextPosX = nextPosXPotentielle;
				nextPosY = nextPosYPotentielle;
				break;
			} else if (animalPresent == null) {
				nextPosX = nextPosXPotentielle;
				nextPosY = nextPosYPotentielle;
			}
		}

		// Jeune
		if (!repus && (++this.joursDeJeune > this.race.nbJoursSubsistance)) {
			this.meurt(locations);
			return;
		}

		// Déplacement réel
		int oldPosX = this.posX;
		int oldPosY = this.posY;
		this.move(locations, nextPosX, nextPosY);

		// Reproduction
		this.age++;
		if (this.age % this.race.ageReproduction == 0) {
			this.naissance(locations, oldPosX, oldPosY, this.race);
		}

	}

	private void naissance(AgentPhysique[][] locations, int posX, int posY,
			Race race) {
			Animal bebe = new Animal(env, sma, posX, posY, race);
			try {
				this.env.addAgent(bebe);
				sma.addAgentApres(bebe);
			} catch (Exception e) {
			}
	}

	private void move(AgentPhysique[][] locations, int nextPosX, int nextPosY) {
		locations[posY][posX] = null;
		posX = nextPosX;
		posY = nextPosY;
		this.env.addAgent(this);
	}

	private void meurt(AgentPhysique[][] locations) {
		locations[this.posY][this.posX] = null;
		sma.removeAgentApres(this);
	}

	@Override
	public void dessine(Graphics g) {
		g.setColor(this.race.getCouleur());
		g.fillOval(posX * taille, posY * taille, taille, taille);
	}

	public int getAge() {
		return age;
	}
}
