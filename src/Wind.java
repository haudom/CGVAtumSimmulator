import kapitel04.Vektor2D;
import kapitel04.Vektor3D;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Die Wind-Klasse ist ein updatebares Objekt, das Wind-ähnliches Physikverhalten simuliert
 * und dessen velocity-Vektor auf die Position von Elementen addiert werden kann, um auf ihnen
 * Wind zu simulieren.
 */
public class Wind implements Updatebar {
  public Vektor3D velocity;
  public Vektor3D minWind;
  public Vektor3D maxWind;

  private Vektor3D[] alteWerte;
  private int alteWerteIndex = 0;

  private Random zufall = ThreadLocalRandom.current();

  public Wind(int traegheit) {
    this(
        traegheit,
        new Vektor3D(-20, -20, 0),
        new Vektor3D(20, 20, 0)
    );
  }

  /**
   * @param traegheit Die Trägheit des Windes. Je höher, desto weniger stark ändert sich die
   *                  Richtung.
   * @param minWind Die mindeste Windstärke
   * @param maxWind Die maximalste Windstärke
   */
  public Wind(int traegheit, Vektor3D minWind, Vektor3D maxWind) {
    this.minWind = minWind;
    this.maxWind = maxWind;
    this.velocity = new Vektor3D(0, 0, 0);

    // Damit, durch die zufälligen Werte, der Wind nicht jeden Frame hin und her schlägt,
    // erstellen wir ein Array von vorherigen Windwerten, um dann daraus ein Mittel zu bilden,
    // damit der Wind glatter wirkt.
    this.alteWerte = new Vektor3D[traegheit];
    for (int i = 0; i < traegheit; i++) {
      this.alteWerte[i] = new Vektor3D(0, 0, 0);
    }
  }

  public void update(double time) {
    this.velocity = new Vektor3D(
        zufall.nextFloat() * (maxWind.x - minWind.x) + minWind.x,
        zufall.nextFloat() * (maxWind.y - minWind.y) + minWind.y,
        zufall.nextFloat() * (maxWind.z - minWind.z) + minWind.z
    );

    // Bilde den Durchschnitt der vorherigen Windwerte in Zufallswert.
    for (Vektor3D alterWind : alteWerte) {
      this.velocity.add(alterWind);
    }
    // Dividiere die Summe aller Werte mit Array-Länge + 1 (+1 wegen der Zufallswerte,
    // die schon im zufallsWind-Vektor drin waren, bevor die alten Werte drauf addiert wurden).
    this.velocity.mult(1.0 / (alteWerte.length + 1));

    alteWerte[alteWerteIndex++] = this.velocity;
  }
}
