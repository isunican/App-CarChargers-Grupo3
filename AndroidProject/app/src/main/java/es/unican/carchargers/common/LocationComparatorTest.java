package es.unican.carchargers.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.unican.carchargers.common.LocationComparator;
import es.unican.carchargers.model.Address;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Operator;

public class LocationComparatorTest {


    private LocationComparator comparador;
    @Test
    public void calculateDistanceTest() {

        comparador = new LocationComparator(-29.6866,-123.3046 );

        // UGIC 3.a

        double distancia = comparador.calculateDistance(47.0616,146.3375,-29.6866, -123.3046 );

        assertTrue( distancia > (12408570 * 0.99) && distancia < (12408570 * 1.01) );

        // UGIC 3.b
        distancia = comparador.calculateDistance(0.0, 146.3375, -29.6866, -123.3046);

        assertTrue( distancia > 10051790 * 0.98 && distancia < 10051790 * 1.02 );

        // UGIC 3.c
        distancia = comparador.calculateDistance(47.0616, 0.0, -29.6866, -123.3046);
        System.out.println(distancia);
        assertTrue( distancia > 14851500 * 0.99 && distancia < 14851500 * 1.01 );

        // UGIC 3.d
        distancia = comparador.calculateDistance(47.0616, 146.3375, 0.0, -123.3046);
        assertTrue( distancia > 10044320 * 0.99 && distancia < 10044320 * 1.01 );

        // UGIC 3.e
        distancia = comparador.calculateDistance(47.0616, 146.3375, -29.6866, 0.0);
        assertTrue( distancia > 16558930 * 0.99 && distancia < 16558930 * 1.01 );

    }

    @Test
    public void compareTest() {

        comparador = new LocationComparator(-29.6866,-123.3046 );

        Charger c1 = new Charger();
        c1.address.latitude = "52.343197";
        c1.address.longitude = "-0.170632";

        Charger c2 = new Charger();
        c2.address.latitude = "53.343197";
        c2.address.longitude = "-1.170632";

        // UGIC 2.a
        assertTrue(comparador.compare(c1,c2) == 1);

        // UGIC 2.b
        assertTrue(comparador.compare(c2,c1) == -1);

        // UGIC 2.c

        assertTrue(comparador.compare(c1,c1) == 0);

        // UGIC 2.d
        assertTrue(comparador.compare(null,c2) == -1);

        // UGIC 2.3
        assertTrue(comparador.compare(c1,null) == 1);

        // UGIC 2.3
        assertTrue(comparador.compare(null,null) == 0);
    }
}