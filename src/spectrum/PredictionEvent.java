package spectrum;


public class PredictionEvent {
	
	public final Spectrum spectrum;
	public final double score;
	
	public PredictionEvent(Spectrum spectrum, double score) {
		this.spectrum = spectrum;
		this.score = score;
	}

}
