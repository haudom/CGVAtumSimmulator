package main.objekte;

import kapitel04.Vektor3D;
import main.Updatebar;

public abstract class BasisObjekt implements Updatebar {
   public Vektor3D rotation;
   public Vektor3D rotationSpeed = new Vektor3D();
   public Vektor3D position;
   public Vektor3D speed;

   public BasisObjekt() {
      this(new Vektor3D(0,0, 0));
   }

   public BasisObjekt(Vektor3D position) {
      this(position, new Vektor3D(0, 0, 0));
   }

   public BasisObjekt(Vektor3D position, Vektor3D velocity) {
      this(position, velocity, new Vektor3D(0, 0, 0));
   }

   public BasisObjekt(Vektor3D position, Vektor3D speed, Vektor3D rotation) {
      this.position = new Vektor3D(position);
      this.speed = new Vektor3D(speed);
      this.rotation = new Vektor3D(rotation);
   }

   public abstract void render();

   public void update(double time) {}
}
