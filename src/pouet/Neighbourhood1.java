package pouet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import util.Pair;

/**
 * Voisinnage pour la recherche tabou appliquee a un probleme des n reines.
 * 
 * <p>
 * Le mouvement de voisinnage utilise est le deplacement d'une reine dans sa
 * ligne. L'exploration de ce voisinnage est totale.
 * </p>
 */
public class Neighbourhood1 extends Neighbourhood {
    
    List<Pair<Integer,Integer>> tabuList_ = new ArrayList<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    public Neighbourhood1(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public Solution findNeighbour(Solution sol, double proba) {
	List<Pair<Integer,Integer>> bestMoves = new ArrayList<Pair<Integer,Integer>>();
	int bestCost = sol.cost();
	
	Solution currentNeighbour = null;
	Pair<Integer,Integer> currentMove = null;
	
	this.neighbour_ = null;
	
	Random rand = new Random();
	boolean exploreTabuList = (rand.nextDouble() < proba);
	
	for (Integer row = 0; row < sol.size() ; ++row) {
	    for (Integer column = 0 ; column < sol.size(); ++column) {
		    currentMove = new Pair<Integer, Integer>(row, column);
		
		if((sol.get(row) != column) && (
			(this.tabuList_.contains(currentMove) == false)
				|| (exploreTabuList == true))) {
		    
		    currentNeighbour = new Solution(sol, currentMove);
		    
		    if(currentNeighbour.cost() <= bestCost) {
			if(currentNeighbour.cost() < bestCost) {
			    bestMoves.clear();
			}

			bestMoves.add(new Pair<Integer, Integer>(row, column));
			bestCost = currentNeighbour.cost();
		    }
		}
	    }
	}
	
	if (bestMoves.isEmpty() == false) {
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);

	    this.neighbour_ = new Solution(sol, move);
	    
	    this.addToTabuList(move);
	}
	
	return this.neighbour_;
    }
    
    private void addToTabuList(Pair<Integer,Integer> movement) {
	
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
    
    public void clearTabuList() {
	this.tabuList_.clear();
    }
    
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
