package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jpandas.core.Series;

public class SeriesTest {

    // On s'assure que la Series se complète correctement avec les bonnes données à la bonne place
    @Test
    public void testGetData() {
        Series<String> series = new Series<>(Arrays.asList("A", "B", "C"));
        assertEquals("A", series.getData().get(0));
        assertEquals("B", series.getData().get(1));
        assertEquals("C", series.getData().get(2));
    }

    // On vérifie que EstNumérique fonctionne avec des valeurs numériques
    @Test
    public void testEstNumeriqueAvecNombres() {
        Series<Number> serie = new Series<>(Arrays.asList(1, 2.5, 3));
        assertTrue(serie.estNumerique());
    }

    // On vérifie que EstNumérique fonctionne avec des valeurs non-numériques
    @Test
    public void testEstNumeriqueAvecTextes() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        assertFalse(serie.estNumerique());
    }

    // On vérifie que EstNumérique fonctionne avec des valeurs nulles
    @Test
    public void testSerieEstNumeriqueAvecNulls() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 2, null, 4));
        assertTrue(serie.estNumerique());
}

    // On vérifie que la taille des series qui est renvoyée est correcte
    @Test
    public void testSize() {
        Series<Integer> series = new Series<>(Arrays.asList(1, 2, 3, 4));
        assertEquals(4, series.size());
    }

    // Les 4 tests ci-dessous s'assurent du bon fonctionne des quatre méthodes statistiques avec des valeurs numériques
    @Test
    public void testMoyenne() {
        Series<Number> serie = new Series<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(3.0, serie.moyenne(), 0.0001);
    }

    @Test
    public void testMinimum() {
        Series<Number> serie = new Series<>(Arrays.asList(10, 2, 8));
        assertEquals(2.0, serie.minimum(), 0.0001);
    }

    @Test
    public void testMaximum() {
        Series<Number> serie = new Series<>(Arrays.asList(10, 2, 8));
        assertEquals(10.0, serie.maximum(), 0.0001);
    }

    @Test
    public void testEcartType() {
        Series<Number> serie = new Series<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(1.5811, serie.ecartType(), 0.0001);
    }


    // Les 4 tests ci-dessous s'assurent du bon fonctionne des quatre méthodes statistiques avec des valeurs catégoricielles
    @Test
    public void testMoyenneAvecSerieNonNumerique() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        assertTrue(Double.isNaN(serie.moyenne()));
    }

    @Test
    public void testMinimumAvecSerieNonNumerique() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        assertTrue(Double.isNaN(serie.minimum()));
    }

    @Test
    public void testMaximumAvecSerieNonNumerique() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        assertTrue(Double.isNaN(serie.maximum()));
    }

    @Test
    public void testEcartTypeAvecSerieNonNumerique() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        assertTrue(Double.isNaN(serie.ecartType()));
    }

    // Les 4 tests ci-dessous s'assurent du bon fonctionne des quatre méthodes statistiques avec série vide
     @Test
    public void testMoyenneAvecSerieVide() {
        Series<Number> serie = new Series<>(new ArrayList<>());
        assertTrue(Double.isNaN(serie.moyenne()));
    }

    @Test
    public void testMaximumAvecSerieVide() {
        Series<Number> serie = new Series<>(new ArrayList<>());
        assertTrue(Double.isNaN(serie.maximum()));
    }

    @Test
    public void testMinimumAvecSerieVide() {
        Series<Number> serie = new Series<>(new ArrayList<>());
        assertTrue(Double.isNaN(serie.minimum()));
    }

    @Test
    public void testEcartTypeAvecSerieVide() {
        Series<Number> serie = new Series<>(new ArrayList<>());
        assertTrue(Double.isNaN(serie.ecartType()));
    }

    // On vérifie que l'écart type fonctionne quand il y a qu'une seule valeur
    @Test
    public void testEcartTypeAvecUneSeuleValeur() {
        Series<Number> serie = new Series<>(Arrays.asList(42));
        assertTrue(Double.isNaN(serie.ecartType()));
    }

    // Les 6 tests ci-dessous s'assurent du bon fonctionne des méthodes statistiques avec des valeurs/séries nulles
    @Test
    public void testMoyenneAvecValeursNulles() {
        Series<Number> serie = new Series<>(Arrays.asList(null, null, null));
        assertTrue(Double.isNaN(serie.moyenne()));
    }

    @Test
    public void testMoyenneAvecNullEtNombres() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 2, null, 4));
        assertEquals(3.0, serie.moyenne(), 0.0001);
    }

    @Test
    public void testMinimumAvecValeursNulles() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 7, null, 2));
        assertEquals(2.0, serie.minimum(), 0.0001);
    }

    @Test
    public void testMaximumAvecValeursNulles() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 7, null, 2));
        assertEquals(7.0, serie.maximum(), 0.0001);
    }

    @Test
    public void testEcartTypeAvecValeursNulles() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 1, 2, null, 3));
        assertEquals(1.0, serie.ecartType(), 0.0001);
    }

    @Test
    public void testSerieNulle() {
        assertThrows(NullPointerException.class, () -> {
            Series<Number> serie = new Series<>(null);
            serie.moyenne();
        });
    }

    // Test pour getValeursNumeriques avec une série vide
    @Test
    public void testGetValeursNumeriquesSerieVide() {
        Series<Number> serie = new Series<>(new ArrayList<>());
        List<Double> valeurs = serie.getValeursNumeriques();
        assertTrue(valeurs.isEmpty());
    }

    // Test pour getValeursNumeriques avec des valeurs nulles
    @Test
    public void testGetValeursNumeriquesAvecValeursNulles() {
        Series<Number> serie = new Series<>(Arrays.asList(null, 2, null, 4));
        List<Double> valeurs = serie.getValeursNumeriques();
        assertEquals(2, valeurs.size());
        assertTrue(valeurs.contains(2.0));
        assertTrue(valeurs.contains(4.0));
    }

    // Test pour getValeursNumeriques avec des valeurs mixtes positives et négatives
    @Test
    public void testGetValeursNumeriquesAvecValeursMixtes() {
        Series<Number> serie = new Series<>(Arrays.asList(-5, 2, -3, 4));
        List<Double> valeurs = serie.getValeursNumeriques();
        assertEquals(4, valeurs.size());
        assertTrue(valeurs.contains(-5.0));
        assertTrue(valeurs.contains(2.0));
        assertTrue(valeurs.contains(-3.0));
        assertTrue(valeurs.contains(4.0));
    }

    // Test pour getValeursNumeriques avec des valeurs non numériques
    @Test
    public void testGetValeursNumeriquesAvecValeursNonNumeriques() {
        Series<String> serie = new Series<>(Arrays.asList("a", "b", "c"));
        List<Double> valeurs = serie.getValeursNumeriques();
        assertTrue(valeurs.isEmpty());
    }

    // Test pour la moyenne avec une série contenant uniquement des nulls (division par zéro)
    @Test
    public void testMoyenneAvecNulls() {
        Series<Number> serie = new Series<>(Arrays.asList(null, null, null));
        assertEquals(Double.NaN, serie.moyenne(), 0.0001);
    }

    // Test pour la moyenne avec une seule valeur (non nulle)
    @Test
    public void testMoyenneAvecUneSeuleValeur() {
        Series<Number> serie = new Series<>(Arrays.asList(10));
        assertEquals(10.0, serie.moyenne(), 0.0001);
    }

    // Les 4 tests ci-dessous s'assurent du bon fonctionne des quatre méthodes statistiques avec des valeurs négatives
    @Test
    public void testMoyenneAvecValeursNegatives() {
        Series<Number> serie = new Series<>(Arrays.asList(-5, -10, -15));
        assertEquals(-10.0, serie.moyenne(), 0.0001);
    }

    @Test
    public void testMinimumAvecValeursNegatives() {
        Series<Number> serie = new Series<>(Arrays.asList(-5, 2, -3, 4));
        assertEquals(-5.0, serie.minimum(), 0.0001);
    }

    @Test
    public void testMaximumAvecValeursNegatives() {
        Series<Number> serie = new Series<>(Arrays.asList(-5, 2, -3, 4));
        assertEquals(4.0, serie.maximum(), 0.0001);
    }

    @Test
    public void testEcartTypeAvecValeursNegatives() {
        Series<Number> serie = new Series<>(Arrays.asList(-5, -10, -15));
        assertEquals(5.0, serie.ecartType(), 0.0001);
    }
}
