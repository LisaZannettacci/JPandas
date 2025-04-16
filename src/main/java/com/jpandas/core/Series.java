package com.jpandas.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * V&eacute;rifie si la s&eacute;rie contient uniquement des valeurs num&eacute;riques,
     * en ignorant les {@code null}.
     *
     * @return {@code true} si toutes les valeurs non nulles sont des {@code Number}, {@code false} sinon.
     */
    public boolean estNumerique() {
        return data.stream()
                .filter(Objects::nonNull)
                .allMatch(val -> val instanceof Number);
    }

    /**
     * Retourne les valeurs num&eacute;riques non nulles de la s&eacute;rie sous forme de {@code List<Double>}.
     *
     * @return Liste des valeurs num&eacute;riques valides, ou liste vide si aucune.
     */
    public List<Double> getValeursNumeriques() {
        if (!estNumerique()) {
            return Collections.emptyList();
        }

        return data.stream()
                .filter(Objects::nonNull)
                .map(val -> ((Number) val).doubleValue())
                .collect(Collectors.toList());
    }

    /**
     * Calcule la moyenne des valeurs num&eacute;riques de la s&eacute;rie.
     *
     * @return La moyenne sous forme de {@code double}, ou {@code NaN} si la s&eacute;rie n'est pas num&eacute;rique ou vide.
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.mean.html">Documentation Pandas - mean</a>
     */
    public double moyenne() {
        List<Double> valeurs = getValeursNumeriques();

        if (valeurs.isEmpty()) {
            return Double.NaN;
        } else {
            double somme = valeurs.stream().mapToDouble(Double::doubleValue).sum();
            return somme / valeurs.size();
        }
    }

    /**
     * Renvoie la plus petite valeur num&eacute;rique de la s&eacute;rie.
     *
     * @return La valeur minimale sous forme de {@code double}, ou {@code NaN} si non applicable.
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.min.html">Documentation Pandas - min</a>
     */
    public double minimum() {
        List<Double> valeurs = getValeursNumeriques();

        if (valeurs.isEmpty()) {
            return Double.NaN;
        } else {
            return valeurs.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);
        }
    }

    /**
     * Renvoie la plus grande valeur num&eacute;rique de la s&eacute;rie.
     *
     * @return La valeur maximale sous forme de {@code double}, ou {@code NaN} si non applicable.
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.max.html">Documentation Pandas - max</a>
     */
    public double maximum() {
        List<Double> valeurs = getValeursNumeriques();

        if (valeurs.isEmpty()) {
            return Double.NaN;
        } else {
            return valeurs.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
        }
    }

    /**
     * Calcule l'&eacute;cart-type des valeurs num&eacute;riques de la s&eacute;rie.
     *
     * @return L'&eacute;cart-type sous forme de {@code double}, ou {@code NaN} si la s&eacute;rie n'est pas num&eacute;rique ou contient moins de deux valeurs.
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.std.html">Documentation Pandas - std</a>
     */
    public double ecartType() {
        List<Double> valeurs = getValeursNumeriques();

        if (valeurs.size() < 2) {
            return Double.NaN;
        } else {
            double moyenne = valeurs.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double sommeDesCarres = valeurs.stream()
                .mapToDouble(val -> Math.pow(val - moyenne, 2))
                .sum();

            return Math.sqrt(sommeDesCarres / (valeurs.size() - 1));
        }
    }

}
