package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    // On vérifie que la taille des series qui est renvoyée est correcte
    @Test
    public void testSize() {
        Series<Integer> series = new Series<>(Arrays.asList(1, 2, 3, 4));
        assertEquals(4, series.size());
    }
}
