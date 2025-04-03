package com.jpandas.core;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe DataFrame représente le tableau à deux dimensions, où chaque colonne est une instance de la classe Series.
 * Un DataFrame peut se construire 
 *  - en spécifiant directement les données de chaque colonne.
 *  - en lisant les données depuis un fichier CSV.
 *
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 * @see Series
 */
public class DataFrame {
    private Map<String, Series<?>> colonne;

    /**
     * Constructeur pour initialiser un DataFrame en spécifiant les données de chaque colonne.
     * Le paramètre "colonne" est un Map<String, Series<?>>,
     * Où 
     *  - chaque clé est le nom de la colonne (par exemple "Age" ou "Name")
     *  - la valeur est une instance de la classe Series qui contient les données de cette colonne.
     *
     * @param colonne
     */
    public DataFrame(Map<String, Series<?>> colonne) {
        this.colonne = colonne;
    }

    /**
     * Constructeur pour initialiser un DataFrame lisant les données depuis un fichier CSV.
     * Le paramètre "path" est un String
     * Il représente le chemin vers le fichier CSV que nous voulons passer en paramètre.
     *
     * @return  les valeurs de la série.
     */
    public DataFrame(String path) {
        this.colonne = new HashMap<>();
        // LecteurCSV.parseCSV(path, this);
    }

    /**
     *  Cette méthode parcourt toutes les colonnes du DataFrame et utilise la méthode print de chaque Series pour afficher les valeurs de la colonne.
     *
     */
    public void print() {
        for (String nomColonne: colonne.keySet()){
            System.out.println("Colonne: " + nomColonne);
            colonne.get(nomColonne).print();
        }
    }
}
