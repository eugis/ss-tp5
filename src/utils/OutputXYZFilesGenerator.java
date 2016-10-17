package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import run.SiloRunner;
import model.Particle;

public class OutputXYZFilesGenerator {
	
	private int frameNumber;
	private String path;

	public OutputXYZFilesGenerator(String directory, String file) {
		frameNumber = 0;
		this.path = directory + file;
		try {
			Files.createDirectories(Paths.get(directory));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printState(List<? extends Particle> particles) {
		List<String> lines = new LinkedList<String>();
		lines.add(String.valueOf(particles.size()+4));
		lines.add("ParticleId xCoordinate yCoordinate xDisplacement yDisplacement Radius R G B Transparency Selection");
		for (Particle p : particles) {
			lines.add(getInfo(p, "1 0 0", 0, 0));
		}
		addBorderParticles(lines);
		writeFile(lines);
	}


	private void addBorderParticles(List<String> lines) {
		lines.add("10000 0 0 0 0 0 0 0 0 1 0");
		lines.add("10001 "+SiloRunner.W+" 0 0 0 0 0 0 0 1 0");
		lines.add("10002 "+SiloRunner.W+" "+SiloRunner.L+" 0 0 0 0 0 0 1 0");
		lines.add("10003 0 "+SiloRunner.L+" 0 0 0 0 0 0 1 0");
		
	}

	//TODO: add z
	private String getInfo(Particle p, String color, double transparency, int selection) {
		return p.getId() + " " + p.getX() + " " + p.getY() + " " + p.getXVelocity() + " " + p.getYVelocity() + " "
				+ p.getRadius() + " " + color + " " + transparency + " " + selection;
	}

	private void writeFile(List<String> lines) {
		Path file = Paths.get(path + frameNumber + ".xyz");
		frameNumber++;
		try {
			Files.write(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
