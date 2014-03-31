package domain;



public class Environnement {
	private int largeur;
	private int hauteur;
	private AgentPhysique[][] locations;
	private int nbAgentsMax;
	
	public Environnement(int largeur, int hauteur) {
		super();
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.locations = new AgentPhysique[hauteur / AgentPhysique.getTaille()][largeur
				/ AgentPhysique.getTaille()];
		this.nbAgentsMax = locations.length * locations[0].length;
	}

	public int getNbAgentsMax() {
		return nbAgentsMax;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setLongueur(int hauteur) {
		this.hauteur = hauteur;
	}

	public AgentPhysique[][] getLocations() {
		return locations;
	}

	public void addAgent(AgentPhysique agent) {
		if (locations[agent.getPosY()][agent.getPosX()] != null) {
			throw new IllegalArgumentException(
					"Deux billes se trouvent dans la même case :\n("
							+ agent.getPosX()
							+ ","
							+ agent.getPosY()
							+ ") et ("
							+ locations[agent.getPosY()][agent.getPosX()]
									.getPosX()
							+ ","
							+ locations[agent.getPosY()][agent.getPosX()]
									.getPosY() + ")");
		}
		locations[agent.getPosY()][agent.getPosX()] = agent;
	}

	public void removeAgent(AgentPhysique agent) {
		this.locations[agent.posY][agent.posX] = null;
	}
}