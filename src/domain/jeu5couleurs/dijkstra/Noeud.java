package domain.jeu5couleurs.dijkstra;

import java.util.HashSet;
import java.util.Set;

public class Noeud implements Comparable<Noeud> {
	private int posX;
	private int posY;
	private Set<Noeud> adjacents;
	private double minDistance = Integer.MAX_VALUE;
	private Noeud precedent;

	public Noeud(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.precedent = null;
		this.adjacents = new HashSet<Noeud>();
	}

	public int compareTo(Noeud autre) {
		return Double.compare(minDistance, autre.minDistance);
	}

	public Set<Noeud> getAdjacents() {
		return adjacents;
	}
	
	public void reset() {
		this.adjacents.clear();
		this.precedent = null;
		this.minDistance = Integer.MAX_VALUE;
	}

	public void addAdjacent(Noeud adj) {
		this.adjacents.add(adj);
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	public Noeud getPrecedent() {
		return precedent;
	}

	public void setPrecedent(Noeud precedent) {
		this.precedent = precedent;
	}
}
