package vues;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.Agent;
import domain.AgentPhysique;
import domain.SMA;

@SuppressWarnings("serial")
public class VueWithNumbers extends JFrame implements Observer {
	protected SMA sma;
	private JPanel content;
	private boolean grille;
	private Color colorGrille;

	public VueWithNumbers(SMA sma, Color colorGrille) {
		super("Simulation de système multi-agents");
		this.sma = sma;
		content = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		for (int i = 0; i < 10; i++) {
			gbc.gridx = i + 1;
			gbc.gridy = 0;
			content.add(new JLabel("" + i, SwingConstants.CENTER), gbc);
			gbc.gridx = 0;
			gbc.gridy = i + 1;
			content.add(new JLabel("" + i, SwingConstants.CENTER), gbc);
		}
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 10;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel drawPane = new DrawPane();
		content.add(drawPane, gbc);
		this.add(content);
		this.grille = true;
		this.colorGrille = colorGrille;
	}
	
	public VueWithNumbers(SMA sma) {
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
		super.setSize((int) (width * 1.13), (int) (height * 1.13));
		this.setResizable(false);
	}

}
