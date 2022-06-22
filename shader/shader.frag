#define PI 3.1415926538


varying float posZ;
uniform float u_Time;
uniform vec2 step;

void main() {
    float r;
    vec2 gb;
    vec2 sunPos = vec2(0.,0.);


    sunPos.y = u_Time *  0.25;

    if (u_Time > 0.25 && u_Time < 0.375){
        r = (cos(8.0*PI*u_Time)+1.0)/2;


    }
    else if(u_Time > 0.645 && u_Time < 0.75){
        r = (cos(8.0*PI*u_Time)+1.0)/2;
    }
    else if( u_Time <= 0.25  || u_Time >= 0.75  ){
        r = 1.;
    }
    else{
        r = 0.;
    }

    if (posZ == -10){
        if(sunPos.y <=0 && sunPos.y >= 1){
            if (distance(sunPos, gl_FragCoord.xy)  < 100.*step.y){
                gl_FragColor = vec4(1,r,r,1);
            }
            else{
                gl_FragColor = vec4(0,0,0,1);
            }
        }

    }
    else{
        gl_FragColor = gl_Color;
    }


    //gl_BackColor =vec4(1,r,0.9*r,1) ;
}