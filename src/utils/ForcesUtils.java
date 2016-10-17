package utils;

import model.Point;
import model.VerletParticle;
import run.SiloRunner;

public class ForcesUtils {

	
	public static Point getForce(Point relativeVelocity, Point normal, Point tangential, double e){
		double Kn = SiloRunner.Kn;
		double Kt = SiloRunner.Kt;
		Point n = normal.clone();
		n.applyFunction(x->(-Kn*e*x));
		double prod = Point.scalarProd(relativeVelocity, tangential);
		Point t = new Point(-Kt*e*prod*tangential.x, -Kt*e*prod*tangential.y);	
		return Point.sum(n, t);
	}

	public static Point wallRightForce(VerletParticle p) {
		double e =  p.getX() - SiloRunner.W  + p.getRadius();
		return getForce(p.getVelocity(), new Point(1, 0), new Point(0, 1), e);
	}

	public static Point wallLeftForce(VerletParticle p) {
		double e = p.getRadius() - p.getX();
		return getForce(p.getVelocity(), new Point(-1, 0), new Point(0, -1), e);
	}
	
	public static Point wallBottomForce(VerletParticle p) {
		double e = - p.getY() + p.getRadius();
		return getForce(p.getVelocity(), new Point(0, -1), new Point(1, 0), e);
	}
}
