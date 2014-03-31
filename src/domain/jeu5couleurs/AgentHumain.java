package domain.jeu5couleurs;

import java.util.List;

import outils.Randomizer;
import domain.Agent;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.jeu5couleurs.dijkstra.Dijkstra;
import domain.jeu5couleurs.dijkstra.Noeud;
import domain.utils.VoisinageMoore;

public class AgentHumain extends Agent {
	private final static VoisinageMoore<Noeud> VOISINAGE_MOORE = new VoisinageMoore<Noeud>();
	private final Noeud[][] distances;
	private final LigneCommande interfaceHumain = LigneCommande
			.getInstance();

	private int points = 0;

	public AgentHumain(Environnement env, SMA sma) {
		super(env, sma);
		this.distances = initDistances(this.env.getLocations());
	}

	@Override
	public void decide() {
		interfaceHumain.interragis(this);
		// On compte - 1 car l'agentHumain ne compte pas
		int nbAgentsPhysiques = this.sma.getAgents().size() - 1;
		int nbPlacesLibres = this.env.getNbAgentsMax() - nbAgentsPhysiques;
		int nbAgentsAAjouter = (nbPlacesLibres >= 3 ? 3 : nbPlacesLibres);
		boolean ok;

		// Ajout des agents
		for (int i = 0; i < nbAgentsAAjouter; i++) {
			ok = false;
			while (!ok) {
				final int posX = Randomizer.randomGenerator.nextInt(env
						.getLocations()[0].length);
				final int posY = Randomizer.randomGenerator.nextInt(env
						.getLocations().length);
				if (this.env.getLocations()[posY][posX] == null) {
					BilleCouleur nouvelleBille = new BilleCouleur(env, sma,
							posX, posY, this);
					this.env.getLocations()[posY][posX] = nouvelleBille;
					sma.addAgentApres(nouvelleBille);
					ok = true;
				}
			}
		}
	}

	@Override
	public boolean isPhysique() {
		return false;
	}

	public Statut testDeplacement(int depart, int arrivee) {
		if (depart < 0 || depart > 99) {
			return Statut.POSITION_DEPART_HORS_GRILLE;
		}
		if (arrivee < 0 || arrivee > 99) {
			return Statut.POSITION_ARRIVEE_HORS_GRILLE;
		}
		int departX = depart % 10;
		int departY = depart / 10;
		AgentPhysique[][] locations = env.getLocations();
		BilleCouleur agentPresent = (BilleCouleur) locations[departY][departX];
		if (agentPresent == null) {
			return Statut.CASE_DEPART_VIDE;
		}

		int arriveeX = arrivee % 10;
		int arriveeY = arrivee / 10;
		AgentPhysique agentArrivee = locations[arriveeY][arriveeX];
		if (agentArrivee != null) {
			return Statut.CASE_ARRIVEE_NON_VIDE;
		}
		if (!existeChemin(departX, departY, arriveeX, arriveeY)) {
			return Statut.PAS_DE_CHEMIN;
		}
		// Déplacement de la bille
		locations[departY][departX] = null;
		locations[arriveeY][arriveeX] = agentPresent;
		agentPresent.setPosX(arriveeX);
		agentPresent.setPosY(arriveeY);
		
		// Vérifiaction des points
		this.addPoint(agentPresent.checkLignes());
		return Statut.OK;
	}

	private boolean existeChemin(int departX, int departY, int arriveeX,
			int arriveeY) {
		AgentPhysique[][] locations = env.getLocations();
		// pour chaque noeud, j'ajoute ses voisins vers lesquels il peut aller
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances[i].length; j++) {
				distances[i][j].reset();
				// On parcourt les voisins de moore
				for (Noeud noeud : VOISINAGE_MOORE.getVoisinsNonNull(distances,
						j, i)) {
					// Si la case voisine ne contient pas d'agent, on ajoute le
					// voisin
					if (locations[noeud.getPosY()][noeud.getPosX()] == null) {
						distances[i][j].addAdjacent(noeud);
					}
				}
			}
		}

		Dijkstra.calculerChemins(distances[departY][departX]);
		List<Noeud> chemin = Dijkstra
				.trouverPlusCourtChemin(distances[arriveeY][arriveeX]);
		return chemin.size() > 1;
	}

	private Noeud[][] initDistances(AgentPhysique[][] locations) {
		Noeud[][] distances = new Noeud[locations.length][locations[0].length];
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances[i].length; j++) {
				distances[i][j] = new Noeud(j, i);
			}
		}
		return distances;
	}

	public int getPoints() {
		return points;
	}

	public void addPoint(int points) {
		this.points += points;
	}

}
