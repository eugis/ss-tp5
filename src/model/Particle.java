package model;

import utils.RandomUtils;

public class Particle {
	
	private int id;
	protected Point position;
	protected Point velocity;
	private double radius;
	private double mass;
	
	public Particle(int id, double x, double y, double z, double vx, double vy, double vz, double m, double r) {
		this.id = id;
		this.position = new Point(x, y, z);
		this.velocity = new Point(vx, vy, vz);
		this.mass = m;
		this.radius = r;
	}

	public Particle(int id, double x, double y, double vx, double vy, double m, double r) {
		this.id = id;
		this.position = new Point(x, y);
		this.velocity = new Point(vx, vy);
		this.mass = m;
		this.radius = r;
	}
	
	public Particle(int id, double x, double y, double velAbs, double m, double r) {
		this.id = id;
		this.position = new Point(x, y);
		double angle = RandomUtils.getRandomDouble(0, 2*Math.PI);
		this.velocity = new Point(velAbs * Math.cos(angle), velAbs * Math.sin(angle));
		this.mass = m;
		this.radius = r;
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return position.x;
	}
	
	public double getY(){
		return position.y;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public double getXVelocity(){
		return velocity.x;
	}
	
	public double getYVelocity(){
		return velocity.y;
	}
	
	public Point getVelocity(){
		return velocity;
	}
	
	public double getMass(){
		return mass;
	}

	public double getRadius() {
		return radius;
	}
	
	public void updatePosition(double x, double y) {
		this.position = new Point(x, y);
	}
	
	public void updatePosition(double x, double y, double z) {
		this.position = new Point(x, y, z);
	}
	
	public void updateVelocity(double x, double y) {
		this.velocity = new Point(x, y);
	}
	
	public void updateVelocity(double x, double y, double z) {
		this.velocity = new Point(x, y, z);
	}
	
	public void move(double time){
		Point deltaPosition = velocity.clone();
		deltaPosition.applyFunction(v->v*time);
		position.add(deltaPosition);
	}
	
	public static boolean areOverlapped(Particle p, Particle q){
		return Math.pow(p.position.x-q.position.x, 2) + Math.pow(p.position.y-q.position.y, 2) <= Math.pow(p.radius+q.radius,2);
	}

	public double getSpeed() {
		return velocity.abs();
	}
	
	public void addMass(double mass) {
		this.mass+=mass;
	}
	
	public void addRadius(double radius) {
		this.radius+=radius;
	}

}
