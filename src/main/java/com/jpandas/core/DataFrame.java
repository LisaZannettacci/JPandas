package com.jpandas.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import com.jpandas.io.LecteurCSV;

/**
 * La classe {@code DataFrame} repr&eacute;sente un tableau &agrave; deux dimensions, 
 * o&ugrave; chaque colonne est une instance de la classe {@link Series}.<br>
 * <br>
 * Un {@code DataFrame} peut &ecirc;tre construit de deux fa&ccedil;ons :
 * <ul>
 *   <li>En sp&eacute;cifiant directement les donn&eacute;es de chaque colonne</li>
 *   <li>En lisant les donn&eacute;es depuis un fichier CSV</li>
 * </ul>
 * Si la colonne "Index" est absente, elle est g&eacute;n&eacute;r&eacute;e automatiquement avec des indices num&eacute;riques.
 *
 * @author Lisa ZANNETTACCI, Justine REAT
 * @version 1.0
 * @see Series
 */
public class DataFrame {
    
    /**
     * Un objet {@code Map<String, Series<?>>} repr&eacute;sentant les colonnes du DataFrame, 
     * o&ugrave; chaque cl&eacute; est le nom de la colonne et chaque valeur est une instance de 
     * la classe {@link Series} qui contient les donn&eacute;es de cette colonne.
     */
    public Map<String, Series<?>> colonne;

    /**
     * Constructeur pour initialiser un {@code DataFrame} &agrave; partir d'un ensemble de colonnes.<br>
     * <br>
     * Si la colonne "Index" est absente, elle est g&eacute;n&eacute;r&eacute;e automatiquement.
     * 
     * @param colonnes Un {@code Map<String, Series<?>>} o&ugrave; :
     *                 <ul>
     *                   <li>chaque cl&eacute; est le nom d'une colonne (par ex. "Age", "Nom")</li>
     *                   <li>chaque valeur est une instance de {@link Series} contenant les donn&eacute;es</li>
     *                 </ul>
     */
    public DataFrame(Map<String, Series<?>> colonnes) {
        this.colonne = new LinkedHashMap<>(colonnes);

        // Si la colonne "Index" n'existe pas, on la cr&eacute;e
        if (!this.colonne.containsKey("Index")) {
            List<String> indexList = new ArrayList<>();
            for (int i = 0; i < colonnes.values().iterator().next().size(); i++) {
                indexList.add(String.valueOf(i));  // Index auto-g&eacute;n&eacute;r&eacute; de 0 &agrave; n-1
            }
            this.colonne.put("Index", new Series<>(indexList));  // Ajout de la colonne "Index"
        }
        this.mettreIndexEnPremier();
    }

    /**
     * Constructeur pour initialiser un {@code DataFrame} lisant les donn&eacute;es depuis un fichier CSV.
     *
     * @param path Le param&egrave;tre "path" est une cha&icirc;ne de caract&egrave;res repr&eacute;sentant le chemin du fichier CSV &agrave; lire
     */
    public DataFrame(String path) {
        this.colonne = new LinkedHashMap<>();
        LecteurCSV.parseCSV(path, this);
        this.mettreIndexEnPremier();
    }

    /**
     * M&eacute;thode pour r&eacute;cup&eacute;rer une colonne sp&eacute;cifique du DataFrame.
     *
     * @param nomColonne Le nom de la colonne &agrave; r&eacute;cup&eacute;rer
     * @return L'objet {@link Series} correspondant, ou {@code null} si la colonne n'existe pas
     */
    public Series<?> getColonneByName(String nomColonne) {
        return this.colonne.get(nomColonne);
    }

    /**
     * M&eacute;thode qui renvoie le nombre de colonnes qui composent le DataFrame.
     * 
     * @return Le nombre de colonnes dans le DataFrame
     */
    public int getNbColonne() {
        return this.colonne.size();
    }

