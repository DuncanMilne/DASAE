import java.rmi.Naming; //Import naming classes to bind to rmiregistry

public class AuctionServer {

  public AuctionServer() {
    //N.b. it is possible to host multiple objects on a server
    //by repeating the following method.
    try {
      AuctionHouse a = new AuctionHouseImpl();
      System.out.println("Test");
      Naming.rebind("rmi://localhost/AuctionHouse", a);
      System.out.println("Test1");
    } catch (Exception e) {
      System.out.println("Server Error: " + e);
    }
  }
  // end of calculatorserver constructor

  public static void main(String args[]) {
    new AuctionServer();
  }

}
