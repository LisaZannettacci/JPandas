package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import com.jpandas.core.DataFrame;

import org.junit.Test;

public class DataFrameTest {

    @Test
    public void testAjouterColonne() {
        HashMap<String, Series<?>> colonne = new HashMap<>();
        colonne.put("Name", new Series<>(Arrays.asList("Alice", "Bob")));
        DataFrame dataframe = new DataFrame(colonne);
        assertEquals(2, dataframe.GetColonne("Name").getData().size());
    }

    // @Test
    // public void testAjouterDonneesColonne() {
    //     DataFrame dataframe = new DataFrame();
    //     dataframe.AjouterColonne("colonne1");

    //     dataframe.GetColonne("colonne1").getData().add("val1");
    //     dataframe.GetColonne("colonne1").getData().add("val2");

    //     assertEquals(2, dataframe.GetColonne("colonne1").getData().size());
    //     assertEquals("val1", dataframe.GetColonne("colonne1").getData().get(0));
    //     assertEquals("val2", dataframe.GetColonne("colonne1").getData().get(1));
    //}
}
