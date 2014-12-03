import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser.*;
import java.io.*;
import javax.sound.sampled.*;
import java.awt.Desktop;
import java.nio.file.Paths;

public class Driver {
   public static int MAX_INT_SIZE = 2147483647;
   public static float MAX_PERCENT_VALUE = 1.0f;

   public static SpinnerNumberModel getPercentSpinModel(float minVal){
      return new SpinnerNumberModel(minVal, 0.0f, MAX_PERCENT_VALUE, 0.01f);
   }

   public static SpinnerNumberModel getUIntSpinModel(int minVal){
      return new SpinnerNumberModel(minVal, 0, MAX_INT_SIZE, 1);
   }

   private static class LabelAndSpinner {
      private JLabel label;
      private JSpinner spinner;
      private LabelAndSpinner(String labelText, JSpinner spinner) {
         label = new JLabel(labelText);
         this.spinner = spinner;
      }

      private JSpinner getSpinner() {
         return spinner;
      }

      private Object getSpinnerVal() {
         return spinner.getValue();
      }

      private JLabel getLabel() {
         return label;
      }
   }
   public static JFrame mainWindow = new JFrame("448 Lab 6");

   public static ImageIcon hkBackground = new ImageIcon("background.jpg");
   public static ImageIcon hkWinkBackground = new ImageIcon("backgroundWink.jpg");

   public static JLabel background = new JLabel(hkBackground);

   public static JLabel queryLabel = new JLabel("Query: ");
   public static JTextField queryInput = new JTextField("", 12);

   public static LabelAndSpinner exon1Start = 
    new LabelAndSpinner("Exon 1 start position: ", new JSpinner(getUIntSpinModel(1)));
   public static LabelAndSpinner exon1End = 
    new LabelAndSpinner("Exon 1 end position: ", new JSpinner(getUIntSpinModel(1)));

   public static LabelAndSpinner exon2Start = 
    new LabelAndSpinner("Exon 2 start position: ", new JSpinner(getUIntSpinModel(1)));
   public static LabelAndSpinner exon2End = 
    new LabelAndSpinner("Exon 2 end position: ", new JSpinner(getUIntSpinModel(1)));

   public static LabelAndSpinner intronLength = 
    new LabelAndSpinner("Hidden intron length(s): ", new JSpinner(getUIntSpinModel(1)));
   public static LabelAndSpinner exonLength = 
    new LabelAndSpinner("Hidden exon length: ", new JSpinner(getUIntSpinModel(1)));

