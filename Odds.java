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

   private void assertZeroOrGreater(String var, double val) throws InitException {
      if(val < -0.00000000001f)
         throw new InitException(var + " cannot be less than 0\n");
   }

   public Odds(double a, double t, double g, double c) throws InitException {
      double sum = a + t + g + c;
      if(sum <= 0.999999f || sum >= 1.000001f)
         throw new InitException("A, T, G, and C must add to 1.0, they currently add to " + sum);
      assertZeroOrGreater("A", a);
      assertZeroOrGreater("T", t);
      assertZeroOrGreater("G", g);
      assertZeroOrGreater("C", c);

      this.a = a;
      this.t = t;
      this.g = g;
      this.c = c;
   }
}