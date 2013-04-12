package object;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class Preprocessor2 implements Preprocessor {

    @Override
    public Solution preprocess(Solution sol, boolean verbose) {
	List<Integer> good = new ArrayList<Integer>(sol.size());
	List<Integer> wrong = new ArrayList<Integer>();

	if(verbose == true) {
	    System.out.print("Preprocessing ... ");
	}
	
	// Etape 1 : les reines dispatchee dans good et wrong
	good.add(sol.get(0));

	// On tente ensuite d'ajouter la reine suivante de la solution initiale
	// dans la liste Good. Si elle genere un conflit, elle est retiree
	// et placee dans la liste Wrong
	for(int i = 1 ; i < sol.size() ; ++i) {
	    good.add(sol.get(i));

	    Solution s = new Solution(good);

	    if(s.cost() != 0) {
		good.remove(good.size()-1);
		wrong.add(sol.get(i));
	    }
	}

	// Liste contenant pour chaque reine de wrong la liste
	// positions possibles avec leur cout
	List<List<Pair<Integer,Integer>>> pouet =
		new ArrayList<List<Pair<Integer,Integer>>>();
	
	// Liste des positions d'une reine de wrong avec leur cout
	List<Pair<Integer,Integer>> meuh;
	
	boolean addition;
	int idxWrong;

	while(wrong.isEmpty() == false) {
	    idxWrong = 0;
	    addition = false;
	    pouet.clear();

	    while(idxWrong < wrong.size() && (addition == false)) {
		meuh = new ArrayList<Pair<Integer,Integer>>();
		
		for(int idxGood = 0 ; (idxGood <= good.size())
			&& (addition == false) ; ++idxGood) {

		    good.add(idxGood, wrong.get(idxWrong));
		    Solution s = new Solution(good);

		    if(s.cost() == 0) {
			addition = true;
		    }
		    else {
			meuh.add(new Pair<Integer,Integer>(idxGood, s.cost()));
			good.remove(idxGood);
		    }
		}

		if(addition == false) {
		    pouet.add(meuh);
		    ++idxWrong;
		}
		
		else {
		    wrong.remove(idxWrong);
		}
	    }
	    
	    if(addition == false) {
		int minCost = Integer.MAX_VALUE;
		Pair<Integer, Integer > posMin = null;
		int cost = 0;

		for (int idxPouet = 0 ; idxPouet < pouet.size() ; ++idxPouet) {
		    List<Pair<Integer, Integer>> l = pouet.get(idxPouet);

		    for (int idxMeuh = 0 ; idxMeuh < l.size() ; ++idxMeuh) {
			cost = l.get(idxMeuh).getSecond();

			if (cost < minCost) {
			    minCost = cost;
			    posMin = new Pair<Integer, Integer>(idxPouet,
				    idxMeuh);
			}
		    }
		}
		
		if(posMin != null) {
		    good.add(
			    pouet.get(posMin.getFirst()).get(posMin.getSecond()).getFirst(),
			    wrong.get(posMin.getFirst()));
		    
		    int index = posMin.getFirst();
		    
		    wrong.remove(index);
		}
	    }
	}

	if(verbose == true) {
	    System.out.println("done");
	}

	return new Solution(good);
    }

}
