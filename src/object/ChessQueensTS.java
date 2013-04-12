package object;

import java.io.IOException;
import java.util.Random;


public class ChessQueensTS {

    /**
     * Nombre dfe reines
     */
    private int nQueens_;

    /**
     * Taille de la memoire tabou
     */
    private int memorySize_;
    
    /**
     * Probabilite d'explorer les voisins tabous
     * (aspiration condition)
     */
    private double pAspi_ = .0;
    
    public ChessQueensTS(final int nQueens, final int memorySize) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
    }
    
    public ChessQueensTS(final int nQueens, final int memorySize,
	    final double pAspi) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
	this.pAspi_ = pAspi;
    }

    public Solution search(final Neighbourhood neighbourhood,
	    final SolutionGenerator generator, final Integer nRuns,
	    final Data data, final boolean verbose)
	    throws IOException {

	Solution candidateSol = null;
	Solution bestSol = null;
	Solution currentSol = null;
	Solution neighbourSol = null;

	// Passé à true quand une solution optimale est trouvée
	Boolean stop = false;
	
	long cntIterations = 0;
	long cntRuns = 0;
	long startTime = 0;
	long endTime = 0;
	
	// Nombre de mouvements consécutifs sans amélioration
	Integer noImprovement = 0;

	if(verbose == true) {
	    System.out.println("\nNumber of queens : " + this.nQueens_);
	    System.out.println("Tabu list size : " + this.memorySize_);
	    System.out.println("Maximum number of runs : " + nRuns);
	}

	startTime = System.currentTimeMillis();

	// On continue tant qu'aucune solution n'a été trouvée
	// et que le nombre maximum de runs n'est pas atteint
	for (cntRuns = 0; cntRuns < nRuns && stop == false; cntRuns++) {
	    
	    if(verbose == true) {
		System.out.println("\nRun " + (cntRuns+1) + " :\n");
	    }

	    neighbourhood.clearTabuList();
	    noImprovement = 0;
	    cntIterations = 0;

	    // Génération de la solution initiale
	    currentSol = generator.generate(this.nQueens_, verbose);
	    bestSol = (Solution) currentSol.clone();
	    
	    // Passé à true quand on trouve un voisin au moins
	    // aussi bon que la meilleure solution connue
	    Boolean goOn = true;

	    if (verbose == true) {
		System.out.println("Initial cost : " + currentSol.cost());
	    }
	    
	    // Le run continue tant que l'on trouve un voisin
	    // au moins aussi bon que la solution courante
	    // et que il n'est pas une solution optimale
	    while (goOn && bestSol.cost() > 0) {
		goOn = false;
		++cntIterations;

		neighbourSol = neighbourhood.findNeighbour(currentSol, this.pAspi_);
		
		if(neighbourSol != null) {
		    if (neighbourSol.cost() < bestSol.cost()) {
			noImprovement = 0;

			if (verbose == true) {
			    System.out.println("New lower cost : "
				    + neighbourSol.cost());
			}
		    }

		    else {
			++noImprovement;
		    }

		    currentSol = neighbourSol;
		    bestSol = (Solution) currentSol.clone();

		    goOn = true;
		}
		
		else {
		    Solution s = neighbourhood.exploreTabuList(bestSol);
			
		    if(s != null) {
			System.out.println("DBG new cost = " + s.cost());

			currentSol = s;
			bestSol = (Solution) currentSol.clone();
			goOn = true;
		    }
		}

		if ((neighbourSol != null) && (noImprovement > this.memorySize_)) {
		    neighbourhood.degradeSolution(currentSol);
		    noImprovement = 0;

		    if (verbose == true) {
			System.out.println("Local minimum detected."
				+ "Solution degraded.");
		    }
		}
	    }
	    
	    if(bestSol.cost() == 0) {
		stop = true;
	    }
	    
	    else {
		if(verbose == true) {
		    System.out.println("No solution found.");
		}
	    }
	}
	
	endTime = System.currentTimeMillis();

	if (bestSol.cost() == 0) {
	    
	    if(verbose == true) {
		System.out.println("\nCandidate solution found in " + cntRuns
			+ " run(s) after " + (endTime - startTime) + " ms :");

		    bestSol.print();
		    System.out.println();
	    }

	    if (data != null) {
		data.add((endTime - startTime), cntRuns, cntIterations);
	    }
	}

	return candidateSol;
    }
}
