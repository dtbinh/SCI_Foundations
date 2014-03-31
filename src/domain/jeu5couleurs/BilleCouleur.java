package domain.jeu5couleurs;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import outils.Randomizer;
import domain.Agent;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;

public class BilleCouleur extends AgentPhysique {

	private final Color couleur;
	private final static Color[] COULEURS = { Color.BLACK, Color.BLUE,
			Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.RED };
	private final AgentPhysique[][] locations;
	private final AgentHumain humain;

	public BilleCouleur(Environnement env, SMA sma, int posX, int posY, AgentHumain humain) {
		super(env, sma, posX, posY);
		this.locations = this.env.getLocations();
		this.couleur = COULEURS[Randomizer.randomGenerator
				.nextInt(COULEURS.length)];
		this.humain = humain;
		// this.couleur = COULEURS[5];
	}

	@Override
	public void decide() {
		humain.addPoint(this.checkLignes());
	}

	@Override
	public void dessine(Graphics g) {
		g.setColor(couleur);
		g.fillOval(posX * taille, posY * taille, taille, taille);
	}

	public int checkLignes() {
		Set<Agent> serieHor = new HashSet<Agent>();
		Set<Agent> serieVer = new HashSet<Agent>();
		Set<Agent> serieDiaDesc = new HashSet<Agent>();
		Set<Agent> serieDiaAsc = new HashSet<Agent>();
		checkDirections(serieHor, serieVer, serieDiaDesc, serieDiaAsc);
		int nbBillesRetirées = 0;
		if (serieHor.size() >= 4) {
			sma.removeAgentApres(serieHor);
			sma.removeAgentApres(this);
			nbBillesRetirées += serieHor.size();
		}
		if (serieVer.size() >= 4) {
			sma.removeAgentApres(serieVer);
			sma.removeAgentApres(this);
			nbBillesRetirées += serieVer.size();
		}
		if (serieDiaDesc.size() >= 4) {
			sma.removeAgentApres(serieDiaDesc);
			sma.removeAgentApres(this);
			nbBillesRetirées += serieDiaDesc.size();
		}
		if (serieDiaAsc.size() >= 4) {
			sma.removeAgentApres(serieDiaAsc);
			sma.removeAgentApres(this);
			nbBillesRetirées += serieDiaAsc.size();
		}
		if (nbBillesRetirées > 0) {
			nbBillesRetirées++; // On compte la bille courante
		}
		return nbBillesRetirées;
	}

	private void checkDirections(Set<Agent> serieHor, Set<Agent> serieVer,
			Set<Agent> serieDiaDesc, Set<Agent> serieDiaAsc) {
		boolean continuerHorGauche = true;
		boolean continuerHorDroite = true;
		boolean continuerVerBas = true;
		boolean continuerVerHaut = true;
		boolean continuerDiaDescGauche = true;
		boolean continuerDiaDescDroite = true;
		boolean contineurDiaAscGauche = true;
		boolean contineurDiaAscDroite = true;

		int facteurMultXDroite = 0;
		int facteurMultXGauche = 0;
		int facteurMultYDroite = 0;
		int facteurMultYGauche = 0;
		// On regarde à gauche ou en bas
		while (continuerHorGauche || continuerHorDroite || continuerVerBas
				|| continuerVerHaut || continuerDiaDescGauche
				|| continuerDiaDescDroite || contineurDiaAscGauche
				|| contineurDiaAscDroite) {
			facteurMultXGauche--;
			facteurMultYGauche--;
			facteurMultXDroite++;
			facteurMultYDroite++;
			continuerHorGauche = continuerHorGauche
					&& check(serieHor, facteurMultXGauche, facteurMultYGauche,
							Directions.HORIZONTAL);
			continuerHorDroite = continuerHorDroite
					&& check(serieHor, facteurMultXDroite, facteurMultYDroite,
							Directions.HORIZONTAL);
			continuerVerBas = continuerVerBas
					&& check(serieVer, facteurMultXGauche, facteurMultYGauche,
							Directions.VERTICAL);
			continuerVerHaut = continuerVerHaut
					&& check(serieVer, facteurMultXDroite, facteurMultYDroite,
							Directions.VERTICAL);
			continuerDiaDescGauche = continuerDiaDescGauche
					&& check(serieDiaDesc, facteurMultXGauche,
							facteurMultYGauche, Directions.DIAGONAL_DESC);
			continuerDiaDescDroite = continuerDiaDescDroite
					&& check(serieDiaDesc, facteurMultXDroite,
							facteurMultYDroite, Directions.DIAGONAL_DESC);
			contineurDiaAscGauche = contineurDiaAscGauche
					&& check(serieDiaAsc, facteurMultXGauche,
							facteurMultYGauche, Directions.DIAGONAL_ASC);
			contineurDiaAscDroite = contineurDiaAscDroite
					&& check(serieDiaAsc, facteurMultXDroite,
							facteurMultYDroite, Directions.DIAGONAL_ASC);
		}
	}

	private boolean check(Set<Agent> serie, int facteurMultX, int facteurMultY,
			Directions direction) {
		int curX = this.posX + facteurMultX * direction.dirX;
		int curY = this.posY + facteurMultY * direction.dirY;
		if (curX >= 0 && curX < locations[0].length && curY >= 0
				&& curY < locations.length && locations[curY][curX] != null) {
			BilleCouleur bille = (BilleCouleur) locations[curY][curX];
			if (!bille.couleur.equals(this.couleur)) {
				return false;
			}
			serie.add(bille);
			return true;
		}
		return false;
	}

	private enum Directions {
		HORIZONTAL(1, 0), VERTICAL(0, 1), DIAGONAL_DESC(1, 1), DIAGONAL_ASC(1,
				-1);

		private int dirX;
		private int dirY;

		Directions(int dirX, int dirY) {
			this.dirX = dirX;
			this.dirY = dirY;
		}
	}

}
