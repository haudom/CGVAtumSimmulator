package kapitel01;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;

// POGL = "Primitives of OpenGL" 
public class POGL {
	private POGL() {
	}

	public static void clearBackgroundWithColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void setBackgroundColorClearDepth(float a, float b, float c) {
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
		renderViereck(width, height, 0);
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

	public static void renderGradient(
			int width, int height, float z,
			double topR, double topG, double topB,
			double botR, double botG, double botB
	) {

		glBegin(GL_QUADS);

		glColor3d(topR, topG, topB);
		glVertex3f(0, 0, z);
		glVertex3f(width, 0, z);

		glColor3d(botR, botG, botB);
		glVertex3f(width, height, z);
		glVertex3f(0, height, z);

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
					if (line.split(" ")[1].split("/")[1] != "") { // format "f v1//vn1 v2//vn2 v3//vn3"
						texCoordsIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]),
								Float.valueOf(lineElements[2].split("/")[1]),
								Float.valueOf(lineElements[3].split("/")[1]));
					}
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
}

