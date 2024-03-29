package object;

import java.util.List;
import java.util.Random;

import util.Pair;

/**
 * A solution (not necessarily feasible) to a n-queens problem.
 */
public class Solution implements Cloneable {

    /**
     * The list of the values of the variables.
     */
    private int variables_[] = null;

    /**
     * The solution cost.
     */
    private Integer cost_ = null;

//    /**
//     * Construi
//     * 
//     * @param size
//     *            The size of the solution
//     */
//    public Solution(int size) {
//	this.variables_ = new int[size];
//
//	for (int i = 0; i < size; ++i) {
//	    this.variables_[i] = 0;
//	}
//    }

    /**
     * Construit une nouvelle solution a partir d'une liste de valeurs
     * 
     * @param values
     *            Une liste contenant les positions des reines de la nouvelle
     *            solution
     */
    public Solution(List<Integer> values) {
	this.variables_ = new int[values.size()];

	for (int i = 0; i < values.size(); ++i) {
	    variables_[i] = values.get(i);
	}
    }

    /**
     * Construit une nouvelle solution a partir d'un tableau de valeurs
     * 
     * @param values
     *            Un tableau contenant les positions des reines de la nouvelle
     *            solution
     */
    public Solution(int[] values) {
	this.variables_ = new int[values.length];

	for (int i = 0; i < values.length; ++i) {
	    variables_[i] = values[i];
	}
    }

    /**
     * Construit une nouvelle solution a partir d'une autre deja existante.
     * 
     * @param sol
     *            Une solution existante
     * @param movement
     *            Un mouvement a appliquer a la solution existante (deplacement
     *            d'une reine dans sa ligne)
     */
    public Solution(final Solution sol, final Pair<Integer, Integer> movement) {
	this.variables_ = new int[sol.size()];

	System.arraycopy(sol.variables_, 0, this.variables_, 0, sol.size());
	this.variables_[movement.getFirst()] = movement.getSecond();
    }

    /**
     * Retourne l'emplacement d'une reine de la solution.
     * 
     * @param index
     *            Index d'une reine de la solution
     * 
     * @return La colonne dans laquelle se trouve la reine
     */
    public int get(final int index) {
	return this.variables_[index];
    }

    public int size() {
	return this.variables_.length;
    }

    public int cost() {
	return (this.cost_ != null) ? this.cost_ : this.fitness();
    }

    public void set(final int index, final int value) {
	this.variables_[index] = value;

	this.cost_ = null;
    }

    // public void degrade() {
    // Random rand = new Random();
    //
    // int index1 = rand.nextInt(this.size());
    // int index2 = rand.nextInt(this.size());
    //
    // int aux = this.variables_[index2];
    // this.variables_[index2] = this.variables_[index1];
    // this.variables_[index1] = aux;
    //
    // this.cost_ = null;
    // }

    public void swap(int index1, int index2) {
	int aux = 0;

	aux = this.variables_[index1];
	this.variables_[index1] = this.variables_[index2];
	this.variables_[index2] = aux;
    }

    private int fitness() {
	int result = 0;

	// allDifferent on Q
	result += this.fitnessAllDiff(this);

	// allDifferent on y

	int[] aux = new int[this.size()];

	for (int i = 0; i < this.size(); ++i) {
	    aux[i] = this.variables_[i] + i;
	}

	result += this.fitnessAllDiff(new Solution(aux));

	// allDifferent on z

	for (int i = 0; i < this.size(); ++i) {
	    aux[i] = this.variables_[i] - i;
	}

	result += this.fitnessAllDiff(new Solution(aux));

	return result;
    }

    private int fitnessAllDiff(Solution sol) {
	int result = 0;

	for (int i = 0; i < sol.size(); ++i) {
	    for (int j = i + 1; j < sol.size(); ++j) {
		if (sol.get(i) == sol.get(j)) {
		    ++result;
		}
	    }
	}

	return result;
    }

    @Override
    public Object clone() {
	return new Solution(this.variables_);
    }

    public void print() {
	System.out.print("{");

	if (this.size() > 0) {
	    System.out.print(this.get(0));

	    for (int i = 1; i < this.size(); ++i) {
		System.out.print(", " + this.get(i));
	    }
	}

	System.out.println("}");
    }

    public boolean equals(Solution other) {
	boolean eq = true;

	if (this.size() == other.size()) {
	    for (int i = 0; i < this.size(); ++i) {
		if (this.get(i) != other.get(i)) {
		    eq = false;
		}
	    }
	}

	else {
	    eq = false;
	}

	return eq;
    }
}
