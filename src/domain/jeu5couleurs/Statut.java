package domain.jeu5couleurs;

public enum Statut {
	OK(""),
	FORMAT_INCORRECT("Entrée incorrecte ou mal formattée."),
	POSITION_DEPART_HORS_GRILLE("La position de départ est en dehors de la grille."),
	POSITION_ARRIVEE_HORS_GRILLE("La position d'arrivée est en dehors de la grille."),
	CASE_DEPART_VIDE("Il n'y a pas de bille à la position de départ."),
	CASE_ARRIVEE_NON_VIDE("Il y a déjà une bille à la position d'arrivée"),
	PAS_DE_CHEMIN("Il n'y a pas de chemin entre ces deux positions.");

	private String message;

	Statut(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
