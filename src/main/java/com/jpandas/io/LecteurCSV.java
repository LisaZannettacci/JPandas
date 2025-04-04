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
     * 
     * @param path Chemin du fichier
     * @param dataframe Le dataframe qui va contenir les donnees de notre CSV
     */
    public static void parseCSV(String path, DataFrame dataframe)
    {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            // Verification du format CSV
            // Si moins de 2 lignes => pas de donnees ou pas d'en-tetes
            if (lines.size() < 2) {
                throw new IllegalArgumentException("Le fichier CSV est mal forme.");
            }

            String[] enTetes = lines.get(0).split(",");

            // Pour chaque nom de colonne, on cree une nouvelle Series vide et on l'ajoute a la Map des colonnes du DataFrame.
            for (String enTete: enTetes){
                dataframe.colonne.put(enTete, new Series<>(new ArrayList<>()));
            }

            // On parcourt les lignes de donnees et on les ajoute dans le DataFrame
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                for (int j = 0; j < enTetes.length; j++) {
                    String value = values[j];
                    Series<?> column = dataframe.colonne.get(enTetes[j]);
                    if (column != null) {
                        ((Series<String>) column).getData().add(value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
