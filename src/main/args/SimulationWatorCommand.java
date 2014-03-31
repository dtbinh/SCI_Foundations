package main.args;

import java.util.Calendar;

import javax.swing.JFrame;

import outils.Randomizer;
import vues.Vue;
import analyseur.Analyseur;
import analyseur.AnalyseurWator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.wator.Animal;
import domain.wator.Animal.Race;

/**
 * Describes the expected arguments in case of a simulation run.
 * 
 */
@Parameters(commandDescription = "Lance une simulation avec des proies et des prédateurs.")
public class SimulationWatorCommand implements UserCommand {

	/**
	 * largeur.
	 */
	@Parameter(names = "-largeur", description = "Largeur.")
	private final int largeur = 700;

	/**
	 * hauteur.
	 */
	@Parameter(names = "-hauteur", description = "Hauteur.")
	private final int hauteur = 700;

	/**
	 * nombre de poissons.
	 */
	@Parameter(names = "-nbPoissons", description = "Nombre de poissons.")
	private final int nbPoissons = 300;

	/**
	 * nombre de requins.
	 */
	@Parameter(names = "-nbRequins", description = "Nombre de requins.")
	private final int nbRequins = 150;
	
	/**
	 * age de reproduction des poissons.
	 */
	@Parameter(names = "-ageReproductionPoissons", description = "Age de reproduction des poisson.")
	private final int ageReproductionPoissons = 4;

	/**
	 * age de reproduction des requins.
	 */
	@Parameter(names = "-ageReproductionRequins", description = "Age de reproduction des requins.")
	private final int ageReproductionRequins = 7;

	/**
	 * nombre de jours de subsistance des requins.
	 */
	@Parameter(names = "-nbJoursSubsistanceRequins", description = "Nombre de jours de subsistance des requins.")
	private final int nbJoursSubsistanceRequins = 3;

	/**
	 * temps d'attente.
	 */
	@Parameter(names = "-tempsAttente", description = "Temps d'attente entre deux tours en millisecondes.")
	private final int tempsAttente = 120;

	/**
	 * temps avant arrêt.
	 */
	@Parameter(names = "-tempsArret", description = "Temps avant l'arrêt en secondes (0 pour l'infini).")
	private final int tempsArret = 270;

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

		// Ajout des agents poissons
		for (int i = 0; i < nbPoissons; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt((env
							.getLargeur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					final int posY = Randomizer.randomGenerator.nextInt((env
							.getHauteur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					sma.addAgent(new Animal(env, sma, posX, posY, Race.POISSON));
					ok = true;
				} catch (final IllegalArgumentException ignore) {
				}
			}
		}
		// Ajout des agents requins
		for (int i = 0; i < nbRequins; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt((env
							.getLargeur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					final int posY = Randomizer.randomGenerator.nextInt((env
							.getHauteur() - AgentPhysique.getTaille())
							/ AgentPhysique.getTaille());
					sma.addAgent(new Animal(env, sma, posX, posY, Race.REQUIN));
					ok = true;
				} catch (final IllegalArgumentException ignore) {
				}
			}
		}

		Race.REQUIN.getProies().add(Race.POISSON);
		Race.REQUIN.setAgeReproduction(ageReproductionRequins);
		Race.REQUIN.setNbJoursSubsistance(nbJoursSubsistanceRequins);
		Race.POISSON.setAgeReproduction(ageReproductionPoissons);
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
			// on enlève les agents morts et on ajoute les nouveaux
			synchronized (sma) {
				sma.update();
			}
			tempsTotalRun += Calendar.getInstance().getTimeInMillis()
					- startTour;
			nbTours++;
			 Analyseur analyseur = new AnalyseurWator();
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
