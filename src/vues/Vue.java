package vues;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import domain.Agent;
import domain.AgentPhysique;
import domain.SMA;

@SuppressWarnings("serial")
public class Vue extends JFrame implements Observer {
	protected SMA sma;
	private DrawPane content;
	private boolean grille;
	private Color colorGrille;

	public Vue(SMA sma, Color colorGrille) {
		super("Simulation de système multi-agents");
		this.sma = sma;
		this.content = new DrawPane();
		setContentPane(content);
		this.grille = true;
		this.colorGrille = colorGrille;
	}
	
	public Vue(SMA sma) {
		this(sma, null);
		this.grille = false;
	}

	private class DrawPane extends JPanel {
		public void paintComponent(Graphics g) {
			synchronized (sma) {
				g.clearRect(0, 0, sma.getEnv().getLargeur(), sma.getEnv()
						.getHauteur());
				for (Agent agent : sma.getAgents()) {
					if (agent.isPhysique()) {
						AgentPhysique ap = (AgentPhysique) agent;
						ap.dessine(g);
					}
				}
			}
			if (grille) {
				g.setColor(colorGrille);
				for (int i = 0; i <= sma.getEnv().getHauteur(); i += AgentPhysique.getTaille()) {
					g.drawLine(0, i, sma.getEnv().getLargeur(), i);
				}
				for (int i = 0; i <= sma.getEnv().getLargeur(); i += AgentPhysique.getTaille()) {
					g.drawLine(i, 0, i, sma.getEnv().getHauteur());
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o != sma) {
			return;
		}
		this.repaint();
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		this.content.setSize(width, height);
	}

}
