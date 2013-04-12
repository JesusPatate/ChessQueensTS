package object;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class Preprocessor1 implements Preprocessor {

    @Override
    public Solution preprocess(Solution sol, boolean verbose){
	List<Integer> good = new ArrayList<Integer>(sol.size());
	List<Pair<Integer,Integer>> wrong = new ArrayList<Pair<Integer,Integer>>();
	List<Integer> costs = new ArrayList<Integer>();

	if(verbose == true) {
	    System.out.print("Preprocessing ... ");
	}
	
	good.add(sol.get(0));
	
	for(int i = 1 ; i < sol.size() ; ++i) {
	    good.add(sol.get(i));

	    Solution s = new Solution(good);

	    if(s.cost() != 0) {
		good.remove(good.size()-1);
		wrong.add(new Pair<>(i, sol.get(i)));
	    }
	}

	boolean addition;
	
	while(wrong.isEmpty() == false) {
	    addition = false;
	    costs.clear();

	    int i = 0;

	    while(i < wrong.size()) {
		good.add(wrong.get(i).getSecond());

		Solution s = new Solution(good);

		if(s.cost() == 0) {
		    wrong.remove(i);
		    addition = true;
		}

		else {
		    costs.add(s.cost());
		    good.remove(good.size()-1);
		    ++i;
		}
	    }

	    if(addition == false) {
		int minCost = Integer.MAX_VALUE;
		int idxMinCost = -1;

		for(i = 0 ; i < costs.size() ; ++i) {
		    if(costs.get(i) < minCost) {
			minCost = costs.get(i);
			idxMinCost = i;
		    }
		}

		good.add(wrong.get(idxMinCost).getSecond());
		wrong.remove(idxMinCost);
	    }
	}

	if(verbose == true) {
	    System.out.println("done");
	}

	return new Solution(good);
    }
}
