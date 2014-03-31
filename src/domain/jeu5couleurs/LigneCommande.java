package domain.jeu5couleurs;

import java.util.Scanner;

public class LigneCommande {

	private final static Scanner SCANNER = new Scanner(System.in);
	private static LigneCommande instance;

	public static LigneCommande getInstance() {
		if (instance == null) {
			instance = new LigneCommande();
		}
		return instance;
	}
	
	private LigneCommande() {
		super();
	}

	public void interragis(AgentHumain agentJeu) {
		int depart, arrivee;
		Statut statut = Statut.OK;
		do {
			try {
				if (!statut.equals(Statut.OK)) {
					System.out.println(statut.getMessage());
					statut = Statut.OK;
				}
				System.out
						.println("Entrez un nombre pour la position de départ (ligne-colonne)"
								+ " suivi d'un autre pour celle d'arrivée.\n"
								+ "Par exemple :\n"
								+ "34 26 envoie la bille en ligne 3 colonne 4 en position 2-6.\n"
								+ "(les numéros de lignes et colonnes commencent à 0)\n");
				depart = SCANNER.nextInt();
				arrivee = SCANNER.nextInt();
				statut = agentJeu.testDeplacement(depart, arrivee);
			} catch (Exception e) {
				statut = Statut.FORMAT_INCORRECT;
				e.printStackTrace();
			}
		} while (!statut.equals(Statut.OK));
	}
}
