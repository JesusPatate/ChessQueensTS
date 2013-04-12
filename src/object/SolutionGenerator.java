package object;


public interface SolutionGenerator {
    
    /**
     * Genere une solution initiale a un probleme des n reines
     * 
     * @param size
     *            Le nombre de reines du problem
     * @param verbose
     *            Si l'affiche en console est active
     * 
     * @return Une solution
     */
    public Solution generate(Integer size, boolean verbose);
}
