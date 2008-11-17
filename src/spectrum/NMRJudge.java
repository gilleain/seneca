package spectrum;

import java.util.ArrayList;
import java.util.Iterator;

import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;

public class NMRJudge implements Judge {
	
	private PredictionTool predictor;
	private Spectrum targetSpectrum;
	private ArrayList<JudgeListener> listeners;
	private int counter;
	
	public NMRJudge(PredictionTool tool, IMolecule targetMolecule) {
		this.predictor = tool;
		
		this.counter = 0;
		this.listeners = new ArrayList<JudgeListener>();
		
		try {
			this.targetSpectrum = predict(targetMolecule);
		} catch (CDKException c) {
			System.err.println(c);
		}
	}
	
	public Spectrum getTargetSpectrum() {
		return this.targetSpectrum;
	}
	
	public void addJudgeListener(JudgeListener listener) {
		this.listeners.add(listener);
	}
	
	private void fireChangeEvent(Spectrum spectrum, double score) {
		final PredictionEvent event = new PredictionEvent(spectrum, score);
		for (JudgeListener listener : this.listeners) {
			listener.predictionChanged(event);
		}
	}
	
	public double score(IMolecule other) {
		if (this.targetSpectrum == null) throw new NullPointerException("targetSpectrum is null");
		
		try {
			Spectrum otherSpectrum = predict(other);
			double score = this.targetSpectrum.similarity(otherSpectrum, false);
			System.out.println(score + " " + otherSpectrum);
			fireChangeEvent(otherSpectrum, score);
			return score;
		} catch (CDKException c) {
			return 0.0;	// ??
		}
	}
	
	private Spectrum predict(IMolecule mol) throws CDKException {
		CDKHueckelAromaticityDetector.detectAromaticity(mol);
		Spectrum spectrum = new Spectrum(this.counter);
		Iterator<IAtom> atoms = mol.atoms();
		while (atoms.hasNext()) {
			IAtom atom = atoms.next();
			if (atom.getSymbol().equals("C")) {
				float result = predictor.predict(mol, atom);
				spectrum.addSignal(result);
			}
		}
		return spectrum;
	}

}
