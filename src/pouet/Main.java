package pouet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;

public class Main {
    
    private static String PLOTS_DIR = "plots/";
    
    /**
     * Enregistre des graphiques a partir des donnees de la recherche tabou
     * (temps d'execution, nombre d'iterations, nombre de runs)
     * 
     * @param data Les donnees de la recherche tabou
     * @param nQueens Le nombre de reines du probleme traite
     * @param tabuSize La taille de la memoire tabou
     * 
     * @throws IOException
     */
    private static void drawPlots(final Data data, final int nQueens,
	    final int tabuSize) throws IOException {

	ArrayDataSet data_exec = new ArrayDataSet(data.getTimes());
	ArrayDataSet data_runs = new ArrayDataSet(data.getRuns());
	ArrayDataSet data_iter = new ArrayDataSet(data.getIterations());

	final JavaPlot p_exec = new JavaPlot();
	final JavaPlot p_runs = new JavaPlot();
	final JavaPlot p_iter = new JavaPlot();
	
	PlotStyle style = new PlotStyle();
	// style.setStyle(Style.LINES);

	DataSetPlot dsp_exec = new DataSetPlot(data_exec);
	dsp_exec.setPlotStyle(style);
	DataSetPlot dsp_runs = new DataSetPlot(data_runs);
	dsp_runs.setPlotStyle(style);
	DataSetPlot dsp_iter = new DataSetPlot(data_iter);
	dsp_iter.setPlotStyle(style);
	
	p_exec.addPlot(dsp_exec);
	p_exec.setKey(JavaPlot.Key.OFF);
	p_exec.set("xlabel", "'Execution time (milliseconds)'");
	p_exec.set("ylabel", "'Executions (cumulative)'");
	p_exec.setTitle(nQueens + " queens, " + tabuSize + " tabu neighbours");

	p_runs.addPlot(dsp_runs);
	p_runs.setKey(JavaPlot.Key.OFF);
	p_runs.set("xlabel", "'Runs'");
	p_runs.set("ylabel", "'Executions (cumulative)'");
	p_runs.setTitle(nQueens + " queens, " + tabuSize + " tabu neighbours");

	p_iter.addPlot(dsp_iter);
	p_iter.setKey(JavaPlot.Key.OFF);
	p_iter.set("xlabel", "'Iterations'");
	p_iter.set("ylabel", "'Executions (cumulative)'");
	p_iter.setTitle(nQueens + " queens, " + tabuSize + " tabu neighbours");
	
	String fname_exec = "exec_q" + nQueens + "_t" + tabuSize;
	String fname_runs = "runs_q" + nQueens + "_t" + tabuSize;
	String fname_iter = "iter_q" + nQueens + "_t" + tabuSize;
	
	File file_exec = new File(PLOTS_DIR + fname_exec);
	File file_runs = new File(PLOTS_DIR + fname_runs);
	File file_iter = new File(PLOTS_DIR + fname_iter);
	
	ImageTerminal png_exec = new ImageTerminal();
	ImageTerminal png_runs = new ImageTerminal();
	ImageTerminal png_iter = new ImageTerminal();
	
	try {
	    file_exec.createNewFile();
	    file_runs.createNewFile();
	    file_iter.createNewFile();
	    
	    png_exec.processOutput(new FileInputStream(file_exec));
	    png_runs.processOutput(new FileInputStream(file_runs));
	    png_iter.processOutput(new FileInputStream(file_iter));
	}
	catch (FileNotFoundException ex) {
	    System.err.print(ex);
	}
	catch (IOException ex) {
	    System.err.print(ex);
	}
	
	p_exec.setTerminal(png_exec);
	p_runs.setTerminal(png_runs);
	p_iter.setTerminal(png_iter);
	
	p_exec.plot();
	p_runs.plot();
	p_iter.plot();
	
	try {
	    ImageIO.write(png_exec.getImage(), "png", file_exec);
	    ImageIO.write(png_runs.getImage(), "png", file_runs);
	    ImageIO.write(png_iter.getImage(), "png", file_iter);
	}
	catch (IOException ex) {
	    System.err.print(ex);
	}
    }

    public static void main(String[] args) {
	Arguments arguments = new Arguments();
	CmdLineParser parser = new CmdLineParser(arguments);
	Data data = null;

	try {
	    parser.parseArgument(args);

	    if (arguments.getPlots() == true) {
		data = new Data();
	    }

	    Preprocessor preproc = new Preprocessor1();
	    SolutionGenerator generator = new Generator2(preproc);

	    Neighbourhood neighbourhood = new Neighbourhood2(
		    arguments.getTabuListSize());

	    ChessQueensTS tabuSearch = new ChessQueensTS(
		    arguments.getNumberOfQueens(), arguments.getTabuListSize(),
		    arguments.getProbaAspi());

	    for (int i = 0; i < arguments.getNumberOfExecutions(); ++i) {
		if(arguments.getVerbose() == true) {
		    System.out.println("Execution " + (i+1));
		}
		
		tabuSearch.search(neighbourhood, generator,
		        arguments.getNumberOfRuns(), data,
		        arguments.getVerbose());
	    }

	    if (arguments.getPlots() == true) {
		drawPlots(data, arguments.getNumberOfQueens(),
		        arguments.getTabuListSize());
	    }
	}

	catch (CmdLineException e) {
	    System.err.println("Error : missing or wrong argument(s).");
	    System.err.println("Arguments : ");
	    parser.setUsageWidth(80);
	    parser.printUsage(System.err);
	}

	catch (IOException e) {
	    System.err.println("Error : the writing of statistics "
		    + "in output file failed.");
	}
    }

}
