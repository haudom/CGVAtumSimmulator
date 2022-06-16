package main.objekte;

import kapitel04.Vektor3D;
import main.Updatebar;

public abstract class BasisObjekt implements Updatebar {
   public Vektor3D position;
   
   public BasisObjekt() {
      this(new Vektor3D(0,0, 0));
   }
   
   public BasisObjekt(Vektor3D position) {
      this.position = new Vektor3D(position);
   }
   
   public Vektor3D getPosition() {
	   return position;
   }
   
   public void setPosition(Vektor3D pos) {
	   position = new Vektor3D(pos);
   }

   public abstract void render();

   public void update(double time) {
   }
}
