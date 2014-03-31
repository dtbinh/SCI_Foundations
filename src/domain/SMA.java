package domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class SMA extends Observable {
	private Environnement env;
	private List<Agent> agents;
	private Set<Agent> agentsASupprimer;
	private Set<Agent> agentsAAjouter;
	private boolean shuffle = true;

	public SMA(Environnement env, boolean shuffle) {
		this(env);
		this.shuffle = shuffle;
	}

	public SMA(Environnement env) {
		super();
		this.env = env;
		this.agents = new LinkedList<Agent>();
		this.agentsASupprimer = new HashSet<Agent>();
		this.agentsAAjouter = new HashSet<Agent>();
	}

	public void run() {
		if (shuffle) {
			Collections.shuffle(agents);
		}
		for (Agent agent : agents) {
			agent.decide();
		}
		this.setChanged();
		this.notifyObservers();
	}

	public Environnement getEnv() {
		return env;
	}

	public void setEnv(Environnement env) {
		this.env = env;
	}

	public List<Agent> getAgents() {
		return Collections.unmodifiableList(this.agents);
	}

	public void addAgent(Agent agent) {
		if (agent.isPhysique()) { // On n'ajoute à l'environnement que les
									// agentsPhysiques
			env.addAgent((AgentPhysique) agent);
		}
		this.agents.add(0, agent);
	}

	public void addAgentApres(Agent agent) {
		this.agentsAAjouter.add(agent);
	}

	public void removeAgentApres(Agent agent) {
		this.agentsASupprimer.add(agent);
	}

	public void removeAgentApres(Set<Agent> agents) {
		this.agentsASupprimer.addAll(agents);
	}

	public void update() {
		this.agents.addAll(agentsAAjouter);
		this.agentsAAjouter.clear();
		for (Agent a : agentsASupprimer) {
			this.removeAgent(a);
		}
		this.agentsASupprimer.clear();
		this.setChanged();
		this.notifyObservers();
	}

	private void removeAgent(Agent agent) {
		if (agent.isPhysique()) {
			env.removeAgent((AgentPhysique) agent);
		}
		this.agents.remove(agent);
	}
}
