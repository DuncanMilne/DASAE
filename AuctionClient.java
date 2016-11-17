import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class AuctionClient extends UnicastRemoteObject {

  public AuctionClient() throws RemoteException {
    super();
  }

  public static void main(String[] args) {
    try {
      // Create the reference to the remote object through the rmiregistry
      AuctionHouse a = (AuctionHouse) /*(AuctionHouse) casts it to an AuctionHouse */
      Naming.lookup("rmi://localhost/AuctionHouse");
      // Now use the reference a to call remote methods
      /* #TODO custom ones for this
      System.out.println("3+21="+ c.add(3, 21) );
      System.out.println("18-9="+ c.sub(18, 9) );
      System.out.println("4*17="+ c.mul(4, 17) );
      System.out.println("70/10="+ c.div(70, 10) );
      System.out.println("2^5="+ c.pow(2, 5) );
      */
      AuctionClient auctionClient = new AuctionClient();
      auctionClient.pingServer(a);
    }

  // Catch the exceptions that may occur – bad URL, Remote exception
  // Not bound exception or the arithmetic exception that may occur in
  // one of the methods creates an arithmetic error (e.g. divide by zero)
    catch (MalformedURLException murle) {
      System.out.println("MalformedURLException");
      System.out.println(murle);
    }
    catch (RemoteException re) {
      System.out.println("RemoteException");
      System.out.println(re);
    }
    catch (NotBoundException nbe) {
      System.out.println("NotBoundException");
      System.out.println(nbe);
    }
    catch (java.lang.ArithmeticException ae) {
      System.out.println("java.lang.ArithmeticException");
      System.out.println(ae);
    }
  }

  public void callBack(String s) throws RemoteException {
      System.out.println(s);
   }

   public void pingServer(AuctionHouse a) throws RemoteException{
     a.registerObject(this, "test", 5);
   }
}
