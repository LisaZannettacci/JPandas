package com.jpandas.core;

import java.util.List;

/**
 * Cette classe repr&eacute;sente une colonne du DataFrame, stocke les donn&eacute;es d'une colonne sous forme de liste.
 * <br>
 * <b>Param&eacute;trage :</b>
 * <ul>
 *   <li><code>&lt;T&gt;</code> : Le type des &eacute;l&eacute;ments stock&eacute;s dans la s&eacute;rie (ex: String, Integer, etc.).</li>
 * </ul>
 * 
 * @param <T> Le type des &eacute;l&eacute;ments stock&eacute;s dans la s&eacute;rie (ex: String, Integer, etc.).
 * 
 * @author Lisa ZANNETTACCI, REAT Justine
 * @version 1.0
 */
public class Series<T> {
    private List<T> data;

    /**
     * Constructeur pour initialiser une Series avec un param&egrave;tre "data".
     * 
     * @param data tableau ou une liste contenant les valeurs d'une colonne du DataFrame
     */
    public Series(List<T> data){
        this.data = data;
    }

    /**
     * Cette m&eacute;thode renvoie les valeurs de la s&eacute;rie.
     *
     * @return {@code List<T>} : les valeurs de la s&eacute;rie.
     */
    public List<T> getData(){
        return this.data;
    }

    /**
     * Cette m&eacute;thode renvoie la taille des s&eacute;ries.
     * 
     * @return int : taille des s&eacute;ries
     */
    public int size() {
        return data.size();
    }
}
