package pouet;

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
 * Le mouvement de voisinnage utilise est le swap de 2 reines (leurs colonnes
 * sont echangees). L'exploration de ce voisinnage est partielle (seules les
 * reines en conflit peuvent être deplacees).
 * </p>
 */
public class Neighbourhood4 extends Neighbourhood {
    
    Set<Pair<Integer,Integer>> tabuList_ = new HashSet<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    Pair<Integer,Integer> movement_ = null;
    
    public Neighbourhood4(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public Solution findNeighbour(Solution sol, double proba) {
	List<Pair<Integer, Integer>> bestMoves = new ArrayList<Pair<Integer, Integer>>();
	int bestCost = sol.cost();
	
	Pair<Integer, Integer> currentMove = null;
	
	// Passe a true lorsqu'un voisin "acceptable" est trouve
	boolean stop = false;
	
	this.neighbour_ = null;
	
	Random rand = new Random();
	boolean exploreTabuList = (rand.nextDouble() < proba);
	
	for(int row1 = 0 ; (row1 < sol.size()) && (stop == false) ; ++row1) {
	    boolean conflict = isConflictual(sol, row1);

	    // Seules les reines en conflit peuvent etre deplacees
	    // (cette condition n'est pas vraie la seconde reine du swap) 
	    if(conflict == true) {

		for(int row2 = 0 ; row2 < sol.size() ; ++row2) {
		    if(sol.get(row1) != row2) {
			currentMove = new Pair<Integer, Integer>(row1, row2);

			if((this.tabuList_.contains(currentMove) == false)
				|| (exploreTabuList == true)) {
			    
			    sol.swap(row1, row2);
			    
			    if(sol.cost() <= bestCost) {
				if(sol.cost() < bestCost) {
				    bestMoves.clear();
				}
	
				bestMoves.add(new Pair<Integer, Integer>(currentMove));
				bestCost = sol.cost();
				
				stop = true;
			    }
			    
			    sol.swap(row1, row2);
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
	    
	    this.neighbour_ = (Solution) sol.clone();
	    this.neighbour_.swap(move.getFirst(), move.getSecond());
	    
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
     * Teste si une reine d'une solution est en conflit.
     * 
     * @param sol
     *            Une solution d'un probleme des n reines
     * @param row
     *            L'index d'une reine de la solution
     * 
     * @return <ul>
     *         <li>true si la reine est menacee par une autre</li>
     *         <li>flase sinon</li>
     *         </ul>
     */
    private boolean isConflictual(Solution sol, int row) {
	boolean conflict = false;
	
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
	    Solution s = (Solution) bestSol.clone();
	    
	    s.swap(move.getFirst(), move.getSecond());
	    
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
	    int index1 = rand.nextInt(sol.size());
	    int index2 = rand.nextInt(sol.size());

	    sol.swap(index1, index2);

	    addToTabuList(new Pair<Integer, Integer>(index1, index2));
	}
	
	return sol;
    }
}
