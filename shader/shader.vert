varying float posZ;

void main() {
    posZ = gl_Vertex.z;

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}