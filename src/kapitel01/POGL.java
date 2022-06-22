package kapitel01;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11.*;

// POGL = "Primitives of OpenGL" 
public class POGL {
	private POGL() {
	}

	private static Torus torus;

	public static void clearBackgroundWithColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void setBackGroundColorClearDepth(float a, float b, float c) {
		glClearColor(a, b, c, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void renderKreis(float x, float y, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(x, y);
		for (int angle = 0; angle < 360; angle += step)
			glVertex2f(x + (float) Math.sin(angle) * radius, y + (float) Math.cos(angle) * radius);
		glEnd();
	}

	public static void renderViereck(int width, int height) {
		float halfWidth = (float) width / 2;
		float halfHeight = (float) height / 2;

		glBegin(GL_QUADS);
		glVertex3f(-halfWidth, -halfHeight, 0);
		glVertex3f(halfWidth, -halfHeight, 0);
		glVertex3f(halfWidth, halfHeight, 0);
		glVertex3f(-halfWidth, halfHeight, 0);
		glEnd();
	}
	public static void renderViereck(int width, int height, float z) {
		float halfWidth = (float) width / 2;
		float halfHeight = (float) height / 2;

		glBegin(GL_QUADS);
		glVertex3f(-halfWidth, -halfHeight, z);
		glVertex3f(halfWidth, -halfHeight, z);
		glVertex3f(halfWidth, halfHeight, z);
		glVertex3f(-halfWidth, halfHeight, z);
		glEnd();
	}

	public static void renderViereckMitTexturbindung() {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3f(-1.0f, -1.0f, 0.0f);
		glTexCoord2f(1, 0);
		glVertex3f(1.0f, -1.0f, 0.0f);
		glTexCoord2f(1, 1);
		glVertex3f(1.0f, 1.0f, 0.0f);
		glTexCoord2f(0, 1);
		glVertex3f(-1.0f, 1.0f, 0.0f);
		glEnd();
	}

	public static void renderWuerfel() {
		glBegin(GL_QUADS);
		glVertex3f(-1, -1, -1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(-1, 1, -1);

		glVertex3f(-1, -1, 1);
		glVertex3f(1, -1, 1);
		glVertex3f(1, 1, 1);
		glVertex3f(-1, 1, 1);

		glVertex3f(-1, -1, -1);
		glVertex3f(-1, 1, -1);
		glVertex3f(-1, 1, 1);
		glVertex3f(-1, -1, 1);

		glVertex3f(1, -1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(1, 1, 1);
		glVertex3f(1, -1, 1);

		glVertex3f(-1, -1, -1);
		glVertex3f(1, -1, -1);
		glVertex3f(1, -1, 1);
		glVertex3f(-1, -1, 1);

		glVertex3f(-1, 1, -1);
		glVertex3f(1, 1, -1);
		glVertex3f(1, 1, 1);
		glVertex3f(-1, 1, 1);
		glEnd();
	}

	public static void renderWehendeFlaecheKonstruktion(double t) {
		glBegin(GL_QUADS);
		double periode = 20, amplitude = 0.03, step = 0.1;
		float r = 0.5f, g = 0.0f, b = 0.0f;
		double bl, br, tr, tl;

		double flag = 1;
		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t * 10 + x * y * periode)) * amplitude;
				br = Math.sin((t * 10 + (x + step) * y * periode)) * amplitude;
				tr = Math.sin((t * 10 + (x + step) * (y + step) * periode)) * amplitude;
				tl = Math.sin((t * 10 + x * (y + step) * periode)) * amplitude;

				glColor3d(r + bl + flag * step, g + bl + flag * step, b + bl + flag * step);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);

				flag *= -1;
			}
		}
		glEnd();
	}

	public static void renderWehendeFlaeche(double t) {
		glBegin(GL_QUADS);
        double periode = 15, amplitude = 0.03, step = 0.01;
		float r = 0.35f, g = 0.63f, b = 0.73f;
		double bl, br, tl, tr;

		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t*10 + x*y*periode))*amplitude;
				br = Math.sin((t*10 + (x + step)*y*periode))*amplitude;
				tr = Math.sin((t*10 + (x + step)*(y + step)*periode))*amplitude;
				tl = Math.sin((t*10 + x*(y + step)*periode))*amplitude;

				glColor3d(r + bl, g + bl, b + bl);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);
			}
		}
		glEnd();
	}

	public static void createTorus(double x, double y, double z, double r, double thickness, double steps) {
		torus = new Torus(x, y, z, r, thickness, steps);
	}

	public static void renderTorus() {
		if (torus == null)
			createTorus(0, 0, 0, 1, 0.3, 20.0f);

		torus.render();
	}

	public static Model loadModel(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String line;
		String[] lineElements;
		float x, y, z;
		Vector3f vertexIndices 		= null;
		Vector3f texCoordsIndices 	= null;
		Vector3f normalIndices 		= null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				model.texCoords.add(new Vector2f(x, y));
			} else if (line.startsWith("f ")) {
				vertexIndices 		= null;
				texCoordsIndices 	= null;
				normalIndices 		= null;

				lineElements = line.split(" ");
				if (line.contains("/") && lineElements[1].split("/").length > 1) {
					vertexIndices = new Vector3f(Float.valueOf(lineElements[1].split("/")[0]),
							Float.valueOf(lineElements[2].split("/")[0]),
							Float.valueOf(lineElements[3].split("/")[0]));
					texCoordsIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]),
							Float.valueOf(lineElements[2].split("/")[1]),
							Float.valueOf(lineElements[3].split("/")[1]));
					if (lineElements[1].split("/").length == 3) {
						normalIndices = new Vector3f(Float.valueOf(lineElements[1].split("/")[2]),
								Float.valueOf(lineElements[2].split("/")[2]),
								Float.valueOf(lineElements[3].split("/")[2]));
					}
				} else {
					// nur drei Vertices f�r ein Dreieck vorhanden
					vertexIndices = new Vector3f(
							Float.valueOf(lineElements[1]), 
							Float.valueOf(lineElements[2]),
							Float.valueOf(lineElements[3]));
				}
				model.faces.add(new FaceTriangle(vertexIndices, texCoordsIndices, normalIndices));
			}
		}
		reader.close();
		return model;
	}

	public static void renderObject(Model model) {
		glBegin(GL_TRIANGLES);
		for (FaceTriangle face : model.faces) {
			if (face.normal != null) {
				Vector3f n1 = model.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
			}
			if (face.texCoords != null) {
				Vector2f t1 = model.texCoords.get((int) face.texCoords.x - 1);
				glTexCoord2f(t1.x, t1.y);
			}
			Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);
			
			if (face.normal != null) {
				Vector3f n2 = model.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
			}
			if (face.texCoords != null) {
				Vector2f t2 = model.texCoords.get((int) face.texCoords.y - 1);
				glTexCoord2f(t2.x, t2.y);
			}
			Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);
			
			if (face.normal != null) {
				Vector3f n3 = model.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
			}
			if (face.texCoords != null) {
				Vector2f t3 = model.texCoords.get((int) face.texCoords.z - 1);
				glTexCoord2f(t3.x, t3.y);
			}
			Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);
		}
		glEnd();
		glPopMatrix();
	}
	
	public static void renderObjectWithPath(float x, float y, float r, float g, float b, float a, int radius, Weg2DDynamisch path) {
		for (int j = path.getSize()-1; j >= 0 ; j--) {
			float anteil = 1-((float)j/path.getSize());
			glColor4f(r*anteil, g*anteil, b*anteil, 1f);
			renderKreis((float)path.getElement(j).x, (float)path.getElement(j).y, 5, radius);
		}

		glColor4f(r, g, b, a);
		renderKreis(x, y, 5, radius);
	}
	
	public static void renderPfeil(float x, float y, int off, float winkel, int size) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glRotatef(winkel, 0, 0, 1);
		glTranslated(off, 0, 0);
		glScaled(size, size, size);
		
		glBegin(GL_LINES);
		glVertex3d(  0f,  0f, 0);
		glVertex3d(-off/15., 0, 0);
		glEnd();

		glBegin(GL_TRIANGLES);
		glVertex3d(  0f,  .2f, 0);
		glVertex3d(  0f, -.2f, 0);
		glVertex3d( .5f,   0f, 0);
		glEnd();
	}
	
	public static void renderObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(0.05f, 0.39f, 0.51f, 1.0f);
		renderKreis(0, 0, 5, radius);
		glColor4f(0.66f, 0.87f, 0.95f, 1.0f);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen gr��eren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, m�ssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(0.35f, 0.63f, 0.73f, 1.0f);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	
	public static void renderSwarmObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
        glColor4f(0.05f, 0.39f, 0.51f, 1.0f);
        renderKreis(0, 0, 5, radius);
        glColor4f(0.66f, 0.87f, 0.95f, 1.0f);
        renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen gr��eren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, m�ssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(0.35f, 0.63f, 0.73f, 1.0f);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}

}

