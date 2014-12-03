public class InitException extends Exception {
   public InitException() { 
      super();
   }
   
   public InitException(String message) { 
      super(message);
   }
   
   public InitException(String message, Throwable cause) { 
      super(message, cause);
   }
   
   public InitException(Throwable cause) { 
      super(cause);
   }
}