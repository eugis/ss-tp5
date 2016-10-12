package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

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
		lines.add(String.valueOf(particles.size()));
		lines.add("ParticleId xCoordinate yCoordinate xDisplacement yDisplacement Radius R G B Transparency Selection");
		for (Particle p : particles) {
			if (p.getId() == 1) {
				lines.add(getInfo(p, "1 0 0", 0, 0));
			} else {
				lines.add(getInfo(p, "0 0 1", 0, 0));
			}
		}
		writeFile(lines);
	}


	//TODO: add z
	private String getInfo(Particle p, String color, double transparency, int selection) {
		return p.getId() + " " + String.format("%f", p.getX()) + " " + String.format("%f", p.getY()) + " " + String.format("%f", p.getXVelocity()) + " " + String.format("%f", p.getYVelocity()) + " "
				+ String.format("%f", p.getRadius()) + " " + color + " " + transparency + " " + selection;
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
