package run;

import java.util.LinkedList;
import java.util.List;

import model.Particle;
import model.SiloParticle;
import model.Verlet;
import model.VerletParticle;
import utils.OutputFileGenerator;
import utils.OutputXYZFilesGenerator;
import utils.RandomUtils;

public class SiloRunner {

	private double time;
	static public double W = 1.0, L = 2.0, D = 0, fall = 1.0;
	static final public double Kn = 1e5, Kt = 1e5;
	private int N = 200;
	private int tries = 100;
	private int idCounter = 1;
	private final double mass = 0.01;
	private final double maxTime = 5.0;
	private final double dt = 1e-5;
	private final double dt2 = 1.0 / 250;
	private final double MAX_ENERGY = 1e-4;

	public SiloRunner() {
		RandomUtils.setSeed(1234);
		this.run();
	}

	private SiloParticle createRandomParticle() {
		double r = RandomUtils.getRandomDouble(0.5 / 7, 0.5 / 5) / 2.0;
		double x = RandomUtils.getRandomDouble(r, W - r);
		double y = RandomUtils.getRandomDouble(r + fall, (L + fall) - r);
		return new SiloParticle(idCounter, x, y, 0, 0, mass, r);
	}

	private List<VerletParticle> createParticles(int N) {
		List<VerletParticle> list = new LinkedList<VerletParticle>();
		while (idCounter - 1 < N) {
			SiloParticle p = createRandomParticle();
			boolean areOverlapped = false;
			for (VerletParticle pp : list) {
				if (Particle.areOverlapped(p, pp)) {
					areOverlapped = true;
					break;
				}
			}
			if (!areOverlapped) {
				list.add(p);
				idCounter++;
			}
		}
		return list;
	}

	private List<SiloParticle> createParticles() {
		int tryy = 0;
		List<SiloParticle> list = new LinkedList<SiloParticle>();
		while (tryy < tries) {
			SiloParticle p = createRandomParticle();
			boolean areOverlapped = false;
			for (SiloParticle pp : list) {
				if (Particle.areOverlapped(p, pp)) {
					areOverlapped = true;
					break;
				}
			}
			if (!areOverlapped) {
				list.add(p);
				tryy = 0;
				idCounter++;
			} else {
				tryy++;
			}
		}
		return null;
	}

	private void run() {
		OutputXYZFilesGenerator outputXYZFilesGenerator = new OutputXYZFilesGenerator("animation/", "state");
		OutputFileGenerator kineticEnergy = new OutputFileGenerator("animation/", "kinetic");
		OutputFileGenerator caudal = new OutputFileGenerator("animation/", "caudal");
		List<VerletParticle> particles = createParticles(N);
		Verlet v = new Verlet(particles, dt);
		time = 0;
		int totalCaudal = 0;
		double lastTime = 0.0;
		double maxPressure = 0.0;
		double energy = Double.POSITIVE_INFINITY;
		while (energy > MAX_ENERGY) {
			if (lastTime + dt2 < time) {
				outputXYZFilesGenerator.printState(particles);
				energy = getSystemKineticEnery(particles);
				kineticEnergy.addLine(String.valueOf(energy));
				double mp = particles.stream().mapToDouble(x -> x.getPressure()).max().getAsDouble();
				if (maxPressure < mp) {
					maxPressure = mp;
				}
				lastTime = time;
			}
			v.run();
			int c = getCaudal(particles);
			totalCaudal += c;
			for (int i = 0; i < c; i++) {
				caudal.addLine(String.valueOf(time));
			}
			time += dt;
		}
		System.out.println("Time: " + time);
		kineticEnergy.writeFile();
		caudal.writeFile();
	}

	private int getCaudal(List<VerletParticle> particles) {
		int caudal = 0;
		for (VerletParticle particle : particles) {
			if (particle.getOldPosition().y > fall && particle.getPosition().y <= fall) {
				caudal += 1;
			}
		}
		return caudal;
	}

	private double getSystemKineticEnery(List<VerletParticle> particles) {
		double K = 0;
		for (VerletParticle vp : particles) {
			K += vp.getKineticEnergy();
		}
		return K;
	}

}
