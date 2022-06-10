

import kapitel01.POGL;
import kapitel04.Vektor2D;

public class Agent extends BewegendesObjekt {
	private static int objCounter = 0;
	public ObjektManager objektManager;

	public Agent(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
		super(position, velocity);
		this.id = ++objCounter;

		setMass(1);
		setMaxSpeed(100);
		setMaxTurnRate(15);
		setSwarmDistanz(100);

		setWegHistorie(new Weg2DDynamisch(20));
	}

	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	@Override
	public void render() {
		POGL.renderSwarmObjectWithForces((float) position.x, (float) position.y, 10, velocity, getLastAcceleration());
	}
}
