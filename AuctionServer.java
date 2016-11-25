import java.rmi.Naming; //Import naming classes to bind to rmiregistry
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class AuctionServer {

  public AuctionServer(String args[]) throws RemoteException{
    //N.b. it is possible to host multiple objects on a server
    //by repeating the following method.
    try {
      AuctionHouse a = new AuctionHouseImpl();
      if (args[0].equals("-")){
	Registry registry = LocateRegistry.createRegistry(1099);
	    registry.bind("AuctionHouse", a);
      } else {
      Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
	  registry.bind("AuctionHouse", a);
      } 
      System.out.println("Server now running");
    } catch (Exception e) {
      System.out.println("Server Error: " + e);
    }
  }
  // end of calculatorserver constructor

  public static void main(String args[])throws RemoteException {
    new AuctionServer(args);
  }

}
