package model;

import java.util.Collection;

import run.SiloRunner;
import utils.RandomUtils;

public abstract class VerletParticle extends Particle {

	private Point oldPosition;

	public VerletParticle(int id, double x, double y, double vx, double vy,
			double m, double r) {
		super(id, x, y, vx, vy, m, r);
	}
	
	public abstract Point getOwnForce();

	public abstract Point getForce(Particle p);

	public Point getOldPosition() {
		return this.oldPosition;
	}

	public void updatePosition(double x, double y) {
		this.oldPosition = position;
		position = new Point(x, y);
	}

	public void updateOldPosition(Point force, double dt) {
		double x = position.x+(-dt)*velocity.x+(dt*dt)*force.x/(2*getMass());
		double y = position.y+(-dt)*velocity.y+(dt*dt)*force.y/(2*getMass());
		oldPosition = new Point(x, y);
	}

	public void reset(Collection<VerletParticle> topParticles) {
		boolean areOverlapped;
		do{
			position = new Point(RandomUtils.getRandomDouble(getRadius(), SiloRunner.W-getRadius()), SiloRunner.L+SiloRunner.fall-RandomUtils.getRandomDouble(getRadius(), 1-getRadius()));
			areOverlapped = false;
			for(VerletParticle vp : topParticles){
				if(!vp.equals(this) && Particle.areOverlapped(vp, this)){
					areOverlapped = true;
					break;
				}
			}
		}while(areOverlapped);
		velocity = new Point(0,0);
		oldPosition = position.clone();
	}
	
	public double getKineticEnergy() {
		return 0.5 * getMass() * Math.pow(getSpeed(), 2);
	}

	
}
