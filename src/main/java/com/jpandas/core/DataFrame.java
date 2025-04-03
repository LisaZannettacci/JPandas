package com.jpandas.core;

import java.util.HashMap;
import java.util.Map;

import com.jpandas.io.LecteurCSV;

/**
 * La classe DataFrame represente le tableau a deux dimensions, ou chaque colonne est une instance de la classe Series.
 * Un DataFrame peut se construire 
 *  - en specifiant directement les donnees de chaque colonne.
 *  - en lisant les donnees depuis un fichier CSV.
 *
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 * @see Series
 * 
 */
public class DataFrame {
    /**
     * Un objet {@code Map<String, Series<?>>} representant les colonnes du DataFrame, ou chaque cle est le nom de la colonne
     * et chaque valeur est une instance de la classe {@link Series} qui contient les donnees de cette colonne.
     */
    public Map<String, Series<?>> colonne;

    /**
     * Constructeur pour initialiser un DataFrame en specifiant les donnees de chaque colonne.
     *
     * @param colonne  Un {@code Map<String, Series<?>>} ou:
     *  - chaque cle est le nom de la colonne (par exemple "Age" ou "Name")
     *  - la valeur est une instance de la classe {@link Series} qui contient les donnees de cette colonne.
     * 
     */
    public DataFrame(Map<String, Series<?>> colonne) {
        this.colonne = colonne;
    }

    /**
     * Constructeur pour initialiser un DataFrame lisant les donnees depuis un fichier CSV.
     *
     * @param path  Le parametre "path" est un String
     * Il represente le chemin vers le fichier CSV que nous voulons passer en parametre.
     * 
     */
    public DataFrame(String path) {
        this.colonne = new HashMap<>();
        LecteurCSV.parseCSV(path, this);
    }

    /**
     * Methode pour recuperer une colonne specifique du DataFrame.
     *
     * @param nomColonne Le nom de la colonne que l'on souhaite recuperer.
     * @return La serie de donnees associee a ce nom de colonne, sous forme d'un objet `Series`.
     */
    public Series<?> GetColonne(String nomColonne) {
        return this.colonne.get(nomColonne);
    }

    /**
     *  Cette methode parcourt toutes les colonnes du DataFrame et utilise la methode print de chaque Series pour afficher les valeurs de la colonne.
     *
     */
    public void print() {
        for (String nomColonne: colonne.keySet()){
            System.out.println("Colonne: " + nomColonne);
            colonne.get(nomColonne).print();
        }
    }
}
