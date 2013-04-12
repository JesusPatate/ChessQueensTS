package object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.Pair;

/**
 * Voisinnage pour la recherche tabou appliquee a un probleme des n reines.
 * 
 * <p>
 * Le mouvement de voisinnage utilise est le deplacement d'une reine dans sa
 * ligne. L'exploration de ce voisinnage est partielle (seules les reines en
 * conflit peuvent être deplacees).
 * </p>
 */
public class Neighbourhood3 extends Neighbourhood {
    
    /**
     * Liste tabou.
     * 
     * <p>
     * Contient la liste des movements tabous, des paires (ligne, colonne).
     * </p>
     */
    Set<Pair<Integer, Integer>> tabuList_ =
	    new HashSet<Pair<Integer, Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    public Neighbourhood3(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public Solution findNeighbour(Solution sol, double proba) {
	List<Pair<Integer, Integer>> bestMoves =
		new ArrayList<Pair<Integer, Integer>>();

	int bestCost = sol.cost();
	Solution currentNeighbour = null;
	Pair<Integer, Integer> currentMove = null;

	// Passe a true lorsqu'un voisin "acceptable" est trouve
	boolean stop = false;

	this.neighbour_ = null;
	
	Random rand = new Random();
	boolean exploreTabuList = (rand.nextDouble() < proba);

	for(int row = 0 ; (row < sol.size()) && (stop == false) ; ++row) {
	    boolean conflict = isConflictual(sol, row);

	    // Seules les reines en conflit peuvent etre deplacees
	    if(conflict == true) {
		for(int column = 0 ; column < sol.size() ; ++column) {
		    if(sol.get(row) != column) {
			
			if((this.tabuList_.contains(currentMove) == false)
				|| (exploreTabuList == true)) {

			    currentMove = new Pair<Integer, Integer>(
				    row, column);
			    currentNeighbour = new Solution(
				    sol, currentMove);

			    if(currentNeighbour.cost() <= bestCost) {
				if(currentNeighbour.cost() < bestCost) {
				    bestMoves.clear();
				    stop = true;
				}

				bestMoves.add(new Pair<Integer, Integer>(
					currentMove));

				bestCost = currentNeighbour.cost();
			    }
			}
		    }
		}
	    }
	}

	// Selection aleatoire parmi tous les voisins "acceptables"
	// de meilleur cout
	if(bestMoves.isEmpty() == false) {
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);
	    
	    this.neighbour_ = new Solution(sol, move);
	    
	    this.addToTabuList(move);
	}
	
	return this.neighbour_;
    }
    
    private void addToTabuList(Pair<Integer, Integer> movement) {
	if(movement != null) {
	    if ((this.tabuList_.size() == this.tabuListSize_)
		    && (this.tabuListSize_ > 0)) {
		this.tabuList_.remove(0);
	    }

	    if (this.tabuListSize_ > 0) {
		this.tabuList_.add(movement);
	    }
	}
    }
    
    /**
     * Tests whether a queen is conflictual.
     *  
     * @param sol A solution
     * @param row The row index of the queen
     * 
     * @return
     */
    private boolean isConflictual(Solution sol, int row) {
	boolean conflict = false;
	
	for (int j = 0 ; (j < sol.size()) && (conflict == false) ; ++j) {
	    if ( (j != row) && (sol.get(row) == sol.get(j)) ) {
		conflict = true;
	    }
	}
	
	if(conflict == false) {
	    int[] aux = new int[sol.size()];

	    for (int i = 0 ; i < sol.size() ; ++i) {
		aux[i] = sol.get(i) + (row - i);
	    }

	    Solution s = new Solution(aux);

	    for (int j = 0 ; (j < sol.size()) && (conflict == false) ; ++j) {
		if ( (j != row) && (s.get(row) == s.get(j)) ) {
		    conflict = true;
		}
	    }
	    
	    if(conflict == false) {
		aux = new int[sol.size()];

		for (int i = 0 ; i < sol.size() ; ++i) {
		    aux[i] = sol.get(i) - (row - i);
		}

		s = new Solution(aux);

		for (int j = 0 ; (j < sol.size()) && (conflict == false) ; ++j) {
		    if ( (j != row) && (s.get(row) == s.get(j)) ) {
			conflict = true;
		    }
		}
	    }
	}
	
	return conflict;
    }
    
    @Override
    public int getTabuListSize() {
	return this.tabuListSize_;
    }

    @Override
    public Solution getNeighbour() {
	return this.neighbour_;
    }

    @Override
    public void clearTabuList() {
	this.tabuList_.clear();
    }

    @Override
    public String TabuListToString() {
	String output = new String();
	
	output += "{";
	
	for(Pair<Integer,Integer> p : this.tabuList_) {
	    output += p + " ";
	}
	
	output += "}";
	
	return output;
    }

    @Override
    public Solution exploreTabuList(Solution bestSol) {
	Solution res = null;
	boolean stop = false;
	
	Iterator<Pair<Integer,Integer>> it = this.tabuList_.iterator();
	
	while((stop == false) && it.hasNext()) {
	    Pair<Integer,Integer> move = it.next();
	    Solution s = new Solution(bestSol, move);
	    
	    if(s.cost() < bestSol.cost()) {
		res = (Solution) s.clone();
		stop = true;
		
		this.tabuList_.remove(move);
		this.tabuList_.add(move);
	    }
	}
	
	return res;
    }
    
    @Override
    public Solution degradeSolution(Solution sol) {
	Random rand = new Random();
	
	if(sol != null) {
	    int row = rand.nextInt(sol.size());
	    int column = rand.nextInt(sol.size());

	    sol.set(row, column);

	    addToTabuList(new Pair<Integer, Integer>(row,column));
	}
	
	return sol;
    }
}
