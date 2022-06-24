varying float posZ;
varying float angle;
uniform vec2 u_SunPos;

void main() {
    posZ = gl_Vertex.z;

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
    angle = dot(gl_Normal.yz,normalize(u_SunPos));
}