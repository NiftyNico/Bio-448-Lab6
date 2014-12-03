import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class CsvOutputter {

   private PrintWriter outWriter;

   public static void main(String[] args) throws FileNotFoundException{
      String[][] obj = new String[100][10];

      obj[0][6] = "first row, column 7";
      obj[99][1] = "shit";
      obj[3][2] = "fk";
      obj[76][3] = "wo\"rk";
      obj[23][4] = "pl,z";
      obj[90][5] = "o\nmg";
      obj[55][9] = "yes";
      obj[24][8] = "thx";

      new CsvOutputter("out.csv").write(obj).close();
   }

   public CsvOutputter(String fileNameAndPath) throws FileNotFoundException {
      outWriter = new PrintWriter(fileNameAndPath);
   }

   public CsvOutputter write(String[][] obj){
      for(String[] row : obj)
         for(int i = 0; i < row.length; i++)
            outWriter.printf("%s%s", row[i] == null ? "" : "\"" + row[i] + "\"", i == row.length - 1 ? "\r\n" : ",");
      return this;
   }

   public void close(){
      outWriter.flush();
      outWriter.close();
   }
}
