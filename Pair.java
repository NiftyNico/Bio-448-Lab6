public class Pair<T, G> {
   private T left;
   private G right;

   public T getLeft(){ return left; }
   public G getRight(){ return right; }

   public Pair(T left, G right){
      this.left = left;
      this.right = right;
   }
}
