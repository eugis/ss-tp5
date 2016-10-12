package run;

import java.util.LinkedList;
import java.util.List;

import model.Particle;
import model.SiloParticle;
import model.Verlet;
import model.VerletParticle;
import utils.OutputXYZFilesGenerator;
import utils.RandomUtils;

public class SiloRunner {

	private double time;
	static public double W = 1.0, L = 3.0, D = 0.2;
	static final public double Kn = 1e5, Kt = 2e5;  
	private int N = 500;
	private int tries = 100;
	private int idCounter = 1;
	private final double mass = 0.01;

	public SiloRunner() {
		RandomUtils.setSeed(123);
		this.run();
	}

	private SiloParticle createRandomParticle(){
		double r = RandomUtils.getRandomDouble(D/7, D/5)/2.0;
		double x = RandomUtils.getRandomDouble(r, W-r);
		double y = RandomUtils.getRandomDouble(r, L-r);
		return new SiloParticle(idCounter, x, y, 0, 0, mass, r);
	}
	
	private List<VerletParticle> createParticles(int N){
		List<VerletParticle> list = new LinkedList<VerletParticle>();
		while(idCounter-1<N){
			SiloParticle p = createRandomParticle();
			boolean areOverlapped = false;
			for(VerletParticle pp : list){
				if(Particle.areOverlapped(p, pp)){
					areOverlapped = true;
					break;
				}
			}
			if(!areOverlapped){
				System.out.println(idCounter);
				list.add(p);
				idCounter++;
			}
		}
		return list;
	}
	
	private List<SiloParticle> createParticles(){
		int tryy = 0;
		List<SiloParticle> list = new LinkedList<SiloParticle>();
		while(tryy<tries){
			SiloParticle p = createRandomParticle();
			boolean areOverlapped = false;
			for(SiloParticle pp : list){
				if(Particle.areOverlapped(p, pp)){
					areOverlapped = true;
					break;
				}
			}
			if(!areOverlapped){
				list.add(p);
				tryy=0;
				idCounter++;
			}else{
				tryy++;	
			}
		}	
		return null;
	}
	
	private final double maxTime = 2.0;
	private final double dt = 3e-5;
	private final double dt2 = 1.0/60;
	
	private void run() {
		OutputXYZFilesGenerator outputXYZFilesGenerator = new OutputXYZFilesGenerator("animation/", "state");
		List<VerletParticle> particles = createParticles(N);
		Verlet v = new Verlet(particles, dt);
		time = 0;
		double lastTime = 0.0;
		while (time < maxTime) {
			if(lastTime+dt2<time){
				outputXYZFilesGenerator.printState(particles);
				lastTime = time;
				System.out.println(time);
			}
			v.run();
			time+=dt;
		}
		
	}
}
