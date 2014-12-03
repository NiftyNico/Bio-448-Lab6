public class Odds {
   private double a, t, g, c;

   public double getA() {
      return a;
   }

   public double getT() {
      return t;
   }

   public double getG() {
      return g;
   }

   public double getC() {
      return c;
   }

   private void throwLessThanInitException(String var) throws InitException {
      throw new InitException(var + " cannot be less than 0\n");
   }

   public Odds(double a, double t, double g, double c) throws InitException {
      double sum = a + t + g + c;
      if(sum <= 0.999999f && sum >= 1.000001f)
         throw new InitException("A, T, G, and C must add to 1.0, they currently add to " + sum);
      if(a < 0.0f)
         throwLessThanInitException("A");
      if(t < 0.0f)
         throwLessThanInitException("T");
      if(g < 0.0f)
         throwLessThanInitException("G");
      if(c < 0.0f)
         throwLessThanInitException("C");

      this.a = a;
      this.t = t;
      this.g = g;
      this.c = c;
   }
}