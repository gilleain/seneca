package generator;

import java.util.ArrayList;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.structgen.RandomGenerator;

import spectrum.Judge;

public class MoleculeAnnealerAdapter implements AnnealerAdapterI {
	
	private Judge judge; 
	
	private final ArrayList<StateListener> stateListeners;
	private RandomGenerator randomGenerator;
	
	private double bestCost;
	private double currentCost;
	private double nextCost;
	
	private IMolecule best;
	private IMolecule current;
	private IMolecule next;
	
	public MoleculeAnnealerAdapter(IMolecule startingMolecule, Judge judge) {
		this.judge = judge;
		this.stateListeners = new ArrayList<StateListener>();
		
		this.randomGenerator = new RandomGenerator(startingMolecule);
		
		this.current = startingMolecule;
		this.next = null;
		this.best = current;
		this.bestCost = this.currentCost = this.nextCost = 0.0;
	}

	public IMolecule getBest() {
		return this.best;
	}
	
	public IMolecule getCurrent() {
		return this.current;
	}

	public void addStateListener(StateListener listener) {
		this.stateListeners.add(listener);
	}

	public boolean costDecreasing() {
		System.out.println("current cost: "+ this.currentCost);
		System.out.println("previous cost: "+ this.nextCost);
		return this.nextCost < this.currentCost;
	}

	public double costDifference() {
		return this.currentCost - this.nextCost;
	}
	
	private double cost(IMolecule mol) {
		// the score is in the range [0-1], so the cost must be 1-score.
		return 1 - this.judge.score(mol);
	}

	public void initialState() {
		// bit pointless.
		this.current = this.randomGenerator.getMolecule();
		this.currentCost = cost(this.current);
		this.bestCost = this.currentCost;
	}

	public void nextState() {
		this.next = this.randomGenerator.proposeStructure();
		this.nextCost = cost(this.next);
	}

	public void accept() {
		this.current = this.next;
		this.currentCost = this.nextCost;
		if (this.currentCost < this.bestCost) {
			this.best = this.current;
			this.bestCost = currentCost;
			System.err.println("storing new best " + this.bestCost);
		}
		this.randomGenerator.acceptStructure(); 
		
		fireStateEvent(new MoleculeState(this.current, MoleculeState.Acceptance.ACCEPT));
	}

	public void reject() {
		fireStateEvent(new MoleculeState(this.next, MoleculeState.Acceptance.REJECT));
	}
	
	private void fireStateEvent(State state) {
		for (StateListener listener : this.stateListeners) {
			listener.stateChanged(state);
		}
	}

}
