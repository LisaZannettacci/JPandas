package com.jpandas.core;

import java.util.List;

/**
 * Cette classe represente une colonne du DataFrame, stocke les donnees d'une colonne sous forme de liste.
 * 
 * @param <T> Le type des elements stockes dans la serie (ex: String, Integer, etc.).
 *
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 *
 */
public class Series<T> {
    private List<T> data;

    /**
     * Constructeur pour initialiser une Series avec un parametre "data".
     * @param data tableau ou une liste contenant les valeurs d'une colonne du DataFrame
     */
    public Series(List<T> data){
        this.data = data;
    }

    /**
     * Cette methode renvoie les valeurs de la serie.
     *
     * @return {@code List<T>} : les valeurs de la serie.
     * 
     */
    public List<T> getData(){
        return this.data;
    }

    /**
     * Cette methode renvoie la taille des series
     * @return int : taille des series
     */
    public int size() {
        return data.size();
    }
}
