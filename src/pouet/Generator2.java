package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generateur ameliore de solution initiale a un probleme des n reines.
 * 
 * <p>Aucune reine des solutions generees ne peut partager sa colonne
 * avec une autre.</p>
 * 
 */
public class Generator2 implements SolutionGenerator {
    
    private Preprocessor preprocessor_ = null;
    
    public Generator2() {}
    
    public Generator2(Preprocessor preproc) {
	this.preprocessor_ = preproc;
    }

    @Override
    public Solution generate(Integer size, boolean verbose) {
	Solution initSolution = null;
	List<Integer> values = new ArrayList<Integer>();
	
	if(verbose == true) {
	    System.out.print("Generation of an initial solution ... ");
	}
	
	for(Integer i = 0 ; i < size ; ++i) {
	    values.add(i);
	}
	
	Random rand = new Random();
	Integer aux = 0, index1 = 0, index2 = 0;
	
	for(Integer i = 0 ; i < 5*size ; ++i) {
	    index1 = rand.nextInt(size);
	    index2 = rand.nextInt(size);
	    
	    aux = values.get(index1);
	    values.set(index1, values.get(index2));
	    values.set(index2, aux);
	}
	
	if(verbose == true) {
	    System.out.println("done");
	}
	
	if(this.preprocessor_ != null) {
	    initSolution = this.preprocessor_.preprocess(
		    new Solution(values), verbose);
	}
	else {
	    initSolution = new Solution(values);
	}
	
	return initSolution;
    }
}
