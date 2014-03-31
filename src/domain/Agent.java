package domain;


public abstract class Agent {

	protected Environnement env;
	protected SMA sma;
	public Agent(Environnement env, SMA sma) {
		super();
		this.env = env;
		this.sma = sma;
	}

	public Environnement getEnv() {
		return env;
	}

	public SMA getSma() {
		return sma;
	}

	public abstract void decide();

	public abstract boolean isPhysique();
}
