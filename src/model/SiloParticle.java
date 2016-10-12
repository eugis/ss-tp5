package model;

public class SiloParticle extends VerletParticle {

	public SiloParticle(int id, double x, double y, double vx, double vy, double m, double r) {
		super(id, x, y, vx, vy, m, r);
	}

	@Override
	public Point getOwnForce() {
		return new Point(0, -9.8 * getMass());
	}
	
	@Override
	public Point getForce(Particle p) {
		return new Point(0,0);
	}
}
