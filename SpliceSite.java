import java.util.*;

public class SpliceSite {
   private int startCoord;
   private ArrayList<StatePath> paths;
   private double pathProbability;
   private double observedProbability;
   private boolean justAdded;
   private boolean justTransitioned;
   
   public SpliceSite (int start, double pProb, double oProb) {
      startCoord = start;
      paths = new ArrayList<StatePath>();
      pathProbability = pProb;
      observedProbability = oProb;
      justAdded = true;
      justTransitioned = false;
   }
   
   public void updatePathProb(double p) { pathProbability *= p; }
   public void updateObservedProb(double p) { observedProbability *= p; }
   public void changeJustAdded() { justAdded = false; }
   public boolean getJustAdded() { return justAdded; }
   public void changeJustTransitioned() { justTransitioned = !justTransitioned; }
   public boolean getJustTransitioned() { return justTransitioned; }
   public void addPath(StatePath path) { paths.add(path); }
   public ArrayList<StatePath> getPaths() { return paths; }
   public double getPProb() { return pathProbability; }
   public int getStart() { return startCoord; }
   
   public double getOProb() { return observedProbability; }
}
