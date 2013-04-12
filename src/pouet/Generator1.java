package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Generateur de solution initiale a un probleme des n reines.
 * 
 * <p>Les solutions sont generees totalement aleatoirement.</p>
 */
public class Generator1 implements SolutionGenerator {
    
    private Preprocessor preprocessor_ = null;
    
    public Generator1() {}
    
    public Generator1(Preprocessor preproc) {
	this.preprocessor_ = preproc;
    }
    
    @Override
    public Solution generate(Integer size, boolean verbose) {
	Solution initSolution = null;
	List<Integer> values = new ArrayList<Integer>();
	Random rand = new Random();
	
	if(verbose == true) {
	    System.out.print("Generation of an initial solution ... ");
	}
	
	for(int i = 0 ; i < size ; ++i) {
	    values.add(rand.nextInt(size));
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
