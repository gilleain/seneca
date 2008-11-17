package generator;

import org.openscience.cdk.interfaces.IMolecule;

public class MoleculeState implements State {
	
	public enum Acceptance { ACCEPT, REJECT };
	
	public final IMolecule molecule;
	
	public final Acceptance acceptance;
	
	public MoleculeState(IMolecule molecule, Acceptance acceptance) {
		this.molecule = molecule;
		this.acceptance = acceptance;
	}

}
