package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.jpandas.core.DataFrame;
import com.jpandas.utils.FichierTestUtils;

import org.junit.Test;

public class DataFrameTest {

    // Construction Manuelle du DataFrame, sans passer par le CSV
    @Test
    public void testCreationDataFrameAvecUneColonne() {
        HashMap<String, Series<?>> colonnes = new HashMap<>();
        colonnes.put("Name", new Series<>(Arrays.asList("Alice", "Bob")));
        DataFrame dataframe = new DataFrame(colonnes);

        Series<?> nomColonne = dataframe.getColonne("Name");

        assertNotNull(nomColonne);
        assertEquals(2, nomColonne.size());
        assertEquals("Alice", nomColonne.getData().get(0));
        assertEquals("Bob", nomColonne.getData().get(1));
    }

    // On charge un dataframe à partir d'un fichier CSV et on s'assure que les colonnes 
    // sont correctes et non nulles 
    @Test
    public void testChargementCSV_ColonnesCorrectesEtNonNulles() throws IOException {
        Path fichierTMP = Files.createTempFile("test", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Age\nAlice,25\nBob,30\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertNotNull(dataframe.getColonne("Nom"));
            assertNotNull(dataframe.getColonne("Age"));
        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // Test via le CSV, on s'assure qu'on récupère le bon nombre de colonnes dans le CSV qu'on a géné
    @Test
    public void testChargementCSV_TailleColonnes() throws IOException {
        Path fichierTMP = Files.createTempFile("test", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Age\nAlice,25\nBob,30\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertEquals(2, dataframe.getColonne("Nom").size());
            assertEquals(2, dataframe.getColonne("Age").size());
        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On vérifie que les valeurs dans le dataframe chargé à partir du fichier CSV sont les bonnes
    @Test
    public void testChargementCSV_ValeursCorrectes() throws IOException {
        Path fichierTMP = Files.createTempFile("test", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Age\nAlice,25\nBob,30\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertEquals("Alice", dataframe.getColonne("Nom").getData().get(0));
            assertEquals("30", dataframe.getColonne("Age").getData().get(1));
        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On vérifie qu'on lève la bonne exception lorsque le fichier CSV est incorrect
    @Test(expected = IllegalArgumentException.class)
    public void testChargementCSV_Malformed() throws IOException {
        Path fichierTMP = Files.createTempFile("malformed", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Age\n"); // Pas de lignes de données
            writer.close();

            new DataFrame(fichierTMP.toString());
        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On s'assure que le chargement depuis le fichier CSV créé automatiquement est correct
    @Test
    public void testChargementCSV_AvecFichierUtils() throws IOException {
        Path cheminCSV = FichierTestUtils.creerFichierCSVTemporaire();
        cheminCSV.toFile().deleteOnExit();

        DataFrame dataframe = new DataFrame(cheminCSV.toString());

        assertEquals(3, dataframe.getColonne("Nom").size());
        assertEquals("Alice", dataframe.getColonne("Nom").getData().get(0));
        assertEquals("30", dataframe.getColonne("Age").getData().get(1));
        assertEquals("Paris", dataframe.getColonne("Ville").getData().get(2));
    }
}
