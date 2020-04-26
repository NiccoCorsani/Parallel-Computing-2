package edit_distance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class App {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Charts");

				frame.setSize(600, 400);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);

				XYDataset ds = null;
				try {
					ds = createDataset(null,"Parallelo");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JFreeChart chart = ChartFactory.createXYLineChart(" 16 Threads", "Dimension byte", "Time milli_seconds", ds, PlotOrientation.VERTICAL,
						true, true, false);

				ChartPanel cp = new ChartPanel(chart);

				frame.getContentPane().add(cp);
			}
		});

	}

	
	////era private 
	public static XYDataset createDataset(double[][] DimensioneFile_TempoDiProcessamento, String tipoProcessamento) throws IOException {

		DefaultXYDataset ds = new DefaultXYDataset();
		
		ds.addSeries("Sequential",App.createDataSetFromFile("/Users/nicche/eclipse-workspace/Levenshtein/Sequential Dim Velocità"));
		ds.addSeries("FixedThreadPoolColumn",App.createDataSetFromFile("/Users/nicche/eclipse-workspace/Levenshtein/Colonna Dim Velocità fixedThreadPool"));
		ds.addSeries("ScheduledThreadPoolColumn",App.createDataSetFromFile("/Users/nicche/eclipse-workspace/Levenshtein/Colonna Dim Velocità scheduledThreadPool"));
		ds.addSeries("FixedThreadPoolDiagonal",App.createDataSetFromFile("/Users/nicche/eclipse-workspace/Levenshtein/Diagonale Dim Velocità"));

	
		return ds;
	}

	
	
	public static double[][] createDataSetFromFile(String file_name) throws IOException{
		

        
        int lines=0;
        BufferedReader br = new BufferedReader(new FileReader(file_name));
        while(br.readLine()!= null) lines++;
        double[][] data = new double[2][lines];

        String ln;
        double time; 
        double dimension;
        int i=0;        
        BufferedReader br1 = new BufferedReader(new FileReader(file_name));

        while((ln=br1.readLine())!= null) {       	
        String ss[]=ln.split(" ");
        time=Double.valueOf(ss[0]);
        dimension=Double.valueOf(ss[1]);
        data[0][i]=dimension;
        data[1][i]=time;
        i++;
        }


		return data;
		
		
		
		
	}
	
	
	
	
}