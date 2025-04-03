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

    private int nbColonnes() {
        return colonne.size();
    }

    /**
     * Methode pour afficher les lignes, avec differents modes possibles :
     * mode 0 : afficher toutes les lignes (valeur de nb inutilisee)
     * mode 1 : afficher les nb premieres lignes
     * mode 2 : afficher les nb derniers lignes
     * @param nb
     * @param mode
     */
    public void afficherLignes(int nb, int mode) {
        Set<String> cles = colonne.keySet();
        HashMap<String, Integer> largeurs_colonnes = new HashMap<>();
        List<String> liste_cles = new ArrayList<String>();

        //pour determiner le format de l'affichage, on cherche la taille max dans chaque colonne
        for (String cle : cles) {
            liste_cles.add(cle);
            largeurs_colonnes.put(cle, 0);            
        }

        int nb_lignes_total = colonne.get(liste_cles.get(0)).size();
        int debut = 0;
        int nb_lignes_a_afficher = 0;

        if (mode == 0) { // affichage de toutes les lignes
            nb_lignes_a_afficher = nb_lignes_total;
            debut = 0;
        }
        else if (mode == 1) { // affichage des premieres lignes
            nb_lignes_a_afficher = nb;
            debut = 0;
        }
        else if (mode == 2) { // affichage des dernieres lignes
            nb_lignes_a_afficher = nb;
            debut = nb_lignes_total - nb;
        }
        
        int largeur;
        for (String cle : liste_cles) {
            largeur = 0;
            for (int i = debut; i < debut+nb_lignes_a_afficher; i++){
                largeur = Math.max(largeur, colonne.get(cle).getData().get(i).toString().length());
            }
            largeur = Math.max(largeur, cle.length()) + 2;
            largeurs_colonnes.put(cle, largeur);
        }

        // Affichage des noms des colonnes
        for (String cle : liste_cles) {
            System.out.printf("%-" + largeurs_colonnes.get(cle) + "s", cle);
        }
        int taille_mot = 0;
        String separateur = "";
        for (String cle : liste_cles) {
            taille_mot = cle.length();
            for (int i = 0; i<taille_mot; i++){
                separateur+="-";
            }
            System.out.printf("%-" + largeurs_colonnes.get(cle) + "s", separateur);
            separateur = "";
        }
        System.out.println();

        //Affichage des lignes
        for (int i = debut; i < debut+nb_lignes_a_afficher; i++){
            for (String cle : liste_cles) {
                String valeur = colonne.get(cle).getData().get(i).toString();
                System.out.printf("%-" + largeurs_colonnes.get(cle) + "s", valeur);
            }
            System.out.println();
        }
    }

    public void afficherTout(){
        afficherLignes(0, 0);
    }

    public void afficherPremieresLignes(int nb){
        afficherLignes(nb, 1);
    }

    public void afficherDernieresLignes(int nb){
        afficherLignes(nb, 2);
    }
}
