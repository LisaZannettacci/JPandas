package com.jpandas.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.jpandas.io.LecteurCSV;

/**
 * La classe DataFrame represente le tableau a deux dimensions, ou chaque colonne est une instance de la classe Series.
 * Un DataFrame peut se construire 
 *  - en specifiant directement les donnees de chaque colonne.
 *  - en lisant les donnees depuis un fichier CSV.
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
    public Series<?> getColonne(String nomColonne) {
        return this.colonne.get(nomColonne);
    }

    private int nbColonnes() {
        return colonne.size();
    }

    /**
     * Methode pour renvoyer les lignes a afficher, avec differents modes possibles :
     * mode 0 : toutes les lignes (valeur de nb inutilisee)
     * mode 1 : les nb premieres lignes
     * mode 2 : les nb derniers lignes
     * @param nb le nombre de lignes que l'on veut afficher
     * @param mode le mode d'affichage
     */
    private String afficherLignes(int nb, int mode) {
        Set<String> cles = colonne.keySet();
        HashMap<String, Integer> largeursColonnes = new HashMap<>();
        List<String> listeCles = new ArrayList<String>();
        String chaine = "";

        //pour determiner le format de l'affichage, on cherche la taille max dans chaque colonne
        for (String cle : cles) {
            listeCles.add(cle);
            largeursColonnes.put(cle, 0);            
        }

        int nbLignesTotal = colonne.get(listeCles.get(0)).size();
        int debut = 0;
        int nbLignesAAfficher = 0;

        if (mode == 0) { // affichage de toutes les lignes
            nbLignesAAfficher = nbLignesTotal;
            debut = 0;
        }
        else if (mode == 1) { // affichage des premieres lignes
            nbLignesAAfficher = nb;
            debut = 0;
        }
        else if (mode == 2) { // affichage des dernieres lignes
            nbLignesAAfficher = nb;
            debut = nbLignesTotal - nb;
        }
        
        int largeur;
        for (String cle : listeCles) {
            largeur = 0;
            for (int i = debut; i < debut+nbLignesAAfficher; i++){
                largeur = Math.max(largeur, colonne.get(cle).getData().get(i).toString().length());
            }
            largeur = Math.max(largeur, cle.length()) + 2;
            largeursColonnes.put(cle, largeur);
        }

        // Affichage des noms des colonnes
        for (String cle : listeCles) {
            chaine += String.format("%-" + largeursColonnes.get(cle) + "s", cle);
            // System.out.printf("%-" + largeursColonnes.get(cle) + "s", cle);
        }
        chaine += "\n";
        // System.out.println();
        
        int tailleMot = 0;        
        String separateur = "";
        for (String cle : listeCles) {
            tailleMot = cle.length();
            for (int i = 0; i<tailleMot; i++){
                separateur+="-";
            }
            chaine += String.format("%-" + largeursColonnes.get(cle) + "s", separateur);
            // System.out.printf("%-" + largeursColonnes.get(cle) + "s", separateur);
            separateur = "";
        }
        chaine += "\n";
        // System.out.println();
        

        //Affichage des lignes
        for (int i = debut; i < debut+nbLignesAAfficher; i++){
            for (String cle : listeCles) {
                String valeur = colonne.get(cle).getData().get(i).toString();
                chaine += String.format("%-" + largeursColonnes.get(cle) + "s", valeur);
                // System.out.printf("%-" + largeursColonnes.get(cle) + "s", valeur);
            }
            chaine += "\n";
            // System.out.println();
        }
        return chaine;
    }

    /**
     * Methode qui affiche toutes les lignes du dataframe
     */
    public void afficherTout() {
        System.out.print(afficherLignes(0, 0));
    }

    /**
     * Methode qui affiche les nb premieres lignes du dataframe
     * @param nb le nombre de lignes que l'on veut afficher en partant du debut
     */
    public void afficherPremieresLignes(int nb) {
        System.out.print(afficherLignes(nb, 1));
    }

    /**
     * Methode qui affiche les nb dernieres lignes du dataframe
     * @param nb le nombre de lignes que l'on veut afficher en partant de la fin
     */
    public void afficherDernieresLignes(int nb) {
        System.out.print(afficherLignes(nb, 2));
    }
}
