package com.jpandas.core;

import java.util.List;

/**
 * Cette classe représente une colonne du DataFrame, stocke les données d'une colonne sous forme de liste.
 *
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 */
public class Series<T> {
    private List<T> data;

    /**
     * Constructeur pour initialiser une Series avec un paramètre "data" qui est un tableau ou une liste contenant les valeurs d'une colonne du DataFrame.
     *
     * @param data
     */
    public Series(List<T> data){
        this.data = data;
    }

    /**
     * Cette méthode renvoie les valeurs de la série.
     *
     * @return  Une List<t> : les valeurs de la série.
     */
    public List<T> getData(){
        return data;
    }
    
    /**
     * Cette méthode imprime les valeurs de la série dans la console.
     *
     */
    public void print(){
        for (T value: data)
        System.out.println(value);
    }

    public int size() {
        return data.size();
    }
}
