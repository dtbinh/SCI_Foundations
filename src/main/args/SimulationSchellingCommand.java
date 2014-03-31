package main.args;

import java.util.Calendar;

import javax.swing.JFrame;

import outils.Randomizer;
import vues.Vue;
import analyseur.Analyseur;
import analyseur.AnalyseurSchelling;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.schelling.Nationaliste;
import domain.schelling.Nationaliste.Nationalite;

/**
 * Describes the expected arguments in case of a simulation run.
 * 
 */
@Parameters(commandDescription = "Lance une simulation avec des personnes nationalistes.")
public class SimulationSchellingCommand implements UserCommand {

	/**
	 * largeur.
	 */
	@Parameter(names = "-largeur", description = "Largeur.")
	private final int largeur = 1200;

	/**
	 * hauteur.
	 */
	@Parameter(names = "-hauteur", description = "Hauteur.")
	private final int hauteur = 700;

	/**
	 * densité de population en pourcent.
	 */
	@Parameter(names = "-densite", description = "Densité en pourcent.")
	private final int densite = 70;

	/**
	 * tolérance des individus.
	 * En dessous de ce pourcentage de voisins de même ethnie, un individu bougera.
	 */
	@Parameter(names = "-tolerance", description = "Tolérance des individus en pourcent.")
	private final int tolerance = 60;

	/**
	 * temps d'attente.
	 */
	@Parameter(names = "-tempsAttente", description = "Temps d'attente entre deux tours en millisecondes.")
	private final int tempsAttente = 700;

	/**
	 * temps avant arrêt.
	 */
	@Parameter(names = "-tempsArret", description = "Temps avant l'arrêt en secondes (0 pour l'infini).")
	private final int tempsArret = 25;

	/**
	 * seed.
	 */
	@Parameter(names = "-seed", description = "Seed pour \"reproduire un hasard\".")
	private final long seed = Calendar.getInstance().getTimeInMillis();

	@Override
	public void process(final JCommander jCommander) {
		Randomizer.setSeed(this.seed);

		final Environnement env = new Environnement(this.largeur, this.hauteur);
		final SMA sma = new SMA(env);
		boolean ok = false;

		// Ajout des murs
		final int nbCasesY = this.hauteur / AgentPhysique.getTaille();
		final int nbCasesX = this.largeur / AgentPhysique.getTaille();

		final int nbIndividus = (int) (nbCasesX * nbCasesY / 100.0 * this.densite);

		// Ajout des agents
		for (int i = 0; i < nbIndividus; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt((env
							.getLargeur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					final int posY = Randomizer.randomGenerator.nextInt((env
							.getHauteur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					final Nationalite nationalite = Randomizer.randomGenerator
							.nextInt(2) == 0 ? Nationalite.ROUGE
							: Nationalite.VERT;
					sma.addAgent(new Nationaliste(env, sma, posX, posY,
							this.tolerance, nationalite));
					ok = true;
				} catch (final IllegalArgumentException ignore) {
				}
			}
		}
		final Vue vue = new Vue(sma);
		sma.addObserver(vue);
		vue.setSize(env.getLargeur(), (int) (env.getHauteur() * 1.05));
		// padding pour la taille de la fenetre
		vue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vue.setVisible(true);
		final long start = Calendar.getInstance().getTimeInMillis();
		final long stop = this.tempsArret * 1000;
		int nbTours = 0;
		double tempsTotalRun = 0;
		while (stop == 0
				|| Calendar.getInstance().getTimeInMillis() - start < stop) {
			final long startTour = Calendar.getInstance().getTimeInMillis();
			sma.run();
			tempsTotalRun += Calendar.getInstance().getTimeInMillis()
					- startTour;
			nbTours++;
			Analyseur analyseur = new AnalyseurSchelling();
			analyseur.updateInfos(sma.getAgents()); 
			try {
				Thread.sleep(this.tempsAttente);
			} catch (final InterruptedException ignore) {
			}
		}
		System.out.println(nbTours + " tours d'une moyenne de " + tempsTotalRun
				/ nbTours + " millisecondes chacun");
		System.exit(0);

	}
}
