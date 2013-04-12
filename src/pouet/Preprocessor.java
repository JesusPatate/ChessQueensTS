package pouet;

public interface Preprocessor {
    
    /**
     * Applique le preprocessing a une solution d'un probleme des n reines.
     * 
     * @param sol
     *            Une solution d'un probleme des n reines
     * @param verbose
     *            Si l'affichage en console est active
     * 
     * @return Une nouvelle solution obtenue a partir de celle passee en
     *         argument
     */
    public Solution preprocess(Solution sol, boolean verbose);
}
