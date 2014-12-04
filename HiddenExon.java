//NIGGI please put things here =)

public class HiddenExon {
	private static final int NUM_ROWS = 1;
	private static final int NUM_COLS = 2;

	int start, end;
	double likelihood;
   
   public HiddenExon(int s, int e, double l) {
      start = s;
      end = e;
      likelihood = l;
   }

	public String[][] toCsv() {
		String[][] temp = new String[NUM_ROWS][NUM_COLS];
		temp[0][0] = start + "-" + end;
		temp[0][1] = String.format("%.2f", likelihood * 100);

		return temp;
	}
}