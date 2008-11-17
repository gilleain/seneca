package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel {

	private class Point {
		public int x;
		public int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private String name;
	private Color color;
	private ArrayList<Point> points;

	private double scaleX;
	private double scaleY;
	private double max;

	private int currentStep;

	private int selectedStep;

	public GraphPanel(
			String name, Color color,
			double max, int maxS, int width, int height) {
		this.color = color;
		this.points = new ArrayList<Point>();
		this.max = max;
		this.scaleX = (double) width / (double) maxS;
		this.scaleY = (double) height / max;
		this.currentStep = 0;
		this.name = name;
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.LIGHT_GRAY));
		this.setBackground(Color.WHITE);
		this.selectedStep = -1;
	}

	public void setStepRange(int maxS) {
		this.scaleX = this.getWidth() / (double) maxS;
	}

	public void setSelectedStep(int i) {
		this.selectedStep = i;
	}

	public void clear() {
		this.points.clear();
		this.currentStep = 0;
		this.selectedStep = -1;
	}

	public void addValue(double t) {
		assert t <= this.max && t > 0;

		int x = (int) (this.scaleX * this.currentStep);
		int y = (int) (this.scaleY * (this.max - t));
//		int y = (int) (this.scaleY * (t));

		this.points.add(new Point(x, y));
		this.currentStep++;
	}

	public void paint(Graphics g) {
		if (this.isShowing()) {
			super.paint(g);
			g.drawString(name, 5, 10);
			if (this.points.size() > 0) {
				Point last = this.points.get(0);
				g.setColor(this.color);
				for (int i = 1; i < this.points.size(); i++) {
					Point p = this.points.get(i);
					g.drawLine(last.x, last.y, p.x, p.y);
					last = p;
				}
			}
			if (this.selectedStep != -1) {
				int x = (int) (this.scaleX * this.selectedStep);
				g.setColor(Color.BLUE);
				g.drawLine(x, 0, x, this.getHeight());
			}
		}
	}

}
