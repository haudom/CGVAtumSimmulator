#define PI 3.1415926538
//TODO Beleuchtung der Szene
//TODO Mondlicht nachts (?)
//TODO Sonne wird beim Untergang größer (?)

varying float posZ;
uniform float u_Time;
uniform vec2 u_ScreenSize;
uniform vec2 u_SunPos;

void main() {
    float r;
    vec2 gb;
    vec2 sunPos = vec2(0.,0.);

    if (u_Time > 0.25 && u_Time < 0.375){
        r = (cos(8.0 * PI * u_Time) + 1.0) / 2;
    }
    else if (u_Time > 0.645 && u_Time < 0.75) {
        r = (cos(8.0 * PI * u_Time) + 1.0) / 2;
    }
    else if (u_Time <= 0.25 || u_Time >= 0.75) {
        r = 1.;
    }
    else {
        r = 0.;
    }

    if (posZ == -10) {
        vec4 color;
        float sunCircle = 1 - step(40, distance(gl_FragCoord, vec2(u_ScreenSize.x / 2., u_SunPos.y)));
        color = vec4(sunCircle, sunCircle * r, sunCircle * r, 1.);
        if (color == vec4(0., 0., 0., 1.)) {
            float skyHalo = 1. - distance(gl_FragCoord, vec2(u_ScreenSize.x / 2., u_SunPos.y)) / (u_ScreenSize.x); //TODO Tagsüber schwarz durch blau ersetzen
            color = vec4(skyHalo, skyHalo * r, skyHalo * r, 1.);
        }
        gl_FragColor = color;
    }
    else {
        gl_FragColor = gl_Color;
    }

    //gl_BackColor = vec4(1, r, 0.9 * r, 1) ;
}