package com.jpandas.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jpandas.core.DataFrame;
import com.jpandas.core.Series;

/**
 * La classe LecteurCSV est utilis&eacute;e pour lire les donn&eacute;es d'un fichier CSV
 * et les convertir en un DataFrame, ou chaque colonne du CSV devient une Series
 * dans le DataFrame.
 * 
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 * @see DataFrame
 */
public class LecteurCSV {

    /**
     * Constructeur par d&eacute;faut de la classe LecteurCSV.
     * Ce constructeur est utilis&eacute; pour cr&eacute;er une instance de la classe LecteurCSV sans param&egrave;tres.
     * <br>
     * Ce constructeur peut &ecirc;tre vide si aucune initialisation particuli&egrave;re n'est requise.
     */
    public LecteurCSV() {
        // Le constructeur par d&eacute;faut peut &ecirc;tre vide si aucune initialisation particuli&egrave;re n'est requise
    }

    /**
     * Cette m&eacute;thode lit toutes les lignes d'un fichier CSV et cr&eacute;e un DataFrame.
     * <p>
     * Principe : On commence par lire la premi&egrave;re ligne (les en-t&ecirc;tes) et on cr&eacute;e une Series pour chaque colonne du DataFrame.
     * Ensuite, on parcourt les lignes suivantes (les donn&eacute;es) et on ajoute chaque valeur dans la colonne appropri&eacute;e.
     * Si le CSV ne contient pas de colonne "Index", on l'ajoute.
     * </p>
     * 
     * @param path Chemin du fichier
     * @param dataframe Le dataframe qui va contenir les donn&eacute;es de notre CSV
     */
    public static void parseCSV(String path, DataFrame dataframe) {
        try {
            List<String> lignes = Files.readAllLines(Paths.get(path));
    
            if (lignes.size() < 2) {
                throw new IllegalArgumentException("Le fichier CSV est mal form&eacute;.");
            }
    
            String[] enTetes = lignes.get(0).split(",");
    
            // V&eacute;rifie si une colonne Index est d&eacute;j&agrave; pr&eacute;sente
            boolean indexColonne = false;
            for (String enTete : enTetes) {
                if (enTete.equalsIgnoreCase("Index")) {
                    indexColonne = true;
                    break;
                }
            }
    
            // On cr&eacute;e TOUTES les colonnes du fichier, y compris "Index" s&rsquo;il est d&eacute;j&agrave; l&agrave;
            for (String enTete : enTetes) {
                dataframe.colonne.put(enTete, new Series<>(new ArrayList<>()));
            }
    
            // On parcourt les lignes de donn&eacute;es et on les ajoute dans les bonnes colonnes
            for (int i = 1; i < lignes.size(); i++) {
                String[] valeurs = lignes.get(i).split(",");
                for (int j = 0; j < enTetes.length; j++) {
                    String nomColonne = enTetes[j];
                    Series<String> colonne = (Series<String>) dataframe.colonne.get(nomColonne);
                    if (colonne != null) {
                        colonne.getData().add(valeurs[j]);
                    }
                }
            }
    
            // Si le CSV n’a PAS de colonne Index, on en ajoute une automatiquement
            if (!indexColonne) {
                List<String> index = new ArrayList<>();
                for (int i = 0; i < lignes.size() - 1; i++) {
                    index.add(String.valueOf(i));
                }
                dataframe.colonne.put("Index", new Series<>(index));
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
