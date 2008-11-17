package generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import display.GraphPanel;

public class AdaptiveEngineTest extends JFrame 
		implements ActionListener, StateListener, TemperatureListener {

	private static Random rand = new Random();
	
	private GraphPanel graphPanel;
	
	// XXX bit of a hack...
	private GraphPanel stateGraphPanel;
	
	private AnnealingEngineI engine;
	private final JLabel resultLabel;
	
	public int targetNumber;
	public double maxT = 1.0;
	public final static int MAX_S = 1000;
	
	public final static int FRAME_W = 500;
	public final static int FRAME_H = 600;
	public final static int GRAPH_W = 500;
	public final static int GRAPH_H = 200;
	
	public final static int NUMBER_RANGE = 2000;
	
	public AdaptiveEngineTest() {
		
		targetNumber = rand.nextInt(NUMBER_RANGE);
		
		AnnealerAdapterI annealerAdapter 
			= new TrivialAnnealerAdapter(targetNumber, NUMBER_RANGE);
		engine = new AdaptiveAnnealingEngine(annealerAdapter, MAX_S);
		
		int initialT = 10000;
		maxT = initialT;
		double coolRate = 0.9;
		double roundLength = 2.5;
		engine = new BasicAnnealingEngine(annealerAdapter, MAX_S, initialT, coolRate, roundLength);
		
		engine.addTemperatureListener(this);
		annealerAdapter.addStateListener(this);
		
		this.setPreferredSize(new Dimension(FRAME_W, FRAME_H));
		this.setLayout(new BorderLayout());
		
		JButton runButton = new JButton("Run");
		runButton.setActionCommand("run");
		runButton.addActionListener(this);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		
		JPanel controlPanel = new JPanel();
		resultLabel = new JLabel();
		resultLabel.setText("Result=--- Target=---");
		
		controlPanel.add(runButton);
		controlPanel.add(resetButton);
		controlPanel.add(resultLabel);
		
		this.add(controlPanel, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		this.graphPanel = new GraphPanel(
					"temp", Color.RED, maxT, MAX_S, GRAPH_W, GRAPH_H);
		this.stateGraphPanel = new GraphPanel(
					"state", Color.BLACK, NUMBER_RANGE, MAX_S, GRAPH_W, GRAPH_H);
		
		mainPanel.add(stateGraphPanel, BorderLayout.NORTH);
		mainPanel.add(graphPanel, BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent a) {
		String command = a.getActionCommand();
		if (command.equals("run")) {
			reset();
			TrivialAnnealerAdapter result = (TrivialAnnealerAdapter)engine.run();
			resultLabel.setText(
					String.format("Result=%s Target=%s", 
							result.getBestState(), targetNumber));
		} else if (command.equals("reset")) {
			reset();
		}
	}
	
	private void reset() {
		resultLabel.setText("Result=--- Target=---");
		targetNumber = rand.nextInt(NUMBER_RANGE);
		AnnealerAdapterI annealerAdapter 
			= new TrivialAnnealerAdapter(targetNumber, NUMBER_RANGE);
		annealerAdapter.addStateListener(this);
		this.engine.setAnnealerAdapter(annealerAdapter);
		this.graphPanel.clear();
		this.stateGraphPanel.clear();
		this.repaint();
	}
	
	public void stateChanged(State s) {
		stateGraphPanel.addValue(((IntState)s).i);
		repaint();
	}
	
	public void temperatureChange(double t) {
		this.graphPanel.addValue(t);
		this.repaint();
	}

	public static void main(String[] args) {
		new AdaptiveEngineTest();
	}

}
