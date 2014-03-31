package analyseur;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import domain.Agent;
import domain.schelling.Nationaliste;

public class AnalyseurSchelling implements Analyseur {
	
	static {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("schelling_result.csv", true));
			pw.write("---------------------------------------------------------\n");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateInfos(List<Agent> agents) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("schelling_result.csv", true));

		int nbAgentsInsatisfait = 0;
		double ratioTotalDeVoisinsSympas = 0;
		for (Agent agent : agents) {
			Nationaliste nationaliste = (Nationaliste) agent;
			if (nationaliste.isInsatisfait()) {
				nbAgentsInsatisfait++;
			}
			ratioTotalDeVoisinsSympas += nationaliste.getRatioVoisinsSympas ();
		}
		System.out.println("Pourcentage d'agents insatisfaits : "
				+ (100.0 * nbAgentsInsatisfait) / agents.size()
				+ "%\n\tAvec un voisinage sympa moyen de "
				+ ratioTotalDeVoisinsSympas / agents.size() + "%");
		pw.write("" + (100.0 * nbAgentsInsatisfait) / agents.size() + "," + ratioTotalDeVoisinsSympas / agents.size() + "\n");
		pw.close();
		} catch (IOException e) {
			System.err.println("Impossible d'ouvrir le fichier de log");
		}
	}

}
