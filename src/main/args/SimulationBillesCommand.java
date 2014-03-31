package main.args;

import java.io.FileNotFoundException;
import java.util.Calendar;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import outils.Randomizer;
import tests.Tests;
import vues.Vue;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.billes.Bille;
import domain.billes.Directions;
import domain.billes.Mur;
import domain.billes.Mur.TypeMur;

/**
 * Describes the expected arguments in case of a simulation run.
 * 
 */
@Parameters(commandDescription = "Lance une simulation avec des billes.")
public class SimulationBillesCommand implements UserCommand {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimulationBillesCommand.class);

	/**
	 * test.
	 */
	@Parameter(names = "-test", description = "Test.")
	private boolean test = false;

	/**
	 * largeur.
	 */
	@Parameter(names = "-largeur", description = "Largeur.")
	private int largeur = 1200;

	/**
	 * hauteur.
	 */
	@Parameter(names = "-hauteur", description = "Hauteur.")
	private int hauteur = 700;

	/**
	 * nb d'agents.
	 */
	@Parameter(names = "-nbAgents", description = "Nombre d'agent.")
	private int nbAgents = 1000;

	/**
	 * temps d'attente.
	 */
	@Parameter(names = "-tempsAttente", description = "Temps d'attente entre deux tours en millisecondes.")
	private int tempsAttente = 120;

	/**
	 * temps avant arrêt.
	 */
	@Parameter(names = "-tempsArret", description = "Temps avant l'arrêt en secondes (0 pour l'infini).")
	private int tempsArret = 15;

	/**
	 * seed.
	 */
	@Parameter(names = "-seed", description = "Seed pour \"reproduire un hasard\".")
	private long seed = Calendar.getInstance().getTimeInMillis();

	@Override
	public void process(final JCommander jCommander) {
		Randomizer.setSeed(this.seed);
		if (this.test) {
			try {
				Tests.runTests();
			} catch (final FileNotFoundException e) {
				SimulationBillesCommand.LOGGER
						.debug("There was an error opening the file");
			}
			System.exit(0);
		}
		final Environnement env = new Environnement(this.largeur, this.hauteur);
		final SMA sma = new SMA(env);
		boolean ok = false;

		// Ajout des murs
		final int nbCasesY = this.hauteur / AgentPhysique.getTaille();
		final int nbCasesX = this.largeur / AgentPhysique.getTaille();
		for (int i = 0; i < nbCasesY; i++) {
			sma.addAgent(new Mur(env, sma, 0, i, TypeMur.VERTICAL));
			sma.addAgent(new Mur(env, sma, nbCasesX - 1, i, TypeMur.VERTICAL));
		}
		for (int i = 1; i < nbCasesX - 1; i++) {
			sma.addAgent(new Mur(env, sma, i, 0, TypeMur.HORIZONTAL));
			sma.addAgent(new Mur(env, sma, i, nbCasesY - 1, TypeMur.HORIZONTAL));
		}
		
		// Ajout des agents
		for (int i = 0; i < this.nbAgents; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
					final Directions direction = Directions.values()[Randomizer.randomGenerator
							.nextInt(Directions.values().length - 1) + 1];
					sma.addAgent(new Bille(env, sma, posX, posY, direction));
					ok = true;
				} catch (IllegalArgumentException ignore) {
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
