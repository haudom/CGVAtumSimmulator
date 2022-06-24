#define PI 3.1415926538
//TODO Beleuchtung der Szene
//TODO Mondlicht nachts (?)
//TODO Sonne wird beim Untergang größer (?)

varying float posZ;
varying float angle;
uniform float u_Time;
uniform vec2 u_ScreenSize;
uniform vec2 u_SunPos;

// oder mix() nutzen
float lerp(float v0, float v1, float t) {
    return v0 + t * (v1 - v0);
}
vec4 lerpVec4(vec4 v0, vec4 v1, float t) {
    return vec4(
        lerp(v0.x, v1.x, t),
        lerp(v0.y, v1.y, t),
        lerp(v0.z, v1.z, t),
        lerp(v0.w, v1.w, t)
    );
}

/*
Morgendämmerung:	03:00:00 (12.5%)
Sonnenaufgang:	    03:45:00 (15.625%)
Sonnenhöchststand:	12:00:00 (50%)
Sonnenuntergang:	20:15:00 (84.375%)
Abenddämmerung:	    21:00:00 (87.5%)
*/
vec4 getAmbientColor() {
    // Farben, mit denen die Umgebung multipliziert wird im Verlaufe des Tages
    vec4 nightColor = vec4(40. / 255, 65. / 255, 178. / 255, 1.);
    vec4 nightDeepColor = vec4(36. / 255, 55. / 255, 105. / 255, 1.);
    vec4 morningColor = vec4(212. / 255, 112. / 255, 205. / 255, 1.);
    vec4 dayColor = vec4(255. / 255, 246. / 255, 214. / 255, 1.);
    vec4 dayMidColor = vec4(1., 1., 1., 1.);
    vec4 eveningColor = vec4(233. / 255, 119. / 255, 85. / 255, 1.);

    // Nacht bis Morgendämmerung
    if (u_Time <= 0.125) {
        // Länge = 0.125
        return lerpVec4(nightDeepColor, nightColor, pow(u_Time / 0.125, 2.));
    }
    // Morgendämmerung bis Sonnenaufgang
    else if (u_Time <= 0.15625) {
        float diff = 0.15625 - 0.125;
        float tmp_t = (t - 0.125) / diff;

        return lerpVec4(nightColor, morningColor, tmp_t);
    }
    // Sonnenaufgang bis Tag
    else if (u_Time <= 0.33) {
        float diff = 0.33 - 0.15625;
        float tmp_t = (t - 0.15625) / diff;

        return lerpVec4(morningColor, dayColor, 1.0 - pow(1.0 - tmp_t, 3.));
    }
    // Tag bis Sonnenhöchststand
    else if (u_Time <= 0.5) {
        float diff = 0.5 - 0.33;
        float tmp_t = (t - 0.33) / diff;

        return lerpVec4(dayColor, dayMidColor, 1.0 - pow(1.0 - tmp_t, 2.));
    }
    // Sonnenhöchststand bis Tag
    else if (u_Time <= 0.67) {
        float diff = 0.67 - 0.5;
        float tmp_t = (t - 0.5) / diff;

        return lerpVec4(dayMidColor, dayColor, pow(tmp_t, 2.));
    }
    // Tag bis Sonnenuntergang
    else if (u_Time <= 0.84375) {
        float diff = 0.84375 - 0.67;
        float tmp_t = (t - 0.67) / diff;

        return lerpVec4(dayColor, eveningColor, tmp_t);
    }
    // Sonnenuntergang bis Abenddämmerung
    else if (u_Time <= 0.875) {
        float diff = 0.875 - 0.84375;
        float tmp_t = (t - 0.84375) / diff;

        return lerpVec4(eveningColor, nightColor, tmp_t);
    }
    // Abenddämmerung bis Nacht
    else {
        float diff = 1.0 - 0.875;
        float tmp_t = (t - 0.875) / diff;

        return lerpVec4(nightColor, nightDeepColor, tmp_t);
    }
}

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