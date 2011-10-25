import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;


public class waveParse {

	public static final double NOISE_THRESH = 0.08;
	public static final double OFFSET = 1250;
	
	public static void main(String[] args) {
			
		// if there isn't a file, wig out.
		
		if(args.length == 0) {
			System.out.println("You must specify a file.");
			System.exit(0);
		}
		
		BufferedReader q = null;
		int delay;
		List<List<Double>> waveSet = null;
		
		// try doing stuff
		
		try {
			q = new BufferedReader(new FileReader(args[0]));
			waveSet = numberPrep(q);
			q.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println("Rerun with a valid file/path.");
		}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		delay = (int) waveCrunch(waveSet);
		System.out.println("Results:");
		System.out.println("The delay is " + delay);
	}
	
	public static List<List<Double>> numberPrep(BufferedReader q) throws NumberFormatException, IOException {
		
		List<List<Double>> o = new LinkedList<List<Double>>();
		
		if(q == null)
			System.out.println("No tokens were found. Check your file.");
		else {
				while(q.ready()) {
					String currentWave = q.readLine();
					
					StringTokenizer m = new StringTokenizer(currentWave, "\t");
					LinkedList<Double> n = new LinkedList<Double>();
					
					while(m.hasMoreTokens())
						n.add(Math.abs(Double.valueOf(m.nextToken())));
					o.add(n);
				}
		}
		return o;
	}

	public static double waveCrunch(List<List<Double>> s) {
		
		double[] waveMax = new double[s.size()];
		ListIterator<List<Double>> i = s.listIterator();
		boolean found;
		int pos = 0;
		
		while(i.hasNext()) {
			found = false;
			List<Double> m = i.next();
			ListIterator<Double> j = m.listIterator();
			while(j.hasNext() && !found) {
				if((Double.compare(j.next(), NOISE_THRESH) > 0)) {
					waveMax[pos] = j.nextIndex() - 1;
					found = true;
				}
			}
			pos++;
		}
		return doMath(waveMax);
	}
	
	public static double doMath(double[] n) {
		double sum = 0;
		
		for(int i = 0; i < n.length; i++)
			sum += n[i];
		
		return((sum / n.length) - OFFSET); 
	}
	
}
