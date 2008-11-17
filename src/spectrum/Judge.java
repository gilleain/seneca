package spectrum;

import org.openscience.cdk.interfaces.IMolecule;

public interface Judge {

	public void addJudgeListener(JudgeListener listener);

	public double score(IMolecule other);

}