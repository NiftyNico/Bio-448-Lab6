import java.io.*;
import java.util.*;

public class FileDump {
	public static String readFASTA(String fileName) {
      String seq = "";
      
      try {
         BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
         
         String text = null;
         
         /* Dump the first line baby!*/
         reader.readLine();
         
         while ((text = reader.readLine()) != null) {
            text = text.toLowerCase();
            seq = seq.concat(text);
         }
      }
      catch (IOException e) {
         e.printStackTrace();
      }

      return seq;
   }

   private static int HEADER_ROWS = 1;
   private static int HEADER_COLS = 2;

   public static void dumpHiddenExons(String nameAndPath, ArrayList<HiddenExon> hiddenExons) throws FileNotFoundException {
   	CsvOutputter outputter = new CsvOutputter(nameAndPath);

   	String[][] header = new String[HEADER_ROWS][HEADER_COLS];

   	header[0][0] = "Start / End:";
   	header[0][1] = "Likelyhood";

   	outputter.write(header);

   	for (HiddenExon e : hiddenExons)
   		outputter.write(e.toCsv());
   	outputter.close(); 
   }
}