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
		double e = p.getRadius() + getRadius() - dir.abs();
		if (e < 0) {
			return new Point(0, 0);
		}
		dir.normalize();
		Point a = ForcesUtils.getForce(Point.sub(this.velocity, p.velocity), dir, new Point(-dir.y, dir.x), e);
		if (a.abs() > 10000) {
			System.out.println(getId() + " collide vs " + p.getId());
		}
		return a;
	}
}
