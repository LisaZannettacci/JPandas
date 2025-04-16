
package com.jpandas;

import com.jpandas.core.DataFrame;
import com.jpandas.core.Series;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JPandas {
    public static void main(String[] args) {

        // Créer des données
        Map<String, Series<?>> colonnes = new LinkedHashMap<>();

		colonnes.put("Nom", new Series<>(List.of("Justine", "Colin", "Yoann", "Eva", "Rudy", "Lisa")));
        colonnes.put("Age", new Series<>(List.of(21, 14, 21, 26, 24, 21)));

        // Créer un DataFrame
        DataFrame df = new DataFrame(colonnes);

        // Démonstration de l'affichage :
		System.out.println("\n==================== AFFICHAGE ====================\n");
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


		// Démonstration de la sélection par index
		System.out.println("\n==================== SELECTION ====================\n");
		System.out.println("Sélection des lignes d'index 0 et 2 :");
		DataFrame sousdataframe = df.loc(List.of("0", "2"));
		sousdataframe.afficherTout();
		System.out.println("");
		
		System.out.println("La colonne Nom devient la colonne index :");
		DataFrame sousdataframeAutreIndex = new DataFrame(colonnes);
		sousdataframeAutreIndex.setIndex("Nom");
		sousdataframeAutreIndex.afficherTout();
		System.out.println("");

		System.out.println("Sélection des lignes d'index Justine et Colin :");		
		sousdataframe = sousdataframeAutreIndex.loc(List.of("Justine", "Colin"));
		sousdataframe.afficherTout();
		System.out.println("");

		System.out.println("Sélection par plage (index 1 à 4):");
		sousdataframe = df.iloc(1, 5, 1, 1);
		sousdataframe.afficherTout();
		System.out.println("");
		

		// Démonstration des filtres
		System.out.println("\n==================== FILTRAGE ====================\n");
		System.out.println("Sélection des lignes dont l'âge est supérieur strictement à 23 :");
		sousdataframe = df.filter(ligne -> (int) ligne.get("Age") > 23);
		sousdataframe.afficherTout();
		System.out.println("");

		System.out.println("Sélection des lignes dont l'âge est égal à 21 :");
		sousdataframe = df.filter(ligne -> (int) ligne.get("Age") == 21);
		sousdataframe.afficherTout();
		System.out.println("");

		System.out.println("Sélection des lignes dont le nom est Justine :");
		sousdataframe = df.filter(ligne -> ligne.get("Nom").equals("Justine"));
		sousdataframe.afficherTout();
		System.out.println("");


		// Démonstration des statistiques
		System.out.println("\n==================== STATISTIQUES ====================\n");
		df.afficherStatistiques("Age");
		System.out.println("");

    }
}
