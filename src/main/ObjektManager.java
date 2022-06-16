package main;

import java.util.HashMap;
import main.objekte.BasisObjekt;

public class ObjektManager {
   private HashMap<Integer, BasisObjekt> partikel;
   private static ObjektManager exemplar = new ObjektManager();

   private ObjektManager() {
      partikel = new HashMap<Integer, BasisObjekt>();
   }

   public static ObjektManager getExemplar() {
      return exemplar;
   }

   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Clonen ist nicht erlaubt");
   }
   
   public void add(BasisObjekt obj) {
      partikel.put(new Integer(obj.id), obj);
   }

   public void remove(BasisObjekt obj) {
      partikel.remove(obj);
   }
   
   public BasisObjekt get(int objID) {
      return partikel.get(new Integer(objID));
   }
   
   public HashMap<Integer, BasisObjekt> getMap() {
      return partikel;
   }
   
   public int size() {
      return partikel.size();
   }
}
