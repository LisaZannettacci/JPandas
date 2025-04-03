package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jpandas.core.Series;

public class SeriesTest {

    // @Test
    // public void testSeriesVide() {
    //     // Creation d'une nouvelle Series vide
    //     Series<String> series = new Series<>();
    //     assertTrue(series.getData().isEmpty());
    // }

    @Test
    public void testAjouterDonnees() {
        Series<String> series = new Series<>(Arrays.asList("A", "B", "C"));
        assertEquals(3, series.getData().size());
        assertEquals("A", series.getData().get(0));
    }
}
