package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import run.SiloRunner;
import utils.CellIndexMethod;

public class Verlet {

	private List<VerletParticle> particles;
	private double dt;
	private CellIndexMethod<VerletParticle> cim;
	private double Kn, Kt;
	
	
	public Verlet(List<VerletParticle> particles, double dt) {
		this.particles = particles;
		this.dt=dt;
		Kn = SiloRunner.Kn;
		Kt = SiloRunner.Kt;
		estimateOldPosition();
		int m = (int)(SiloRunner.L / (0.2 * SiloRunner.D));
		cim = new CellIndexMethod<VerletParticle>(particles, SiloRunner.L, m, 0, false);
	}
	
	private void estimateOldPosition() {
		Map<Integer, Point> forces = new HashMap<Integer, Point>();
		for(VerletParticle p : particles){
			p.updateOldPosition(p.getOwnForce(), dt);
		}
	}

	public void run() {
		Map<VerletParticle, Point> forces = new HashMap<VerletParticle, Point>();
		Map<VerletParticle, Set<VerletParticle>> neighbours = cim.getNeighbours();
		for(VerletParticle p : neighbours.keySet()){
			Point force = p.getOwnForce();
			for(VerletParticle q : neighbours.get(p)){
				force = Point.sum(force, p.getForce(q));
			}
			force = Point.sum(force, wallForce(p));
			Point oldPosition = p.getOldPosition();
			updatePosition(p, force, dt);
			updateVelocity(p, oldPosition, dt);
		}
	}

	private Point wallForce(VerletParticle p) {
		Point sum = new Point(0,0);
		if(p.position.x - p.getRadius() < 0){
			sum = Point.sum(sum, wallLeftForce(p));
		}
		if(p.position.x + p.getRadius() > SiloRunner.W){
			sum = Point.sum(sum, wallRightForce(p));
		}
		if(p.position.y - p.getRadius() < 0){
			sum = Point.sum(sum, wallBottomForce(p));
		}
		return sum;
	}
	
	private Point getForce(VerletParticle p, Point normal, Point tangential, double e){
		Point n = normal.clone();
		n.applyFunction(x->(-Kn*e*x));
		Point t = tangential.clone();
		t.applyFunction(x->(-Kt*e*Point.scalarProd(p.velocity, t)*x));
		return Point.sum(n, t);
	}

	private Point wallRightForce(VerletParticle p) {
		double e =  p.position.x - SiloRunner.W  + p.getRadius();
		return getForce(p, new Point(1, 0), new Point(0, 1), e);
	}

	private Point wallLeftForce(VerletParticle p) {
		double e = p.getRadius() - p.position.x;
		return getForce(p, new Point(-1, 0), new Point(0, -1), e);
	}
	
	private Point wallBottomForce(VerletParticle p) {
		double e = - p.position.y + p.getRadius();
		return getForce(p, new Point(0, -1), new Point(1, 0), e);
	}

	private void updatePosition(VerletParticle p, Point force, double dt) {
		double rx = 2*p.position.x - p.getOldPosition().x + force.x*Math.pow(dt, 2)/p.getMass();
		double ry = 2*p.position.y - p.getOldPosition().y + force.y*Math.pow(dt, 2)/p.getMass();

		if(ry<0){
			p.reset();
		}else{
			p.updatePosition(rx, ry);	
		}
		cim.moveTo(p, p.getPosition());	
	}
	
	private void updateVelocity(VerletParticle p, Point oldPosition, double dt) {
		double vx = (p.position.x - oldPosition.x)/(2*dt);
		double vy = (p.position.y - oldPosition.y)/(2*dt);
		p.updateVelocity(vx, vy);
	}
}
