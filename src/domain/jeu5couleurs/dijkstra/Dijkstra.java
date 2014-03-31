package domain.jeu5couleurs.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
	public static void calculerChemins(final Noeud source) {
		source.setMinDistance(0.);
		final PriorityQueue<Noeud> NoeudQueue = new PriorityQueue<Noeud>();
		NoeudQueue.add(source);

		while (!NoeudQueue.isEmpty()) {
			final Noeud noeudCourant = NoeudQueue.poll();

			for (final Noeud autre : noeudCourant.getAdjacents()) {
				final double distanceParNoeudCourant = noeudCourant.getMinDistance() + 1;
				if (distanceParNoeudCourant < autre.getMinDistance()) {
					NoeudQueue.remove(autre);
					autre.setMinDistance(distanceParNoeudCourant);
					autre.setPrecedent(noeudCourant);
					NoeudQueue.add(autre);
				}
			}
		}
	}

	public static List<Noeud> trouverPlusCourtChemin(final Noeud destination) {
		final List<Noeud> path = new ArrayList<Noeud>();
		for (Noeud noeud = destination; noeud != null; noeud = noeud.getPrecedent()) {
			path.add(noeud);
		}
		Collections.reverse(path);
		return path;
	}
}