   public static LabelAndSpinner percentEA =
    new LabelAndSpinner("Percent A in exon: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentET =
    new LabelAndSpinner("Percent T in exon: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentEG =
    new LabelAndSpinner("Percent G in exon: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentEC =
    new LabelAndSpinner("Percent C in exon: ", new JSpinner(getPercentSpinModel(0.0f)));

   public static LabelAndSpinner percentIA =
    new LabelAndSpinner("Percent A in intron: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentIT =
    new LabelAndSpinner("Percent T in intron: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentIG =
    new LabelAndSpinner("Percent G in intron: ", new JSpinner(getPercentSpinModel(0.0f)));
   public static LabelAndSpinner percentIC =
    new LabelAndSpinner("Percent C in intron: ", new JSpinner(getPercentSpinModel(0.0f)));

   public static JLabel uploadFastaLabel = new JLabel("");
   public static JButton uploadFastaButton = new JButton("Upload FASTA");

   public static JButton runButton = new JButton("Run");

   public static void handleFileUpload(){
      JFileChooser fileBrowser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("fasta", "txt");
      fileBrowser.setFileFilter(filter);
      playSound("money");
      mainWindow.getContentPane().add(fileBrowser);

      int returnVal = fileBrowser.showOpenDialog(mainWindow);
      if(returnVal == JFileChooser.APPROVE_OPTION){
         playSound("money");

         uploadFastaLabel.setText(fileBrowser.getSelectedFile().getAbsolutePath());
      } else if(returnVal == JFileChooser.CANCEL_OPTION)
         playSound("explosive");
   }

   private static String getFileName(String oldFileName){
      if(oldFileName.endsWith(".fasta")) {
         return oldFileName.replace(".fasta", "GCContent.txt");
      }
      else if(oldFileName.endsWith(".FASTA")) {
         return oldFileName.replace(".FASTA", "GCContent.txt");
      }
      else if(oldFileName.endsWith(".txt")) {
         return oldFileName.replace(".txt", "GCContent.txt");
      }
      else if(oldFileName.endsWith(".TXT")) {
         return oldFileName.replace(".TXT", "GCContent.txt");
      }
      else {
         return oldFileName.concat("GCContent.txt");
      }  
     
   }

   public static boolean isValidQuery(String query){
      for(int i = 0; i < query.length(); i++) {
         Character c = Character.toUpperCase(query.charAt(i));
         if(!(c == 'G' || c == 'C' || c == 'T' || c == 'A' || c == 'R' || c == 'Y' || c == 'W' || c == 'S' || c == 'M' || c == 'K' || c == 'H' || c == 'B' || c == 'V' || c == 'D' || c == 'N'))
            return false;
      }
      return true;     
   }

   public static ActionListener handleClick = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
         Object source = evt.getSource();

         if(source.equals(uploadFastaButton)){
            handleFileUpload();
         } else if(source.equals(runButton)){
            if(uploadFastaLabel.getText().equals("")){
               playSound("explosive");
               JOptionPane.showMessageDialog(null, "Provide a FASTA file before running");
            } else {
               playSound("money");

               try {

                  if(!isValidQuery(queryInput.getText())){
                     JOptionPane.showMessageDialog(null, "Invalid query: " + queryInput.getText());
                     return;
                  }
                  
                  String outputFile = getFileName(uploadFastaLabel.getText());
                  outputFile = outputFile.substring(0, outputFile.lastIndexOf('.')) + ".csv";

                  Odds exonOdds = new Odds((Double)percentEA.getSpinnerVal(), 
                                           (Double)percentET.getSpinnerVal(), 
                                           (Double)percentEG.getSpinnerVal(), 
                                           (Double)percentEC.getSpinnerVal());
                  Odds intronOdds = new Odds((Double)percentIA.getSpinnerVal(), 
                                             (Double)percentIT.getSpinnerVal(), 
                                             (Double)percentIG.getSpinnerVal(), 
                                             (Double)percentIC.getSpinnerVal());
                  Integer intronLengthValue = (Integer)intronLength.getSpinnerVal(),
                          exonLengthValue = (Integer)exonLength.getSpinnerVal();
                  Integer exon1EndValue = (Integer)exon1End.getSpinnerVal(),
                          exon2StartValue = (Integer)exon2Start.getSpinnerVal();
                  
                  /*TODO: NIGGI use the above values in your method below */
                  //FileDump.dumpHiddenExons(outputFile, ARRAYLIST_OF_HIDDENEXON_HERE);

                  try {
                     Desktop.getDesktop().open(Paths.get(outputFile).toFile());
                  } catch(Exception e){
                     JOptionPane.showMessageDialog(null, "Output file saved to: " + outputFile);
                  }
               } catch (Exception e){
                  JOptionPane.showMessageDialog(null, e.toString());
                  if (!(e instanceof InitException))
                     JOptionPane.showMessageDialog(null, e.getStackTrace());
               }    
            }
         }
      }
   };

   public static JPanel addToAxis(int axis, JComponent... components){
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, axis));

      for(JComponent comp : components)
         panel.add(comp);

      return panel;
   }

   public static JPanel addToAxis(int axis, LabelAndSpinner component){
      return addToAxis(axis, component.getLabel(), component.getSpinner());
   }


   public static JComponent makeTransparent(JComponent comp){
      comp.setOpaque(false);
      return comp;
   }

   public static void setPanelComponentsEnableability(JPanel panel, boolean shouldEnable){
      for(Component comp : panel.getComponents())
         comp.setEnabled(shouldEnable);
   }

   public static void playSound(String directory){
      try{
         File[] allSounds = new File("sounds/" + directory).listFiles();
         File toPlay = allSounds[new Random().nextInt(allSounds.length)];

         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(toPlay.getAbsoluteFile());
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         clip.start();

         wink();
      } catch(Exception e){
         e.printStackTrace();
      }
   }

   public static JComponent getGap(){
      return makeTransparent(new JLabel(" "));
   }

   public static void wink(){

      new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
               background.setIcon(hkBackground);
            }
        }, 
        100 
      );

      background.setIcon(hkWinkBackground);
   }

   public static void main(String[] args){
      JPanel frameContents = new JPanel();
      frameContents.setLayout(new BoxLayout(frameContents, BoxLayout.PAGE_AXIS));
      frameContents.setOpaque(false);

      uploadFastaButton.addActionListener(handleClick);
      runButton.addActionListener(handleClick);
 
      frameContents.add(addToAxis(BoxLayout.X_AXIS, exon1Start));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, exon1End));
      frameContents.add(getGap());
      frameContents.add(addToAxis(BoxLayout.X_AXIS, exon2Start));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, exon2End));
      frameContents.add(getGap());
      frameContents.add(addToAxis(BoxLayout.X_AXIS, exonLength));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, intronLength));
      frameContents.add(getGap());
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentEA));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentET));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentEG));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentEC));
      frameContents.add(getGap());
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentIA));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentIT));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentIG));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, percentIC));
      frameContents.add(getGap());

      frameContents.add(addToAxis(BoxLayout.X_AXIS, uploadFastaLabel, uploadFastaButton));
      frameContents.add(addToAxis(BoxLayout.X_AXIS, runButton));

      mainWindow.setContentPane(background);
	   mainWindow.setLayout(new FlowLayout());
      mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainWindow.setSize(800, 800);
      mainWindow.getContentPane().add(frameContents, BorderLayout.CENTER);
      mainWindow.setVisible(true);
   }
}

