package com.jpandas.core;

import java.util.LinkedHashMap;
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
 * @author Lisa ZANNETTACCI, Justine REAT
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
     * Si le dataframe n'a pas de colonne "Index", on l'ajoute
     *
     * @param colonne  Un {@code Map<String, Series<?>>} ou:
     *  - chaque cle est le nom de la colonne (par exemple "Age" ou "Name")
     *  - la valeur est une instance de la classe {@link Series} qui contient les donnees de cette colonne.
     * 
     */
    public DataFrame(Map<String, Series<?>> colonnes) {
        this.colonne = new LinkedHashMap<>(colonnes);

        // Si la colonne "Index" n'existe pas, on la cree
        if (!this.colonne.containsKey("Index")) {
            List<String> indexList = new ArrayList<>();
            for (int i = 0; i < colonnes.values().iterator().next().size(); i++) {
                indexList.add(String.valueOf(i));  // Index auto-genere de 0 a n-1
            }
            this.colonne.put("Index", new Series<>(indexList));  // Ajout de la colonne "Index"
        }
        this.mettreIndexEnPremier();
    }

    /**
     * Constructeur pour initialiser un DataFrame lisant les donnees depuis un fichier CSV.
     *
     * @param path  Le parametre "path" est un String
     * Il represente le chemin vers le fichier CSV que nous voulons passer en parametre.
     * 
     */
    public DataFrame(String path) {
        this.colonne = new LinkedHashMap<>();
        LecteurCSV.parseCSV(path, this);
        this.mettreIndexEnPremier();
    }

    /**
     * Methode pour recuperer une colonne specifique du DataFrame.
     *
     * @param nomColonne Le nom de la colonne que l'on souhaite recuperer.
     * @return La serie de donnees associee a ce nom de colonne, sous forme d'un objet `Series`.
     */
    public Series<?> getColonneByName(String nomColonne) {
        return this.colonne.get(nomColonne);
    }

    /**
     * Metode qui renvoie le nombre de colonne qui compose le Dataframe
     * @return le nombre de colonne qui compose le Dataframe
     */
    public int getNbColonne() {
        return this.colonne.size();
    }

    /**
     * Deplace la colonne "Index" en premiere position dans le DataFrame, si elle existe.
     */
    public void mettreIndexEnPremier() {
        if (!colonne.containsKey("Index")) {
            return; // Rien a faire si la colonne Index n'existe pas
        }

        // On sauvegarde la colonne Index
        Series<?> indexColonne = colonne.remove("Index");

        // Nouvelle map pour stocker les colonnes dans le bon ordre
        LinkedHashMap<String, Series<?>> nouvelleColonnes = new LinkedHashMap<>();

        // On met d'abord Index
        nouvelleColonnes.put("Index", indexColonne);

        // Puis les autres colonnes dans l'ordre actuel, sauf "Index"
        for (Map.Entry<String, Series<?>> entry : colonne.entrySet()) {
            nouvelleColonnes.put(entry.getKey(), entry.getValue());
        }

        // On remplace l'ancienne map par la nouvelle ordonnee
        this.colonne = nouvelleColonnes;
    }

    /**
     * Methode pour renvoyer les lignes a afficher, avec differents modes possibles :
     * mode 0 : toutes les lignes (valeur de nb inutilisee)
     * mode 1 : les nb premieres lignes
     * mode 2 : les nb dernieres lignes
     * @param nb le nombre de lignes que l'on veut afficher
     * @param mode le mode d'affichage
     */
    public String afficherLignes(int nb, int mode) {
        Set<String> cles = colonne.keySet();
        LinkedHashMap<String, Integer> largeursColonnes = new LinkedHashMap<>();
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
