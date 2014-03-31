package main.args;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.JFrame;

import outils.Randomizer;
import vues.VueWithNumbers;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import domain.AgentPhysique;
import domain.Environnement;
import domain.SMA;
import domain.jeu5couleurs.AgentHumain;
import domain.jeu5couleurs.BilleCouleur;

/**
 * Describes the expected arguments in case of a simulation run.
 * 
 */
@Parameters(commandDescription = "Lance une simulation avec des billes.")
public class SimulationJeu5Couleurs implements UserCommand {

	/**
	 * dimension.
	 */
	 @Parameter(names = "-dimension", description = "Dimension de l'environnement (carré).")
	private int dimension = 400;

	/**
	 * nb d'agents.
	 */
	@Parameter(names = "-nbBilles", description = "Nombre de billes de départ.")
	private int nbAgents = 5;

	/**
	 * seed.
	 */
	@Parameter(names = "-seed", description = "Seed pour \"reproduire un hasard\".")
	private long seed = Calendar.getInstance().getTimeInMillis();

	@Override
	public void process(final JCommander jCommander) {
		Randomizer.setSeed(this.seed);
		AgentPhysique.setTaille(this.dimension / 10);
		final Environnement env = new Environnement(this.dimension, this.dimension);
		final SMA sma = new SMA(env, false);
		boolean ok = false;

		// Ajout des agents

		AgentHumain agentHumain = new AgentHumain(env, sma);
		sma.addAgent(agentHumain);
		for (int i = 0; i < this.nbAgents; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(env
							.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(env
							.getLocations().length);
					BilleCouleur agent = new BilleCouleur(env, sma, posX, posY, agentHumain);
					sma.addAgent(agent);
					ok = true;
				} catch (IllegalArgumentException ignore) {
				}
			}
		}

		final VueWithNumbers vue = new VueWithNumbers(sma, Color.LIGHT_GRAY);
		sma.addObserver(vue);
		vue.setSize(env.getLargeur(), (int) (env.getHauteur() * 1.05));
		// padding pour la taille de la fenetre
		vue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vue.setVisible(true);
		int nbTours = 0;
		int nbAgentsPhysiques = sma.getAgents().size() - 1;
		int nbPlacesLibres = env.getNbAgentsMax() - nbAgentsPhysiques;

		while (nbPlacesLibres > 0) {
			sma.run();
			synchronized (sma) {
				sma.update();
			}
			nbAgentsPhysiques = sma.getAgents().size() - 1;
			nbPlacesLibres = env.getNbAgentsMax() - nbAgentsPhysiques;
			nbTours++;
		}
		vue.dispose();
		System.out.println("Félicitation vous avez accumulé "
				+ agentHumain.getPoints() + " points et survécu " + nbTours + " tours !");
		System.exit(0);

	}
}
