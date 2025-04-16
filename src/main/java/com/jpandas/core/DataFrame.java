package com.jpandas.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

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
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.head.html">pandas.DataFrame.head()</a>
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.tail.html">pandas.DataFrame.tail()</a>
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
     * 
     * @see <a href="https://pandas.pydata.org/docs/user_guide/options.html#maximum-number-of-rows-displayed">Pandas - Displaying all rows</a>
     */
    public void afficherTout() {
        System.out.print(afficherLignes(0, 0));
    }

    /**
     * M&eacute;thode qui affiche les {@code nb} premi&egrave;res lignes du DataFrame.
     * 
     * @param nb Le nombre de lignes &agrave; afficher &agrave; partir du d&eacute;but
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.head.html">pandas.DataFrame.head()</a>
     */
    public void afficherPremieresLignes(int nb) {
        System.out.print(afficherLignes(nb, 1));
    }

    /**
     * M&eacute;thode qui affiche les {@code nb} derni&egrave;res lignes du DataFrame.
     * 
     * @param nb Le nombre de lignes &agrave; afficher &agrave; partir de la fin
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.tail.html">pandas.DataFrame.tail()</a>
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
    
    /**
     * Remplace la colonne "Index" actuelle par une autre colonne sp&eacute;cifi&eacute;e.<br>
     * La colonne utilis&eacute;e devient le nouvel index, et est retir&eacute;e des colonnes visibles.<br><br>
     *
     * Fonctionne comme en Pandas : <code>df.set_index("Nom")</code><br>
     *
     * @param nomColonne Le nom de la colonne &agrave; utiliser comme nouvel index
     * 
     * @throws IllegalArgumentException si la colonne n&apos;existe pas ou contient des doublons
     *
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.set_index.html">Documentation Pandas - set_index</a>
     */
    public void setIndex(String nomColonne) {
        Series<?> nouvelleColonneIndex = colonne.get(nomColonne);
        if (nouvelleColonneIndex == null) {
            throw new IllegalArgumentException("La colonne spécifiée n'existe pas : " + nomColonne);
        }

        Set<Object> valeursUniquess = new HashSet<>(nouvelleColonneIndex.getData());
        if (valeursUniquess.size() != nouvelleColonneIndex.size()) {
            throw new IllegalArgumentException("La colonne '" + nomColonne + "' contient des valeurs en double, elle ne peut pas être utilisée comme index.");
        }

        List<Object> index = new ArrayList<>(nouvelleColonneIndex.getData());
        colonne.put("Index", new Series<>(index));

        colonne.remove(nomColonne);
    }
  
    /**
     * Renvoie un sous-{@code DataFrame} contenant uniquement les lignes dont la valeur
     * de l&apos;index est pr&eacute;sente dans la liste pass&eacute;e en param&egrave;tre.<br><br>
     *
     * Fonctionne comme en Pandas : <code>df.loc[["valeur1", "valeur2"]]</code><br>
     *
     * @param valeursIndex Liste des valeurs d&apos;index &agrave; s&eacute;lectionner
     * 
     * @return Un nouveau {@code DataFrame} contenant uniquement les lignes correspondant aux index donn&eacute;s
     * 
     * @throws IllegalStateException si la colonne "Index" est absente du {@code DataFrame}
     *
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.loc.html">Documentation Pandas - loc</a>
     */

    public DataFrame loc(List<String> valeursIndex) {
        Series<?> indexSeries = colonne.get("Index");
        if (indexSeries == null) {
            throw new IllegalStateException("Aucune colonne 'Index' trouvée.");
        }
    
        Map<String, Series<?>> nouvellesColonnes = new LinkedHashMap<>();
        for (String nomCol : colonne.keySet()) {
            nouvellesColonnes.put(nomCol, new Series<Object>(new ArrayList<>()));
        }
    
        for (int i = 0; i < indexSeries.size(); i++) {
            String valeurIndex = indexSeries.getData().get(i).toString();
            if (valeursIndex.contains(valeurIndex)) {
                for (String nomCol : colonne.keySet()) {
                    ((List<Object>) nouvellesColonnes.get(nomCol).getData()).add(colonne.get(nomCol).getData().get(i));
                }
            }
        }
    
        return new DataFrame(nouvellesColonnes);
    }

    /**
     * <p>
     * Retourne un sous-ensemble du DataFrame en s&eacute;lectionnant une plage de lignes et toutes les colonnes.
     * </p>
     * <p>
     * Cette m&eacute;thode reproduit le comportement de la m&eacute;thode <code>iloc</code> de la biblioth&egrave;que Pandas :
     * elle s&eacute;lectionne les lignes par leurs positions (et non par leurs valeurs d'index).
     * </p>
     *
     * @param startRow Position de d&eacute;part (incluse) des lignes &agrave; s&eacute;lectionner
     * @param endRow Position de fin (exclue) des lignes &agrave; s&eacute;lectionner
     * @param startCol Position de d&eacute;part (incluse) des colonnes &agrave; s&eacute;lectionner
     * @param endCol Position de fin (exclue) des colonnes &agrave; s&eacute;lectionner
     * 
     * @return Un nouveau DataFrame contenant uniquement les lignes et colonnes s&eacute;lectionn&eacute;es
     * 
     * @throws IndexOutOfBoundsException si une position est hors limites
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.iloc.html">Documentation Pandas - iloc</a>
     */

    public DataFrame iloc(int startRow, int endRow, int startCol, int endCol) {
        if (startRow < 0 || endRow > colonne.get("Index").size() || startCol < 0 || endCol > getNbColonne()) {
            throw new IndexOutOfBoundsException("Les indices sont hors limites.");
        }
    
        Map<String, Series<?>> nouvellesColonnes = new LinkedHashMap<>();
        
        for (String nomCol : colonne.keySet()) {
            nouvellesColonnes.put(nomCol, new Series<Object>(new ArrayList<>()));
        }
        
        for (int i = startRow; i < endRow; i++) {
            for (String nomCol : colonne.keySet()) {
                ((List<Object>) nouvellesColonnes.get(nomCol).getData()).add(colonne.get(nomCol).getData().get(i));
            }
        }
    
        return new DataFrame(nouvellesColonnes);
    }
    

    /**
     * <p>
     * Retourne un sous-ensemble du DataFrame en s&eacute;lectionnant une plage de lignes et de colonnes avec un intervalle de pas.
     * </p>
     * <p>
     * Cette version avanc&eacute;e de <code>iloc</code> permet de sp&eacute;cifier un pas pour l’it&eacute;ration sur les lignes et colonnes,
     * &agrave; la mani&egrave;re du slicing avec step dans Pandas (ex: <code>df.iloc[::2, ::2]</code>).
     * </p>
     *
     * @param startRow Position de d&eacute;part (incluse) pour les lignes
     * @param endRow Position de fin (exclue) pour les lignes
     * @param stepRow Pas pour l’it&eacute;ration sur les lignes (&gt; 0)
     * @param startCol Position de d&eacute;part (incluse) pour les colonnes
     * @param endCol Position de fin (exclue) pour les colonnes
     * @param stepCol Pas pour l’it&eacute;ration sur les colonnes (&gt; 0)
     * 
     * @return Un nouveau DataFrame avec les lignes et colonnes s&eacute;lectionn&eacute;es en fonction des pas
     * 
     * @throws IndexOutOfBoundsException si une position est hors des dimensions du DataFrame
     * 
     * @see <a href="https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.iloc.html">Documentation Pandas - iloc</a>
     */
    public DataFrame iloc(int startRow, int endRow, int stepRow, int startCol, int endCol, int stepCol) {
        if (startRow < 0 || endRow > colonne.get("Index").size() || startCol < 0 || endCol > getNbColonne()) {
            throw new IndexOutOfBoundsException("Les indices sont hors limites.");
        }
    
        Map<String, Series<?>> nouvellesColonnes = new LinkedHashMap<>();
        
        for (String nomCol : colonne.keySet()) {
            nouvellesColonnes.put(nomCol, new Series<Object>(new ArrayList<>()));
        }
        
        for (int i = startRow; i < endRow; i += stepRow) {
            for (int j = startCol; j < endCol; j += stepCol) {
                for (String nomCol : colonne.keySet()) {
                    ((List<Object>) nouvellesColonnes.get(nomCol).getData()).add(colonne.get(nomCol).getData().get(i));
                }
            }
        }
    
        return new DataFrame(nouvellesColonnes);
    }   
       

    /**
     * Filtre les lignes du {@code DataFrame} en fonction d'un crit&egrave;re donn&eacute;.
     * <br><br>
     * Cette m&eacute;thode permet de conserver uniquement les lignes qui v&eacute;rifient une condition sp&eacute;cifi&eacute;e 
     * par un pr&eacute;dicat {@link Predicate}. Chaque ligne est repr&eacute;sent&eacute;e sous forme d'une map 
     * {@code Map<String, Object>} o&ugrave; :
     * <ul>
     *   <li>chaque cl&eacute; est le nom d'une colonne</li>
     *   <li>chaque valeur est la donn&eacute;e correspondante &agrave; cette colonne pour une ligne donn&eacute;e</li>
     * </ul>
     * <br>
     * Exemple d'utilisation :
     * <pre>{@code
     * DataFrame dfFiltr&eacute; = df.filter(ligne -> ((int) ligne.get("Age")) > 30);
     * }</pre>
     * 
     * @param critere Le pr&eacute;dicat {@link Predicate} &agrave; appliquer &agrave; chaque ligne. 
     *                Si le crit&egrave;re retourne {@code true}, la ligne est conserv&eacute;e dans le nouveau DataFrame.
     * 
     * @return Un nouveau {@code DataFrame} contenant uniquement les lignes qui satisfont le crit&egrave;re.
     * 
     * @see Predicate
     */
    public DataFrame filter(Predicate<Map<String, Object>> critere) {
        List<Map<String, Object>> lignesFiltrees = new ArrayList<>();

        // Pour chaque ligne, on applique le critère
        for (int i = 0; i < this.colonne.get("Index").size(); i++) {
            Map<String, Object> ligne = new LinkedHashMap<>();
            for (String cle : this.colonne.keySet()) {
                ligne.put(cle, this.colonne.get(cle).getData().get(i));
            }
            
            if (critere.test(ligne)) {
                lignesFiltrees.add(ligne);
            }
        }

        // Maintenant on recrée un DataFrame avec les lignes filtrées
        Map<String, Series<?>> nouvellesColonnes = new LinkedHashMap<>();
        for (String cle : this.colonne.keySet()) {
            List<Object> donnees = new ArrayList<>();
            for (Map<String, Object> ligne : lignesFiltrees) {
                donnees.add(ligne.get(cle));
            }
            nouvellesColonnes.put(cle, new Series<>(donnees));
        }

        return new DataFrame(nouvellesColonnes);
    }
}