    /**
     * D&eacute;place la colonne "Index" en premi&egrave;re position dans le DataFrame, si elle existe.
     * Si la colonne "Index" n'existe pas, rien n'est fait.
     */
    public void mettreIndexEnPremier() {
        if (!colonne.containsKey("Index")) {
            return; // Rien &agrave; faire si la colonne Index n'existe pas
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

        // On remplace l'ancienne map par la nouvelle ordonn&eacute;e
        this.colonne = nouvelleColonnes;
    }

    /**
     * M&eacute;thode pour renvoyer les lignes &agrave; afficher, avec diff&eacute;rents modes possibles :
     * <ul>
     *   <li>mode 0 : toutes les lignes (valeur de nb inutilis&eacute;e)</li>
     *   <li>mode 1 : les {@code nb} premi&egrave;res lignes</li>
     *   <li>mode 2 : les {@code nb} derni&egrave;res lignes</li>
     * </ul>
     * 
     * @param nb   Le nombre de lignes &agrave; afficher
     * @param mode Le mode d'affichage (0, 1 ou 2)
     * @return Une cha&icirc;ne contenant les lignes format&eacute;es du DataFrame
     */
    public String afficherLignes(int nb, int mode) {
        Set<String> cles = colonne.keySet();
        LinkedHashMap<String, Integer> largeursColonnes = new LinkedHashMap<>();
        List<String> listeCles = new ArrayList<String>();
        String chaine = "";

        // Pour d&eacute;terminer le format de l'affichage, on cherche la taille max dans chaque colonne
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
        } else if (mode == 1) { // affichage des premi&egrave;res lignes
            nbLignesAAfficher = nb;
            debut = 0;
        } else if (mode == 2) { // affichage des derni&egrave;res lignes
            nbLignesAAfficher = nb;
            debut = nbLignesTotal - nb;
        }
        
        int largeur;
        for (String cle : listeCles) {
            largeur = 0;
            for (int i = debut; i < debut + nbLignesAAfficher; i++) {
                largeur = Math.max(largeur, colonne.get(cle).getData().get(i).toString().length());
            }
            largeur = Math.max(largeur, cle.length()) + 2;
            largeursColonnes.put(cle, largeur);
        }

        // Affichage des noms des colonnes
        for (String cle : listeCles) {
            chaine += String.format("%-" + largeursColonnes.get(cle) + "s", cle);
        }
        chaine += "\n";
        
        int tailleMot = 0;
        String separateur = "";
        for (String cle : listeCles) {
            tailleMot = cle.length();
            for (int i = 0; i < tailleMot; i++) {
                separateur += "-";
            }
            chaine += String.format("%-" + largeursColonnes.get(cle) + "s", separateur);
            separateur = "";
        }
        chaine += "\n";

        // Affichage des lignes
        for (int i = debut; i < debut + nbLignesAAfficher; i++) {
            for (String cle : listeCles) {
                String valeur = colonne.get(cle).getData().get(i).toString();
                chaine += String.format("%-" + largeursColonnes.get(cle) + "s", valeur);
            }
            chaine += "\n";
        }
        return chaine;
    }

    /**
     * M&eacute;thode qui affiche toutes les lignes du DataFrame.
     */
    public void afficherTout() {
        System.out.print(afficherLignes(0, 0));
    }

    /**
     * M&eacute;thode qui affiche les {@code nb} premi&egrave;res lignes du DataFrame.
     * 
     * @param nb Le nombre de lignes &agrave; afficher &agrave; partir du d&eacute;but
     */
    public void afficherPremieresLignes(int nb) {
        System.out.print(afficherLignes(nb, 1));
    }

    /**
     * M&eacute;thode qui affiche les {@code nb} derni&egrave;res lignes du DataFrame.
     * 
     * @param nb Le nombre de lignes &agrave; afficher &agrave; partir de la fin
     */
    public void afficherDernieresLignes(int nb) {
        System.out.print(afficherLignes(nb, 2));
    }


    /**
     * Affiche les statistiques descriptives (moyenne, minimum, maximum, &eacute;cart-type) 
     * pour une colonne sp&eacute;cifi&eacute;e du DataFrame si celle-ci est num&eacute;rique.
     * <p>
     * Si la colonne n'existe pas ou si elle n'est pas num&eacute;rique, un message 
     * appropri&eacute; est affich&eacute; &agrave; l'&eacute;cran.
     * </p>
     *
     * @param nomColonne le nom de la colonne sur laquelle ex&eacute;cuter les statistiques
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.describe.html">Documentation Pandas - describe</a>
     */
    public void afficherStatistiques(String nomColonne) {
        Series<?> serie = getColonneByName(nomColonne);
        if (serie == null) {
            System.out.println("Colonne non trouvée.");
            return;
        }
    
        if (!serie.estNumerique()) {
            System.out.println("La colonne n'est pas numérique.");
            return;
        }
    
        System.out.println("Statistiques pour la colonne \"" + nomColonne + "\" :");
        System.out.println("  Moyenne     : " + serie.moyenne());
        System.out.println("  Minimum     : " + serie.minimum());
        System.out.println("  Maximum     : " + serie.maximum());
        System.out.println("  Écart-type  : " + serie.ecartType());
    }
}
