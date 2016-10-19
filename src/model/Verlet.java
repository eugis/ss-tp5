package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import run.SiloRunner;
import utils.CellIndexMethod;
import utils.ForcesUtils;

public class Verlet {

	private List<VerletParticle> particles;
	private double dt;
	private CellIndexMethod<VerletParticle> cim;
	private List<VerletParticle> vertexParticles;

	public Verlet(List<VerletParticle> particles, double dt) {
		this.particles = particles;
		this.dt = dt;
		estimateOldPosition();
		int m = (int) ((SiloRunner.L + SiloRunner.fall) / (0.2 * SiloRunner.D));
		vertexParticles = new LinkedList<>();
		vertexParticles.add(new SiloParticle(0, SiloRunner.W / 2 - SiloRunner.D / 2, SiloRunner.fall, 0, 0, 0, 0));
		vertexParticles.add(new SiloParticle(0, SiloRunner.W / 2 + SiloRunner.D / 2, SiloRunner.fall, 0, 0, 0, 0));
		cim = new CellIndexMethod<VerletParticle>(particles, SiloRunner.L + SiloRunner.fall, m, 0, false);
	}

	private void estimateOldPosition() {
		for (VerletParticle p : particles) {
			p.updateOldPosition(p.getOwnForce(), dt);
		}
	}

	public void run() {
		Map<VerletParticle, Point> forces = new HashMap<VerletParticle, Point>();
		Map<VerletParticle, Set<VerletParticle>> neighbours = cim.getNeighbours();
		for (VerletParticle p : neighbours.keySet()) {
			Point force = p.getOwnForce();
			for (VerletParticle q : neighbours.get(p)) {
				force = Point.sum(force, p.getForce(q));
			}
			force = Point.sum(force, wallForce(p));
			forces.put(p, force);
		}

		time += dt;
		for (VerletParticle p : neighbours.keySet()) {

			Point oldPosition = p.getOldPosition();
			updatePosition(p, forces.get(p), dt);
			updateVelocity(p, oldPosition, dt);
		}
	}

	static double time = 0;

	private Point wallForce(VerletParticle p) {
		Point sum = new Point(0, 0);
		if (p.position.x - p.getRadius() < 0 && p.position.y > SiloRunner.fall) {
			sum = Point.sum(sum, ForcesUtils.wallLeftForce(p));
		}
		if (p.position.x + p.getRadius() > SiloRunner.W && p.position.y > SiloRunner.fall) {
			sum = Point.sum(sum, ForcesUtils.wallRightForce(p));
		}
		if (Math.abs(p.position.y - SiloRunner.fall) < p.getRadius()) {
			if (inGap(p)) {
				for (VerletParticle particle : vertexParticles) {
					sum.add(p.getForce(particle));
				}
			} else {
				sum = Point.sum(sum, ForcesUtils.wallBottomForce(p));	
			}			
		}
		return sum;
	}

	public boolean inGap(VerletParticle verletParticle) {
		double x = verletParticle.getX();
		double w2 = SiloRunner.W / 2;
		double d2 = SiloRunner.D / 2;
		return x >= w2 - d2 && x <= w2 + d2;
	}

	private void updatePosition(VerletParticle p, Point force, double dt) {
		double rx = 2 * p.position.x - p.getOldPosition().x + force.x * Math.pow(dt, 2) / p.getMass();
		double ry = 2 * p.position.y - p.getOldPosition().y + force.y * Math.pow(dt, 2) / p.getMass();

		p.updatePosition(rx, ry);
		if (ry < 0) {
			p.reset(particles);
		}
	}

	private void updateVelocity(VerletParticle p, Point oldPosition, double dt) {
		double vx = (p.position.x - oldPosition.x) / (2 * dt);
		double vy = (p.position.y - oldPosition.y) / (2 * dt);
		p.updateVelocity(vx, vy);
	}
}
