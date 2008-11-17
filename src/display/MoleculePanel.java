package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.renderer.Java2DRenderer;
import org.openscience.cdk.smiles.SmilesParser;

public class MoleculePanel extends JPanel {

	private String name;
	private IMolecule molecule;
	private StructureDiagramGenerator generator;
	private Java2DRenderer renderer;
	private SmilesParser smilesParser;
	private Renderer2DModel model;

	public MoleculePanel(String name) {
		this.name = name;
		this.generator = new StructureDiagramGenerator();
		this.smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		model = new Renderer2DModel();
		model.setIsCompact(true);
		this.renderer = new Java2DRenderer(model);
		this.setPreferredSize(new Dimension(350, 350));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setBackground(Color.WHITE);
	}
	
	public void clear() {
		this.molecule = null;
	}
	
	public void setMolecule(String smiles) throws Exception {
		this.setMolecule(this.smilesParser.parseSmiles(smiles));
	}

	public void setMolecule(IMolecule molecule) throws Exception {
		this.generator.setMolecule(molecule);
		this.generator.generateCoordinates();
		this.molecule = this.generator.getMolecule();
	}

	public void paint(Graphics g) {
		if (this.molecule != null) {
			org.openscience.cdk.geometry.GeometryTools.scaleMolecule(molecule, getSize(), 0.8);
			model.setIsCompact(true);
			model.setBackgroundDimension(getSize());
			super.paint(g);
			g.drawString(this.name, 5, 15);	// note that the renderer alters the transform!
			this.renderer.paintMolecule(this.molecule, (Graphics2D) g, getBounds());
		} else {
			super.paint(g);
			g.drawString(this.name, 5, 15);
		}
	}
}
