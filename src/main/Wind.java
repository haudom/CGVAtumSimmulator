package main;

import kapitel04.Vektor3D;

import main.utility.NumberUtil;
import main.utility.SimplexNoise;

/**
 * Die main.Wind-Klasse ist ein updatebares Objekt, das main.Wind-ähnliches Physikverhalten simuliert
 * und dessen velocity-Vektor auf die Position von Elementen addiert werden kann, um auf ihnen
 * main.Wind zu simulieren.
 */
public class Wind implements Updatebar {
  private Vektor3D noisePosition = new Vektor3D();

  public Vektor3D velocity;
  public Vektor3D minWind;
  public Vektor3D maxWind;

  /**
   * @param minWind Die mindeste Windstärke
   * @param maxWind Die maximalste Windstärke
   */
  public Wind(Vektor3D minWind, Vektor3D maxWind) {
    this.minWind = minWind;
    this.maxWind = maxWind;
    this.velocity = new Vektor3D(0, 0, 0);
  }

  public void update(double time) {
    // Erstelle einen zufälligen Vektor
    this.velocity = new Vektor3D(
        NumberUtil.lerp(
            minWind.x,
            maxWind.x,
            SimplexNoise.noise(noisePosition.x, noisePosition.y, noisePosition.z)
        ),
        NumberUtil.lerp(
            minWind.y,
            maxWind.y,
            SimplexNoise.noise(noisePosition.y, noisePosition.z, noisePosition.x)
        ),
        NumberUtil.lerp(
            minWind.z,
            maxWind.z,
            SimplexNoise.noise(noisePosition.z, noisePosition.x, noisePosition.y)
        )
    );

    noisePosition.add(new Vektor3D(
        time * 0.002,
        time * 0.002,
        time * 0.002
    ));
  }
}
