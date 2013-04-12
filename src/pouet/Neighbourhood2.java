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
 * sont echangees). L'exploration de ce voisinnage est totale.
 * </p>
 */
public class Neighbourhood2 extends Neighbourhood {
    
    Set<Pair<Integer,Integer>> tabuList_ = new HashSet<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    Pair<Integer,Integer> movement_ = null;
    
    public Neighbourhood2(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }

    @Override
    public Solution findNeighbour(Solution sol, double proba) {
	
	int bestCost = sol.cost();
	List<Pair<Integer, Integer>> bestMoves =
		new ArrayList<Pair<Integer, Integer>>();
	
	Pair<Integer, Integer> currentMove = null;
	
	this.neighbour_ = null;
	
	Random rand = new Random();
	boolean exploreTabuList = (rand.nextDouble() < proba);
	
	for(Integer row1 = 0 ; row1 < sol.size()-1 ; ++row1) {
	    for(Integer row2 = row1+1 ; row2 < sol.size() ; ++row2) {
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
		    }
		    
		    sol.swap(row1, row2);
		}
	    }
	}
	
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
