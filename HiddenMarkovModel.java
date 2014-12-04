import java.util.*;
import java.io.*;

public class HiddenMarkovModel {
   private int intronLen;
   private int exonLen;
   private double EE;
   private double EFive;
   private double FiveI;
   private double II;
   private double IThree;
   private double ThreeE;
   private double EA;
   private double ET;
   private double EG;
   private double EC;
   private double IA;
   private double IT;
   private double IG;
   private double IC;
   private double FiveG;
   private double ThreeA;
   private String seq;
   private int exon1End;
   private int exon2Start;
   
   public HiddenMarkovModel (int iLen, int eLen, double ea, double et, double eg, double ec, double ia, double it, double ig, double ic, int e1End, int e2Start, String fileName) {
      intronLen = iLen;
      exonLen = eLen;
      EE = 1 - (1 / exonLen);
      EFive = 1 / exonLen;
      FiveI = 1.0;
      II = 1 - (1 / intronLen);
      IThree = 1 / intronLen;
      ThreeE = 1.0;
      EA = ea;
      ET = et;
      EG = eg;
      EC = ec;
      IA = ia;
      IT = it;
      IG = ig;
      IC = ic;
      FiveG = 1.0;
      ThreeA = 1.0;
      exon1End = e1End;
      exon2Start = e2Start;
      seq = this.readFASTA(fileName).substring(exon1End, exon2Start-1);
   }
   
   private String readFASTA(String fileName) {
      BufferedReader reader = null;
      File in = new File(fileName);
      String seq = "";
      
      try {
         reader = new BufferedReader(new FileReader(in));
         
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
   
   public ArrayList<HiddenExon> findHiddenExons () {
      ArrayList<SpliceSite> sites = this.findSpliceSites();
      double totalProb;
      
      for (SpliceSite ss : sites) {
         System.out.println("SpliceSite...\nstart: "+ss.getStart());
         for (StatePath sp : ss.getPaths())
            System.out.println("end: "+sp.getEnd());
         System.out.println();
      }
      
      totalProb = this.calculateProbPO(sites);
      return calculateLikelihood(sites, totalProb);
   }
   
   private ArrayList<SpliceSite> findSpliceSites () {
      ArrayList<SpliceSite> sites = new ArrayList<SpliceSite>();
      char c1 = 0, c2 = 0;
      double pProb = 1.0, oProb = 1.0;
      
      for (int i = 0; i < seq.length()-1; i++) {
         c1 = seq.charAt(i);
         c2 = seq.charAt(i+1);
         
         if (c1 == 'a' && c2 == 'g') {
            SpliceSite newSS = new SpliceSite(i+exon1End+1, pProb, oProb);
            sites.add(newSS);
         }
         else if (c1 == 'g' && c2 == 't') {
            for (SpliceSite ss : sites) {
               StatePath path = new StatePath(i+exon1End+1, ss.getPProb(), ss.getOProb());
               ss.addPath(path);
            }
         }
         
         for (SpliceSite ss : sites) {
            if (!ss.getJustAdded() && !ss.getJustTransitioned()) {
               ss.updatePathProb(EE);
               ss.updateObservedProb(this.getOProbExon(c1));
            }
            else if (ss.getJustAdded()) {
               ss.changeJustAdded();
               ss.changeJustTransitioned();
               ss.updatePathProb(ThreeE);
            }
            else {
               ss.changeJustTransitioned();
               ss.updateObservedProb(this.getOProbExon(c1));
            }
               
            
            for (StatePath sp : ss.getPaths()) {
               if (!sp.getJustAdded() && !sp.getJustTransitioned()) {
                  sp.updatePathProb(II);
                  sp.updateObservedProb(this.getOProbIntron(c1));
               }
               else if (sp.getJustAdded()) {
                  sp.changeJustAdded();
                  sp.changeJustTransitioned();
                  sp.updatePathProb(FiveI);
               }
               else {
                  sp.changeJustTransitioned();
                  sp.updateObservedProb(this.getOProbIntron(c1));
               }
            }
         }
         
         pProb *= II;
         oProb *= getOProbIntron(c1);
      }
      
      for (SpliceSite ss : sites) {
         for (StatePath sp : ss.getPaths()) {
            if (!sp.getJustAdded()) {
               sp.updatePathProb(II);
               sp.updateObservedProb(this.getOProbIntron(c1));
            }
            else if (sp.getJustAdded() && !sp.getJustTransitioned()) {
               sp.changeJustAdded();
               sp.changeJustTransitioned();
               sp.updatePathProb(FiveI);
            }
            else {
               sp.changeJustTransitioned();
               sp.updateObservedProb(this.getOProbIntron(c1));
            }
         }
      }
      
      return sites;
   }
   
   private double getOProbExon (char c) {
      if (c == 'a')
         return EA;
      if (c == 't')
         return ET;
      if (c == 'g')
         return EG;
      if (c == 'c')
         return EC;
      
      return 1;
   }
   
   private double getOProbIntron (char c) {
      if (c == 'a')
         return IA;
      if (c == 't')
         return IT;
      if (c == 'g')
         return IG;
      if (c == 'c')
         return IC;
      
      return 1;
   }
   
   private double calculateProbPO(ArrayList<SpliceSite> sites) {
      double totalProb = 0;
      
      for (SpliceSite ss : sites)
         for (StatePath sp : ss.getPaths())
            totalProb += sp.calculateProbPO();
      
      return totalProb;
   }
   
   private ArrayList<HiddenExon> calculateLikelihood(ArrayList<SpliceSite> sites, double totalProb) {
      ArrayList<HiddenExon> exons = new ArrayList<HiddenExon>();
      
      for (SpliceSite ss : sites) {
         for (StatePath sp : ss.getPaths()) {
            HiddenExon e = new HiddenExon(ss.getStart(), sp.getEnd(), sp.calculateLikelihood(totalProb));
         }
      }
      
      return exons;
   }
   
   
   
}
