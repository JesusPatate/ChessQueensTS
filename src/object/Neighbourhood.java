package object;

/**
 * Définit un voisinnage pour la recherche tabou appliquee
 * a un probleme des n reines.
 */
public abstract class Neighbourhood {
    
    /**
     * Recherche un voisin "acceptable" d'une solution
     * d'un probleme des n reines.
     * 
     * @param sol
     *            Une solution d'un probleme des n reines
     * @param proba
     * 		Probabilite d'explorer les voisins tabous
     * 
     * @return Le voisin trouve ou null 
     */
    public abstract Solution findNeighbour(Solution sol, double proba);
    
    /**
     * Retourne la capacite de la liste tabou.
     * 
     * @return La taille maximale de la lise tabou
     */
    public abstract int getTabuListSize();
    
    /**
     * Retourne le voisin trouvé par le dernier appel
     * a findNeighbour()
     * 
     * @return Un voisin ou null
     */
    public abstract Solution getNeighbour();
    
    /**
     * Vide la liste tabou.
     */
    public abstract void clearTabuList();
    
    /**
     * Renvoie une representation en chaine de caracteres de la liste tabou.
     * 
     * <p>La chaine est de la forme "{mvt_1, ..., mvt_n}".</p>
     * 
     * @return
     * 		La liste tabou sous forme d'une chaine de caracteres.
     */
    public abstract String TabuListToString();
    
    /**
     * Explore les voisins tabous d'une solution.
     * 
     * @param sol
     *            Une solution d'un probleme des n reines
     * 
     * @return Une solution correspondant a un voisin tabou dont le cout est
     *         inferieur a celui de la solution passee en argument ou null
     */
    public abstract Solution exploreTabuList(Solution sol);
    
    /**
     * Degrade une solution en lui appliquant un mouvement aleatoire.
     * 
     * @param sol
     *            Une solution d'un probleme des n reines
     *            
     * @return La solution degradee
     */
    public abstract Solution degradeSolution(Solution sol);
}
