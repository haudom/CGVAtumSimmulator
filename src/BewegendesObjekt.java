import kapitel01.Weg2DDynamisch;
import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;

public abstract class BewegendesObjekt extends BasisObjekt {
	public Vektor2D acceleration;
	public Vektor2D lastAcceleration;
	public Vektor2D velocity;
	
	public Vektor2D heading;
	public Vektor2D side;
	public double MASS;
	public double MAX_SPEED;
	public double MAX_TURN_RATE;
	public double SWARM_DISTANZ;
	public int WIDTH;
	public int HEIGTH;
	public Weg2DDynamisch wegHistorie;
	
	public Verhalten verhalten = null;

	public BewegendesObjekt(Vektor2D position, Vektor2D velocity) {
		super(position);
		this.velocity = new Vektor2D(velocity);
		this.acceleration = new Vektor2D(0, 0);		
		this.lastAcceleration = new Vektor2D(0, 0);	
	}
	
	public double getMass() {
		return MASS;
	}

	public void setMass(double mass) {
		this.MASS = mass;
	}

	public double getMaxSpeed() {
		return MAX_SPEED;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.MAX_SPEED = maxSpeed;
	}

	public double getMaxTurnRate() {
		return MAX_TURN_RATE;
	}

	public void setMaxTurnRate(double maxTurnRate) {
		this.MAX_TURN_RATE = maxTurnRate;
	}
	
	public void setSwarmDistanz(double dist) {
		this.SWARM_DISTANZ = dist;
	}
	
	public double getSwarmDistanz( ) {
		return SWARM_DISTANZ;
	}
	
	public Weg2DDynamisch getWegHistorie() {
		return wegHistorie;
	}

	public void setWegHistorie(Weg2DDynamisch wegHistorie) {
		this.wegHistorie = wegHistorie;
	}
	
	public Vektor2D getHeading() {
		return heading;
	}

	public void setHeading(Vektor2D heading) {
		this.heading = heading;
	}

	public Vektor2D getSide() {
		return side;
	}

	public void setSide(Vektor2D side) {
		this.side = side;
	}

	public int getWidth() {
		return WIDTH;
	}

	public void setWidth(int width) {
		this.WIDTH = width;
	}

	public int getHeight() {
		return HEIGTH;
	}

	public void setHeight(int height) {
		this.HEIGTH = height;
	}

	public void resetAcceleration() {
		lastAcceleration = new Vektor2D(acceleration);
		acceleration.mult(0);
	}

	public void applyForce(Vektor2D force) {
		acceleration.add(force);
	}
	
	public Vektor2D getLastAcceleration() {		
		return lastAcceleration;
	}
	
	public Vektor2D getAcceleration() {		
		return acceleration;
	}
	
	public Vektor2D getAccelerationInRespectToMass() {
		return LineareAlgebra.div(acceleration, MASS);
	}
	
	public Vektor2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vektor2D velocity) {
		this.velocity = velocity;
		
		if (velocity.lengthSquare()>0.00000001) {
			setHeading(LineareAlgebra.normalize(velocity));
			setSide(LineareAlgebra.normalize(LineareAlgebra.senkrechte(heading)));
		} else {
			setHeading(new Vektor2D(1, 0));
			setSide(new Vektor2D(0, 1));
		}
		
	}

	public void setVerhalten(Verhalten verhalten) {
		this.verhalten = verhalten;
	}

	public void update(double time) {
		if (verhalten != null)
			verhalten.update(time);
	}
}
