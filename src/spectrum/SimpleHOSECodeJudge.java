package spectrum;

import java.util.ArrayList;
import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.BremserOneSphereHOSECodePredictor;
import org.openscience.cdk.tools.HOSECodeGenerator;

public class SimpleHOSECodeJudge implements Judge {
	
	private final ArrayList<JudgeListener> listeners = new ArrayList<JudgeListener>();
	private HOSECodeGenerator hcg;
	private BremserOneSphereHOSECodePredictor bos;
	private Spectrum currentSpectrum;
	private int counter;
	
	public SimpleHOSECodeJudge(IMolecule targetMolecule) {
		this.hcg = new HOSECodeGenerator();
		this.bos = new BremserOneSphereHOSECodePredictor();
		this.counter = 0;
		this.currentSpectrum = predict(targetMolecule);
	}

	public void addJudgeListener(JudgeListener listener) {
		this.listeners.add(listener);
	}
	
	private void fireChangeEvent(Spectrum spectrum, double score) {
		for (JudgeListener listener : this.listeners) {
			listener.predictionChanged(new PredictionEvent(spectrum, score));
		}
	}
	
	private Spectrum predict(IMolecule other) {
		Iterator<IAtom> atoms = other.atoms();
		Spectrum spectrum = new Spectrum(counter); 
		while (atoms.hasNext()) {
			IAtom atom = atoms.next();
			if (atom.getSymbol().equals("C")) {
				try {
					String hoseCode 
						= hcg.makeBremserCompliant(hcg.getHOSECode(other, atom, 1));
					double shift = this.bos.predict(hoseCode);
					spectrum.addSignal((float)shift);
				} catch (CDKException c) {
					return spectrum;
				}
			}
		}
		return spectrum;
	}

	public double score(IMolecule other) {
		Spectrum otherSpectrum = predict(other);
//		System.out.println(this.currentSpectrum);
//		System.out.println(otherSpectrum);
//		System.out.println(this.currentSpectrum.similarity(otherSpectrum, false));
		double score = this.currentSpectrum.similarity(otherSpectrum, false);
		fireChangeEvent(otherSpectrum, score);
		return score;
	}

}
