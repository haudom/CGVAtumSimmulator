#define PI 3.1415926538
//TODO Beleuchtung der Szene
//TODO Mondlicht nachts (?)
//TODO Sonne wird beim Untergang größer (?)

varying float posZ;
varying float angle;
uniform float u_Time;
uniform vec2 u_ScreenSize;
uniform vec2 u_SunPos;

void main() {
    float r;
    float gb;
    vec2 sunPos = vec2(0.,0.);
    vec2 ambientLight;

//    if (u_Time > 0.25 && u_Time < 0.375){
//        r = (cos(8.0 * PI * u_Time) + 1.0) / 2;
//    }
//    else if (u_Time > 0.645 && u_Time < 0.75) {
//        r = (cos(8.0 * PI * u_Time) + 1.0) / 2;
//    }
//    else if (u_Time <= 0.25 || u_Time >= 0.75) {
//        r = 1.;
//    }
//    else {
//        r = 0.;
//    }


    if(u_Time<= 0.25){
        r = - pow(u_Time * 4. ,10.) + 1.;
    }
    else if(u_Time>0.75){
        r = - pow(1.-(u_Time -0.75) * 4.,10.) + 1;
    }
    if(u_Time > 0.25 && u_Time <= 0.75){
        gb = pow((u_Time -0.5) * 4.,8.);
    }
    else{
        gb = 1;
    }

    if (posZ == -10) {
        vec4 color;
        if(u_SunPos.x <= 0){


            float sunCircle = 1 - step(40 + 70 * (1-r), distance(gl_FragCoord, vec2(u_ScreenSize.x / 2., u_SunPos.y)));
            color = vec4(sunCircle, sunCircle * r, sunCircle * r, 1.);
            if (color == vec4(0., 0., 0., 1.)) {
                float skyHalo = 1. - distance(gl_FragCoord, vec2(u_ScreenSize.x / 2., u_SunPos.y)) / (u_ScreenSize.y);
                if (skyHalo < 0)
                {
                    skyHalo = 0;
                }
                color = vec4(max(skyHalo, 0.8*r), max(skyHalo * r, 0.8*r), max(skyHalo * r, max(r, 0.15)), 1.);

                //color += vec4((1-pow(skyHalo,0.8))*0.9*r,(1-pow(skyHalo,0.8))*0.9*r,(1-pow(skyHalo,0.8))*r,0.);
            }
        }
        else{
            float skyHalo = 1. - distance(gl_FragCoord.y, u_SunPos.y) / u_ScreenSize.y;
            color = vec4(max(skyHalo*0.8, 0.8*r),  0.8*r, max(skyHalo * r, max(r, 0.15)), 1.);
        }
        gl_FragColor = color;
    }
    else {
        gl_FragColor = gl_Color * vec4(max(gb*angle,0.5*r),  max(r*angle,0.5*r), max(r*angle,max(0.7*r,0.25)), 1.);
    }

   // gl_BackColor = vec4(1, r, 0.9 * r, 1) ;
}