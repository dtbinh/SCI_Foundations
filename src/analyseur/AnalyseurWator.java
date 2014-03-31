package analyseur;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import domain.Agent;
import domain.wator.Animal;
import domain.wator.Animal.Race;

public class AnalyseurWator implements Analyseur {
	
	static {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("wator_result.csv", true));
			pw.write("---------------------------------------------------------\n");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateInfos(List<Agent> agents) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("wator_result.csv", true));

		int nbPoissons = 0;
		int nbRequins = 0;
		float ageTotalPoissons = 0;
		float ageTotalRequins = 0;
		
		for (Agent agent : agents) {
			Animal animal = (Animal) agent;
			if (animal.getRace().equals(Race.POISSON)) {
				nbPoissons++;
				ageTotalPoissons += animal.getAge();
			} else {
				nbRequins++;
				ageTotalRequins += animal.getAge();
			}
		}
		float ageMoyenPoissons = (nbPoissons == 0 ? 0 : ageTotalPoissons / nbPoissons);
		System.out.println("Nombre de poissons : "
				+ nbPoissons
				+ " agés en moyenne de "
				+ ageMoyenPoissons);
		float ageMoyenRequins = (nbRequins == 0 ? 0 : ageTotalRequins / nbRequins);
		System.out.println("Nombre de requins : "
				+ nbRequins
				+ " agés en moyenne de "
				+ ageMoyenRequins);
		System.out.println();
		pw.write("" + nbPoissons + "," + ageMoyenPoissons + "," + nbRequins + "," + ageMoyenRequins + "\n");
		pw.close();
		} catch (IOException e) {
			System.err.println("Impossible d'ouvrir le fichier de log");
		}
	}

}
