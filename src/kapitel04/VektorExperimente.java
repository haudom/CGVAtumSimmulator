package kapitel04;

import java.util.concurrent.ThreadLocalRandom;

public class VektorExperimente {
   private static ThreadLocalRandom rand = ThreadLocalRandom.current();
   
   private static void direkterVersusFunktionsaufruf() {
      System.out.println("Direkter Parameteraufruf versus Funktionsaufruf: ");
      Vektor3D a = new Vektor3D(30, 5, -4);
      Vektor3D b = new Vektor3D(-10, 0, 1000);
      Vektor3D c = null;
      
      long startZeit = System.currentTimeMillis();
      for (int i=0; i<100000; i++) {
         c = new Vektor3D(a.getX()+b.getX(), a.getY()+b.getY(), a.getZ()+b.getZ());
      }
      long stopZeit  = System.currentTimeMillis();
      System.out.println("   Dauer "+(stopZeit-startZeit)+" ms mit Funktionsaufrufen");

      startZeit = System.currentTimeMillis();
      for (int i=0; i<100000; i++) {
         c = new Vektor3D(a.x+b.x, a.y+b.y, a.z+b.z);
      }
      stopZeit  = System.currentTimeMillis();
      System.out.println("   Dauer "+(stopZeit-startZeit)+" ms ohne Funktionsaufrufe");
      System.out.println();
   }
   
   private static void lengthVersuslengthSquare() {
      System.out.println("Funktion length versus lengthSquare: ");
      
      final int ANZ_VEKTOREN = 10000000;
      Vektor3D[] testVektoren = new Vektor3D[ANZ_VEKTOREN];      
      for (int i=0; i<testVektoren.length; i++)
         testVektoren[i] = new Vektor3D(rand.nextInt(1000)-500, rand.nextInt(1000)-500, rand.nextInt(1000)-500);
      
      double erg;      
      long startZeit = System.currentTimeMillis();
      for (int i=0; i<ANZ_VEKTOREN; i++) {
         erg = testVektoren[i].length();
      }
      long stopZeit  = System.currentTimeMillis();
      System.out.println("   Dauer "+(stopZeit-startZeit)+" ms mit Funktion length");

      startZeit = System.currentTimeMillis();
      for (int i=0; i<ANZ_VEKTOREN; i++) {
         erg = testVektoren[i].lengthSquare();
      }
      stopZeit  = System.currentTimeMillis();
      System.out.println("   Dauer "+(stopZeit-startZeit)+" ms mit Funktion lengthSquare");
      System.out.println();
   }
   
   public static void baryzentrischeInterpolation() {
      Vektor2D A = new Vektor2D(1,3);
      Vektor2D B = new Vektor2D(10,6);
      Vektor2D C = new Vektor2D(12,2);
      double m_a=10, m_b=8, m_c=4;
      System.out.println(LineareAlgebra.barycentricInterpolation(A, A, B, C, m_a, m_b, m_c));
      System.out.println(LineareAlgebra.barycentricInterpolation(B, A, B, C, m_a, m_b, m_c));
      System.out.println(LineareAlgebra.barycentricInterpolation(C, A, B, C, m_a, m_b, m_c));
      Vektor2D M_AB = LineareAlgebra.sub(B, A);
      M_AB.mult(0.5);
      M_AB.add(A);
      M_AB.show();
      System.out.println(LineareAlgebra.barycentricInterpolation(M_AB, A, B, C, m_a, m_b, m_c));
      System.out.println(LineareAlgebra.barycentricInterpolation(new Vektor2D(7,5), A, B, C, m_a, m_b, m_c));
   }
   
   public static void main(String[] args) {
      //VektorExperimente.direkterVersusFunktionsaufruf();
      //VektorExperimente.lengthVersuslengthSquare();
      VektorExperimente.baryzentrischeInterpolation();
   }
}
