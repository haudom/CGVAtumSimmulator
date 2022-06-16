

import java.util.HashMap;

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
   
   public void registrierePartikel(BasisObjekt obj) {
      partikel.put(new Integer(obj.id), obj);
   }

   public void entfernePartikel(BasisObjekt obj) {
      partikel.remove(obj);
   }
   
   public BasisObjekt getAgent(int objID) {
      return partikel.get(new Integer(objID));
   }
   
   public HashMap<Integer, BasisObjekt> getPartikelMap() {
      return partikel;
   }
   
   public int getAgentSize() {
      return partikel.size();
   }
}
