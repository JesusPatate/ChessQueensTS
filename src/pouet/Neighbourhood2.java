package pouet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.Pair;


public class Neighbourhood2 extends Neighbourhood {
    
    Set<Pair<Integer,Integer>> tabuList_ = new HashSet<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    Pair<Integer,Integer> movement_ = null;
    
    public Neighbourhood2(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }

    @Override
    public boolean findBestNeighbour(Solution sol) {
	
	int bestCost = sol.cost();
	List<Pair<Integer, Integer>> bestMoves =
		new ArrayList<Pair<Integer, Integer>>();
	
	Pair<Integer, Integer> currentMove = null;
	
	for(Integer row1 = 0 ; row1 < sol.size()-1 ; ++row1) {
	    for(Integer row2 = row1+1 ; row2 < sol.size() ; ++row2) {
		
		currentMove = new Pair<Integer, Integer>(row1, row2);
		
		if(this.tabuList_.contains(currentMove) == false) {
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
	    Random rand = new Random();
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);
	    
	    this.neighbour_ = (Solution) sol.clone();
	    this.neighbour_.swap(move.getFirst(), move.getSecond());
	    
	    this.movement_ = move;
	}
	
	return (bestMoves.isEmpty() == false);
    }

    @Override
    public void addToTabuList() throws Exception {
	if(this.movement_ != null) {
	    if ((this.tabuList_.size() == this.tabuListSize_)
		    && (this.tabuListSize_ > 0)) {
		
		this.tabuList_.remove(0);
	    }

	    if (this.tabuListSize_ > 0) {
		this.tabuList_.add(this.movement_);
		this.movement_ = null;
		this.neighbour_ = null;
	    }
	}
	
	else {
	    throw new Exception("No neighbour to add to the tabu list.");
	}
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
    public Solution aspirationCondition(Solution bestSol) {
	Solution res = null;
	boolean stop = false;
	
	Iterator<Pair<Integer,Integer>> it = this.tabuList_.iterator();
	
	while(it.hasNext() && (stop == false)) {
	    Pair<Integer,Integer> p = it.next();
	    Solution s = (Solution) bestSol.clone();
	    
	    s.swap(p.getFirst(), p.getSecond());
	    
	    if(s.cost() < bestSol.cost()) {
		res = (Solution) s.clone();
		stop = true;
	    }
	}
	
	return res;
    }
}
