package tests;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JFrame;

import vues.Vue;
import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.billes.Bille;
import domain.billes.Directions;

public class Tests {

	private static Random randomGenerator = new Random();

	private static final int TEMPS_ATTENTE = 10;
	private static final int TEMPS_ARRET = 30;

	private static final int[][] testSet = {
			{ 700, 700, 1, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 10, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 50, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 100, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 500, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 1000, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 5000, 5, TEMPS_ATTENTE, TEMPS_ARRET },
			{ 700, 700, 10000, 5, TEMPS_ATTENTE, TEMPS_ARRET } };

	public static void runTests() throws FileNotFoundException {
		for (int[] test : testSet) {
			System.out.print(test[2] + " billes : ");
			Environnement env = new Environnement(test[0], test[1]);
			SMA sma = new SMA(env);
			Vue vue = new Vue(sma);
			init(test[2], env, sma, vue);
			AgentPhysique.setTaille(test[3]);
			run(test[4], test[5], sma);
			vue.dispose();
		}

	}

	private static void run(int tempsAttente, int tempsArret, SMA sma) {
		long start = Calendar.getInstance().getTimeInMillis();
		long stop = tempsArret * 1000;
		int nbTours = 0;
		double tempsTotalRun = 0;
		while (Calendar.getInstance().getTimeInMillis() - start < stop) {
			long startTour = Calendar.getInstance().getTimeInMillis();
			sma.run();
			tempsTotalRun += (Calendar.getInstance().getTimeInMillis() - startTour);
			nbTours++;
			try {
				Thread.sleep(tempsAttente);
			} catch (InterruptedException ignore) {
			}
		}
		System.out.println(tempsTotalRun / nbTours);

		// System.out.println(nbTours + " tours d'une moyenne de "
		// + tempsTotalRun / nbTours / 1000 + " secondes chacun");
	}

	private static void init(int nbAgents, Environnement env, SMA sma, Vue vue) {
		boolean ok;
		for (int i = 0; i < nbAgents; i++) {
			ok = false;
			while (!ok) {
				try {
					int posX = randomGenerator
							.nextInt(((env.getLargeur() - AgentPhysique.getTaille()) / AgentPhysique
									.getTaille()));
					int posY = randomGenerator
							.nextInt(((env.getHauteur() - AgentPhysique.getTaille()) / AgentPhysique
									.getTaille()));
					Directions direction = Directions.values()[i
							% Directions.values().length];
					sma.addAgent(new Bille(env, sma, posX, posY, direction));
					ok = true;
				} catch (IllegalArgumentException ignore) {
				}
			}
		}
		sma.addObserver(vue);
		vue.setSize(env.getLargeur(), (int) (env.getHauteur() * 1.05));// padding
																		// pour
																		// la
																		// taille
																		// de la
																		// fenetre
		vue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vue.setVisible(true);
	}
}