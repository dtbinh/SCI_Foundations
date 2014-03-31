package domain.billes;

public enum Directions {
	IMMOBILE(0,0),
	HAUT(0, -1) {
		@Override
		public Directions getOpposeY() {
			return BAS;
		}
	},
	HAUT_DROITE(1, -1) {
		@Override
		public Directions getOpposeX() {
			return HAUT_GAUCHE;
		}

		@Override
		public Directions getOpposeY() {
			return BAS_DROITE;
		}
	},
	DROITE(1, 0) {
		@Override
		public Directions getOpposeX() {
			return GAUCHE;
		}
	},
	BAS_DROITE(1, 1) {
		@Override
		public Directions getOpposeX() {
			return BAS_GAUCHE;
		}

		@Override
		public Directions getOpposeY() {
			return HAUT_DROITE;
		}
	},
	BAS(0, 1) {
		@Override
		public Directions getOpposeY() {
			return HAUT;
		}
	},
	BAS_GAUCHE(-1, 1) {
		@Override
		public Directions getOpposeX() {
			return BAS_DROITE;
		}

		@Override
		public Directions getOpposeY() {
			return HAUT_GAUCHE;
		}
	},
	GAUCHE(-1, 0) {
		@Override
		public Directions getOpposeX() {
			return DROITE;
		}
	},
	HAUT_GAUCHE(-1, -1) {
		@Override
		public Directions getOpposeX() {
			return HAUT_DROITE;
		}

		@Override
		public Directions getOpposeY() {
			return BAS_GAUCHE;
		}
	};

	private final int dirX;
	private final int dirY;

	private Directions(int dirX, int dirY) {
		this.dirX = dirX;
		this.dirY = dirY;
	}

	public int getDirX() {
		return this.dirX;
	}

	public int getDirY() {
		return this.dirY;
	}

	public Directions getOpposeX() {
		return this;
	}

	public Directions getOpposeY() {
		return this;
	}
}
