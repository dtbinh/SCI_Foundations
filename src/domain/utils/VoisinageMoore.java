package domain.utils;

import java.util.ArrayList;
import java.util.List;

public class VoisinageMoore<T> {
	
	public List<T> getVoisinsNonNull(T[][] locations, int posX, int posY) {
		List<T> voisins = new ArrayList<T>();
		for (int i = ((posY - 1) < 0 ? 0 : posY - 1); i <= posY + 1
				&& i < locations.length; i++) {
			for (int j = ((posX - 1) < 0 ? 0 : posX - 1); j <= posX + 1
					&& j < locations[0].length; j++) {
				if ((i != posY || j != posX) && voisins != null) {
					voisins.add(locations[i][j]);
				}
			}
		}
		return voisins;
	}
}
