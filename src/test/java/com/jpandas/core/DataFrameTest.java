package com.jpandas.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.jpandas.core.DataFrame;
import com.jpandas.io.LecteurCSV;
import com.jpandas.utils.FichierTestUtils;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class DataFrameTest {
    
    private DataFrame dataframeAttribut;

    // On vérifie que la méthode getNbColonne renvoie bien le bon nombre de colonne
    @Test
    public void testgetNbColonne() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(Arrays.asList("0", "1")));
        colonnes.put("Nom", new Series<>(Arrays.asList("Lisounette", "Justinette")));
        colonnes.put("Jeux", new Series<>(Arrays.asList("genshin_impac", "paladin")));
        DataFrame dataframe = new DataFrame(colonnes);

        int nbColonne = dataframe.getNbColonne();

        assertEquals(3, nbColonne);
    }

    // Construction Manuelle du DataFrame, sans passer par le CSV
    @Test
    public void testCreationDataFrameAvecUneColonne() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Alice", "Bob")));
        DataFrame dataframe = new DataFrame(colonnes);

        Series<?> nomColonne = dataframe.getColonneByName("Nom");

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

            assertNotNull(dataframe.getColonneByName("Nom"));
            assertNotNull(dataframe.getColonneByName("Age"));
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

            assertEquals(2, dataframe.getColonneByName("Nom").size());
            assertEquals(2, dataframe.getColonneByName("Age").size());
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

            assertEquals("Alice", dataframe.getColonneByName("Nom").getData().get(0));
            assertEquals("30", dataframe.getColonneByName("Age").getData().get(1));
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

        assertEquals(3, dataframe.getColonneByName("Nom").size());
        assertEquals("Alice", dataframe.getColonneByName("Nom").getData().get(0));
        assertEquals("30", dataframe.getColonneByName("Age").getData().get(1));
        assertEquals("Paris", dataframe.getColonneByName("Ville").getData().get(2));
    }

    // On vérifie que l'affichage de toutes les lignes est correct avec une colonne
    @Test
    public void testAffichageToutAvecUneColonne() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Justine", "Lisa")));
        // colonnes.put("Age", new Series<>(Arrays.asList("10", "20")));
        DataFrame dataframe = new DataFrame(colonnes);

        String res = dataframe.afficherLignes(0, 0);
        String attendu = "Index  Nom      \n" + //
                         "-----  ---      \n" + //
                         "0      Justine  \n" + //
                         "1      Lisa     \n" + //
                         "";

        assertEquals(attendu, res);
    }

    // On vérifie que l'affichage de toutes les lignes est correct avec plusieurs colonnes
    @Test
    public void testAffichageToutAvec3Colonnes() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Alice", "Bob")));
        colonnes.put("Age", new Series<>(Arrays.asList("10", "20")));
        colonnes.put("Ville", new Series<>(Arrays.asList("Grenoble", "Lyon")));
        DataFrame dataframe = new DataFrame(colonnes);

        String res = dataframe.afficherLignes(0, 0);
        String attendu = "Index  Nom    Age  Ville     \n" + //
                         "-----  ---    ---  -----     \n" + //
                         "0      Alice  10   Grenoble  \n" + //
                         "1      Bob    20   Lyon      \n" + //
                         "";

        assertEquals(attendu, res);
    }

    // On vérifie que l'affichage des nb premières lignes est correct (ici les 2 premières)
    @Test
    public void testAffichagePremieresLignesAvec3Colonnes() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Alice", "Bob", "Justine", "Lisa")));
        colonnes.put("Age", new Series<>(Arrays.asList("10", "20", "21", "21")));
        colonnes.put("Ville", new Series<>(Arrays.asList("Grenoble", "Lyon", "Grenoble", "Le Cheylas")));
        DataFrame dataframe = new DataFrame(colonnes);

        String res = dataframe.afficherLignes(2, 1);
        String attendu = "Index  Nom    Age  Ville     \n" + //
                         "-----  ---    ---  -----     \n" + //
                         "0      Alice  10   Grenoble  \n" + //
                         "1      Bob    20   Lyon      \n" + //
                         "";

        assertEquals(attendu, res);
    }

    // On vérifie que l'affichage des nb dernières lignes est correct (ici les 2 dernières)
    @Test
    public void testAffichageDernieresLignesAvec3Colonnes() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Alice", "Bob", "Justine", "Lisa")));
        colonnes.put("Age", new Series<>(Arrays.asList("10", "20", "21", "21")));
        colonnes.put("Ville", new Series<>(Arrays.asList("Grenoble", "Lyon", "Grenoble", "Le Cheylas")));
        DataFrame dataframe = new DataFrame(colonnes);

        String res = dataframe.afficherLignes(2, 2);
        String attendu = "Index  Nom      Age  Ville       \n" + //
                         "-----  ---      ---  -----       \n" + //
                         "2      Justine  21   Grenoble    \n" + //
                         "3      Lisa     21   Le Cheylas  \n" + //
                         "";

        assertEquals(attendu, res);
    }

    // On vérifie que lorsqu'il n'y a qu'une seule ligne, c'est bien à la fois la dernière et la première
    @Test
    public void testAffichageUneSeuleLigne() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Justine")));
        colonnes.put("Age", new Series<>(Arrays.asList("21")));
        colonnes.put("Ville", new Series<>(Arrays.asList("Grenoble")));
        DataFrame dataframe = new DataFrame(colonnes);

        String premiere = dataframe.afficherLignes(1, 1);
        String derniere = dataframe.afficherLignes(1, 2);
        String attendu = "Index  Nom      Age  Ville     \n" + //
                         "-----  ---      ---  -----     \n" + //
                         "0      Justine  21   Grenoble  \n" + //
                         "";

        assertEquals(attendu, premiere);
        assertEquals(attendu, derniere);
    }

    // Tests de l'index
    // On vérifie qu'on ajoute bien une colonne index lors de la création d'un dataframe qui n'en contient pas:
    @Test
    public void testCreationManuelleSansIndex() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(Arrays.asList("Titi", "Grogro", "Loki")));
        colonnes.put("Age", new Series<>(Arrays.asList("8", "8", "2")));

        DataFrame dataframe = new DataFrame(colonnes);

        Series<?> index = dataframe.getColonneByName("Index");

        assertNotNull(index);
        assertEquals(3, index.size());
        assertEquals("0", index.getData().get(0));
        assertEquals("1", index.getData().get(1));
        assertEquals("2", index.getData().get(2));
    }

    // On vérifie que la colonne index d'un dataframe existe bien (lorsqu'elle a été crée en amont manuellement)
    @Test
    public void testCreationManuelleAvecIndex() {
        LinkedHashMap<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(Arrays.asList("0", "1")));
        colonnes.put("Nom", new Series<>(Arrays.asList("Justine", "Lisa")));

        DataFrame dataframe = new DataFrame(colonnes);

        Series<?> index = dataframe.getColonneByName("Index");

        assertEquals("0", index.getData().get(0));
        assertEquals("1", index.getData().get(1));
        assertEquals(2, index.size());
    }

    // On vérie qu'il n'y est pas d'erreur lorsque le csv est vide (on ne veut pas ajouter de colonne index)
    @Test(expected = IllegalArgumentException.class)
    public void testIndexNonCreeDansCSVInvalide() throws IOException {
        Path fichierTMP = Files.createTempFile("empty", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("");  // Fichier vide
            writer.close();

            new DataFrame(fichierTMP.toString()); // Devrait lever une exception
        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On vérifie qu'on peut ajouter une colonne 'Index' lorsque l'on extrait un CSV qui n'en contient pas
    @Test
    public void testChargementCSVSansIndex() throws IOException {
        Path fichierTMP = Files.createTempFile("test_sans_index", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Age\nLisa,21\nJustine,21\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertNotNull(dataframe.getColonneByName("Index"));
            assertEquals(2, dataframe.getColonneByName("Index").size());
            assertEquals("0", dataframe.getColonneByName("Index").getData().get(0));
            assertEquals("1", dataframe.getColonneByName("Index").getData().get(1));
            assertEquals(3, dataframe.getNbColonne());

        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On s'assure qu'un CSV qui contient déjà un "Index" est correctement traité
    @Test
    public void testChargementCSVAvecIndex() throws IOException {
        Path fichierTMP = Files.createTempFile("test_avec_index", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Index,Nom,Age\n0,Lisa,21\n1,Justine,21\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertNotNull(dataframe.getColonneByName("Index"));
            assertEquals(2, dataframe.getColonneByName("Index").size());
            assertEquals("0", dataframe.getColonneByName("Index").getData().get(0));
            assertEquals("1", dataframe.getColonneByName("Index").getData().get(1));
            assertEquals(3, dataframe.getNbColonne());

        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On s'assure qu'un CSV qui contient déjà un "Index" dans une position non attendue (au milieu) est correctement traité
    @Test
    public void testChargementCSVAvecIndexAuMilieu() throws IOException {
        Path fichierTMP = Files.createTempFile("test_avec_index", ".csv");
        try {
            FileWriter writer = new FileWriter(fichierTMP.toFile());
            writer.write("Nom,Index,Age\nLisa,0,21\nJustine,1,21\n");
            writer.close();

            DataFrame dataframe = new DataFrame(fichierTMP.toString());

            assertNotNull(dataframe.getColonneByName("Index"));
            assertEquals(2, dataframe.getColonneByName("Index").size());
            assertEquals("0", dataframe.getColonneByName("Index").getData().get(0));
            assertEquals("1", dataframe.getColonneByName("Index").getData().get(1));
            assertEquals(3, dataframe.getNbColonne());

        } finally {
            fichierTMP.toFile().deleteOnExit();
        }
    }

    // On vérifie que l'on peut mettre l'index en premier sans "casser" le dataframe
    @Test
    public void testMettreIndexEnPremier() {
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(List.of("Justine", "Lisa")));
        colonnes.put("Index", new Series<>(List.of("0", "1")));
        colonnes.put("Age", new Series<>(List.of("21", "21")));

        DataFrame dataframe = new DataFrame(colonnes);

        dataframe.mettreIndexEnPremier();

        List<String> nomsColonnes = new ArrayList<>(dataframe.colonne.keySet());

        assertEquals("Index", nomsColonnes.get(0));
        assertEquals("Nom", nomsColonnes.get(1));
        assertEquals("Age", nomsColonnes.get(2));
    }

    // Tests sur la sélection

    // On s'assure que les lignes de la colonne qui a été choisie pour remplacer l'index sont cohérentes
    @Test
    public void testSetIndexRemplaceIndexParColonne() {
    List<String> noms = List.of("Lisa", "Justine", "Eva", "Louis");
    List<Integer> ages = List.of(25, 30, 22, 27);

    Map<String, Series<?>> colonnes = new LinkedHashMap<>();
    colonnes.put("Nom", new Series<>(noms));
    colonnes.put("Age", new Series<>(ages));

    DataFrame dataframe = new DataFrame(colonnes);

    dataframe.setIndex("Nom");

    Series<?> index = dataframe.colonne.get("Index");
    assertEquals("Lisa", index.getData().get(0));
    assertEquals("Louis", index.getData().get(3));
    }

    // On s'assure que la colonne qui est devenue l'index a disparu
    @Test
    public void testSetIndexSupprimeAncienneColonne() {
        List<String> noms = List.of("Lisa", "Justine", "Eva", "Louis");

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);
        dataframe.setIndex("Nom");

        assertFalse(dataframe.colonne.containsKey("Nom"));
    }

    // On s'assure qu'on lève une exception si la colonne que l'on veut mettre en index n'existe pas
    @Test(expected = IllegalArgumentException.class)
    public void testSetIndexColonneInexistante() {
        List<String> noms = List.of("Lisa", "Justine", "Eva");

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);
        dataframe.setIndex("Prenom");
    }

    // On s'assure que la clé est toujours unique même si on applique un setIndex
    @Test(expected = IllegalArgumentException.class)
    public void testSetIndexAvecValeursDupliquees() {
        List<String> noms = List.of("Lisa", "Lisa", "Eva");

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));

        DataFrame df = new DataFrame(colonnes);
        df.setIndex("Nom");
    }

    // On s'assure que loc fonctionne avec la colonne "Index" classique
    @Test
    public void testLocAvecIndexNumeriqueParDefaut() {
        List<String> noms = List.of("Lisa", "Justine", "Eva");
        List<Integer> ages = List.of(25, 30, 22);

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));
        colonnes.put("Age", new Series<>(ages));

        DataFrame dataframe = new DataFrame(colonnes);

        DataFrame sousdataframe = dataframe.loc(List.of("0", "2"));

        assertEquals(2, sousdataframe.getColonneByName("Nom").size());
        assertEquals("Lisa", sousdataframe.getColonneByName("Nom").getData().get(0));
        assertEquals("Eva", sousdataframe.getColonneByName("Nom").getData().get(1));

        assertEquals("0", sousdataframe.getColonneByName("Index").getData().get(0).toString());
        assertEquals("2", sousdataframe.getColonneByName("Index").getData().get(1).toString());
    }

    // On teste avec un index qui n'existe pas avec la colonne "Index"
    @Test
    public void testLocAvecIndexNumeriqueInexistant() {
        List<String> noms = List.of("Lisa", "Justine", "Eva");

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);

        DataFrame sousdataframe = dataframe.loc(List.of("5"));

        assertEquals(0, sousdataframe.getColonneByName("Nom").size());
        assertEquals(0, sousdataframe.getColonneByName("Index").size());
    }

    // On s'assure que la méthode loc renvoie ce qui est attendu avec un appel à setIndex
    @Test
    public void testLocRetourneLignesCorrespondantes() {
        List<String> noms = List.of("Lisa", "Justine", "Eva");
        List<Integer> ages = List.of(25, 30, 22);

        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));
        colonnes.put("Age", new Series<>(ages));

        DataFrame dataframe = new DataFrame(colonnes);
        dataframe.setIndex("Nom");

        DataFrame sousdataframe = dataframe.loc(List.of("Lisa", "Eva"));

        assertEquals(2, sousdataframe.getColonneByName("Age").size());
        assertEquals(25, sousdataframe.getColonneByName("Age").getData().get(0));
        assertEquals(22, sousdataframe.getColonneByName("Age").getData().get(1));

        assertEquals("Lisa", sousdataframe.getColonneByName("Index").getData().get(0));
        assertEquals("Eva", sousdataframe.getColonneByName("Index").getData().get(1));
    }

    // On teste avec un index qui n'existe pas avec la méthode setIndex
    @Test
    public void testLocAucuneCorrespondance() {
        List<String> noms = List.of("Lisa", "Justine");
        List<Integer> ages = List.of(25, 30);
    
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(noms));
        colonnes.put("Age", new Series<>(ages));
    
        DataFrame dataframe = new DataFrame(colonnes);
        dataframe.setIndex("Nom");
    
        DataFrame sousdataframe = dataframe.loc(List.of("Louis"));
    
        assertEquals(0, sousdataframe.getColonneByName("Age").size());
        assertEquals(0, sousdataframe.getColonneByName("Index").size());
    }

    // On vérifie qu'iloc renvoie le résultat attendu (cas classique)
    @Test
    public void testIlocSelectionSansPas() {
        List<String> index = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> noms = new ArrayList<>(List.of("Lisa", "Justine", "Eva", "Louis"));
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(index));
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);

        DataFrame sousDataFrame = dataframe.iloc(1, 3, 0, 1);
        
        assertEquals(2, sousDataFrame.colonne.get("Nom").size());
        assertEquals("Justine", sousDataFrame.colonne.get("Nom").getData().get(0));
        assertEquals("Eva", sousDataFrame.colonne.get("Nom").getData().get(1));
    }

    // On vérifie qu'on lève une exception lorsqu'on tape dans un index hors limites
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIlocIndicesHorsLimites() {
        List<String> index = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> noms = new ArrayList<>(List.of("Lisa", "Justine", "Eva", "Louis"));
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(index));
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);

        dataframe.iloc(2, 6, 0, 1); // PB: 6
    }

    // On vérifie que la méthode de iloc avec slashing fonctionne comme attendue
    @Test
    public void testIlocAvecPas() {
        List<String> index = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> noms = new ArrayList<>(List.of("Lisa", "Justine", "Eva", "Louis"));
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(index));
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);

        DataFrame sousDataFrame = dataframe.iloc(0, 4, 2, 0, 1, 1);

        assertEquals(2, sousDataFrame.colonne.get("Nom").size());
        assertEquals("Lisa", sousDataFrame.colonne.get("Nom").getData().get(0));
        assertEquals("Eva", sousDataFrame.colonne.get("Nom").getData().get(1));
    }

    // On vérifie que la méthode de iloc avec slashing renvoie une erreur lorsque l'on accède à un index hors limites
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIlocAvecPasIndicesHorsLimites() {
        List<String> index = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> noms = new ArrayList<>(List.of("Lisa", "Justine", "Eva", "Louis"));
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Index", new Series<>(index));
        colonnes.put("Nom", new Series<>(noms));

        DataFrame dataframe = new DataFrame(colonnes);

        dataframe.iloc(0, 5, 2, 0, 1, 1); // PB: 5
    }

    // Préparation des tests de filtrage avancé
    @Before
    public void setUp() {
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();
        colonnes.put("Nom", new Series<>(List.of("Lisounette", "Louinounet", "Justinette", "Evanounette")));
        colonnes.put("Animaux", new Series<>(List.of("Leia", "Titi", "Grogro", "Loki")));
        colonnes.put("Activite", new Series<>(List.of("Coloriage", "Typst", "Manger", "Courir")));
        colonnes.put("Age", new Series<>(List.of(21, 19, 26, 30)));
        dataframeAttribut = new DataFrame(colonnes);
    }

    // On vérifie que le filtrage fonctionne avec une condition sur une colonne de int
    @Test
    public void testFiltreAgeSuperieurA25() {
        DataFrame resultat = dataframeAttribut.filter(ligne -> (int) ligne.get("Age") > 25);
        assertEquals(2, resultat.colonne.get("Nom").size());
        assertEquals("Justinette", resultat.colonne.get("Nom").getData().get(0));
        assertEquals("Evanounette", resultat.colonne.get("Nom").getData().get(1));
    }

    // On vérifie que le filtrage fonctionne avec une condition sur une colonne de string
    @Test
    public void testFiltreNomAlice() {
        DataFrame resultat = dataframeAttribut.filter(ligne -> ligne.get("Nom").equals("Lisounette"));
        assertEquals(1, resultat.colonne.get("Nom").size());
        assertEquals("Lisounette", resultat.colonne.get("Nom").getData().get(0));
    }

    // On vérifie que le filtrage fonctionne avec deux conditions sur deux colonnes différentes
    @Test
    public void testFiltreAgeEtActivite() {
        DataFrame resultat = dataframeAttribut.filter(ligne ->
            (int) ligne.get("Age") < 25 && ligne.get("Activite").equals("Typst"));

        assertEquals(1, resultat.colonne.get("Nom").size());
        assertEquals("Louinounet", resultat.colonne.get("Nom").getData().get(0));
    }

    // On vérifie que le filtrage fonctionne même lorsque les contraintes ne correspondent à aucune ligne
    @Test
    public void testAucunResultat() {
        DataFrame resultat = dataframeAttribut.filter(ligne -> ((int) ligne.get("Age")) > 100);
        assertEquals(0, resultat.colonne.get("Nom").size());
    }

    // On vérifie que le filtrage fonctionne même lorsque les contraintes  correspondent à toutes les lignes
    @Test
    public void testTousLesResultats() {
        DataFrame resultat = dataframeAttribut.filter(ligne -> true);
        assertEquals(4, resultat.colonne.get("Nom").size());
    }

    // Nettoyage de la préparation des tests de filtrage avancé
    @After
    public void tearDown() {
        dataframeAttribut = null;
    }
}
