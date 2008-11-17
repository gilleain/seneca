package generator;

import org.openscience.cdk.interfaces.IMolecule;

import spectrum.Spectrum;

public class Step {

	public final int index;
	public final IMolecule mol;
	public final String smiles;
	public final double score;
	public final Spectrum spectrum;
	public final MoleculeState.Acceptance acceptance;

	public Step(int index,
			IMolecule mol, String smiles, Spectrum spectrum,
			double score, MoleculeState.Acceptance acceptance) {
		this.index = index;
		this.mol = mol;
		this.smiles = smiles;
		this.spectrum = spectrum;
		this.score = score;
		this.acceptance = acceptance;
	}

	public String toString() {
		return String.format("%s %2.2f %s %s %s",
				this.index, this.score, this.acceptance, this.smiles, this.spectrum.toSimpleString());
	}

}
