package model;

import utils.ForcesUtils;

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
		Point dir = Point.sub(p.position, this.position);
		double e = p.getRadius()+getRadius()-dir.abs();
		dir.normalize();
		Point a= ForcesUtils.getForce(Point.sub(this.velocity, p.velocity), dir, new Point(-dir.y, dir.x), e);
		return a;
	}
}
