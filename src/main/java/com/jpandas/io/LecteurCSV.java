package com.jpandas.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jpandas.core.DataFrame;
import com.jpandas.core.Series;

/**
 * La classe LecteurCSV est utilisee pour lire les donnees d'un fichier CSV
 * et les convertir en un DataFrame, ou chaque colonne du CSV devient une Series
 * dans le DataFrame.
 * 
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 * @see DataFrame
 */
public class LecteurCSV {

    /**
     * Constructeur par defaut de la classe LecteurCSV.
     * Ce constructeur est utilise pour creer une instance de la classe LecteurCSV sans parametres.
     * 
     */
    public LecteurCSV() {
        // Le constructeur par defaut peut etre vide si aucune initialisation particuliere n'est requise
    }

    /**
     * Cette methode lit toutes les lignes d'un fichier CSV et cree un DataFrame.
     * Principe: On commence par lire la premiere ligne (les en-tetes) et on cree une Series pour chaque colonne du DataFrame.
     * Ensuite, on parcourt les lignes suivantes (les donnees) et on ajoute chaque valeur dans la colonne appropriee.
     * Si le csv ne contient pas de colonne "Index", on l'ajoute.
     * 
     * @param path Chemin du fichier
     * @param dataframe Le dataframe qui va contenir les donnees de notre CSV
     */
    public static void parseCSV(String path, DataFrame dataframe) {
        try {
            List<String> lignes = Files.readAllLines(Paths.get(path));
    
            if (lignes.size() < 2) {
                throw new IllegalArgumentException("Le fichier CSV est mal forme.");
            }
    
            String[] enTetes = lignes.get(0).split(",");
    
            // Verifie si une colonne Index est deja presente
            boolean indexColonne = false;
            for (String enTete : enTetes) {
                if (enTete.equalsIgnoreCase("Index")) {
                    indexColonne = true;
                    break;
                }
            }
    
            // On cree TOUTES les colonnes du fichier, y compris "Index" s’il est deja la
            for (String enTete : enTetes) {
                dataframe.colonne.put(enTete, new Series<>(new ArrayList<>()));
            }
    
            // On parcourt les lignes de donnees et on les ajoute dans les bonnes colonnes
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
