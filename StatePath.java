public class StatePath {
   private int endCoord;
   private double pathProbability;
   private double observedProbability;
   private double probabilityPO;
   private double likelihood;
   private boolean justAdded;
   private boolean justTransitioned;
   
   public StatePath (int end, double pProb, double oProb) {
      endCoord = end;
      pathProbability = pProb;
      observedProbability = oProb;
      likelihood = 0;
      justAdded = true;
      justTransitioned = false;
   }
   
   public double getLikelihood() { return likelihood; }
   public int getEnd() { return endCoord; }
   public void changeJustAdded() { justAdded = false; }
   public boolean getJustAdded() { return justAdded; }
   public void changeJustTransitioned() { justTransitioned = !justTransitioned; }
   public boolean getJustTransitioned() { return justTransitioned; }
   public void setProbPO(double prob) { probabilityPO = prob; }
   public double getProbPO() { return probabilityPO; }
   
   public double calculateProbPO () {
      probabilityPO = pathProbability * observedProbability;
      return probabilityPO;
   }
   
   public double calculateLikelihood(double totalProb) {
      likelihood = probabilityPO / totalProb;
      return likelihood;
   }
   
   public void updatePathProb (double p) {
      pathProbability *= p;
   }
   
   public void updateObservedProb (double p) {
      observedProbability *= p;
   }
}