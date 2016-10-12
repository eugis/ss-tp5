package run;

import java.util.ArrayList;
import java.util.List;

import model.VerletParticle;

public class SiloRunner {

	private final double maxTime = 5.0;

	private double time;

	public SiloRunner() {
		super();
		this.run();
	}

	void run() {
		double dt = 10E-3;
		double dt2 = 10E-2;
		
		List<VerletParticle> particles = new ArrayList<VerletParticle>();
		
	}
}
