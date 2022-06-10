package kapitel04;

public class Vektor2D_float {
	public float x, y;

	public Vektor2D_float() {
		this(0, 0);
	}

	public Vektor2D_float(float x, float y) {
		setX(x);
		setY(y);
	}

	public Vektor2D_float(Vektor2D_float vec) {
		this(vec.x, vec.y);
	}

	public Vektor2D_float(float x, float y, float x2, float y2) {
		this(x2 - x, y2 - y);
	}

	public Vektor2D_float(Vektor2D_float a, Vektor2D_float b) {
		this(b.x - a.x, b.y - a.y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(Vektor2D_float vec) {
		setX(vec.x);
		setY(vec.y);
	}

	public boolean isNullvector() {
		return (x == 0 && y == 0);
	}

	public void add(Vektor2D_float vec) {
		x += vec.x;
		y += vec.y;
	}

	public void sub(Vektor2D_float vec) {
		x -= vec.x;
		y -= vec.y;
	}

	public void mult(float s) {
		x *= s;
		y *= s;
	}
	
	public boolean div(float s) {
		if (s!=0) {
			x /= s;
			y /= s;
			return true;
		}
		return false;
	}

	public boolean isEqual(Vektor2D_float vec) {
		return (x == vec.x && y == vec.y);
	}

	public boolean isNotEqual(Vektor2D_float vec) {
		return !isEqual(vec);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double lengthSquare() {
		return x * x + y * y;
	}

	/*public void normalize() {
		if (this.isNullvector())
			setPosition(LineareAlgebra.mult(this, (1.0 / this.length() + 0.00001)));
		else			
			setPosition(LineareAlgebra.div(this, this.length()));
	}*/
	
	/*public void truncate(float max) {
		if (length() > max) {
			normalize();
			mult(max);
		}
	}*/
	
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}

	public void show() {
		System.out.println(toString());
	}
	
	public static void main(String[] args) {
		Vektor2D_float a = new Vektor2D_float(4.0f, 3.0f);
		Vektor2D_float b = new Vektor2D_float(2.0f, -1.0f);
		
		System.out.println(a);
		a.add(b);
		System.out.println(a);
	}
}
