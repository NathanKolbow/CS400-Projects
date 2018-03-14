import java.io.IOException;

public class AnalysisTest {

	public static void main(String[] args) {

		// TODO Add code for checking command line arguments
		int n = 1;
		while (true) {
			
			PerformanceAnalysisHash ana;
			try {
				ana = new PerformanceAnalysisHash("IntegerLarge.txt");
				ana.compareDataStructures();
				ana.loadData("IntegerSmall.txt");
				ana.compareDataStructures();
				ana.loadData("StringLarge.txt");
				ana.compareDataStructures();
				ana.loadData("StringSmall.txt");
				ana.compareDataStructures();
				System.out.println("Report " + n + ":");
				ana.printReport();
			} catch (IOException e) {
				e.printStackTrace();
			}
			n++;
		}
	}

}