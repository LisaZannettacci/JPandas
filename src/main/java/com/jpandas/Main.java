package com.jpandas;

import com.jpandas.core.DataFrame;
import com.jpandas.core.Series;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // Créer des données
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();

		colonnes.put("Nom", new Series<>(Arrays.asList("Alice", "Bob", "Justine", "Yoann", "Eva", "Rudy")));
        colonnes.put("Age", new Series<>(Arrays.asList("12", "17", "21", "21", "26", "24")));

        // Créer un DataFrame
        DataFrame df = new DataFrame(colonnes);

        // Afficher le DataFrame
        System.out.println("Affichage du DataFrame complet :");
        df.afficherTout();
        System.out.println("");

        // Tester l'affichage des premières lignes
        System.out.println("Affichage des deux premières lignes :");
        df.afficherPremieresLignes(2);
        System.out.println("");

        // Tester l'affichage des dernières lignes
        System.out.println("Affichage des trois dernières lignes :");
        df.afficherDernieresLignes(3);
        System.out.println("");
    }
}
