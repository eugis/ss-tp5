package model;

public class SiloParticle extends Particle {

	public SiloParticle(int id, double x, double y, double z, double vx, double vy, double vz, double m, double r) {
		super(id, x, y, z, vx, vy, vz, m, r);
	}

	public SiloParticle(int id, double x, double y, double vx, double vy, double m, double r) {
		super(id, x, y, vx, vy, m, r);
	}

	public SiloParticle(int id, double x, double y, double velAbs, double m, double r) {
		super(id, x, y, velAbs, m, r);
	}

}
