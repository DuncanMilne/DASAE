import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class AuctionClient extends UnicastRemoteObject implements AuctionClientIntf, Runnable {

  public AuctionClient() throws RemoteException {
    super();
  }

  public static void main(String[] args) {
    try {
      // Create the reference to the remote object through the rmiregistry
      AuctionHouse a = (AuctionHouse) /*(AuctionHouse) casts it to an AuctionHouse */
      Naming.lookup("rmi://localhost/AuctionHouse");

      AuctionClient auctionClient = new AuctionClient();

      auctionClient.startHeartbeat(a);

      // Now use the reference a to call remote methods

      Scanner standardInput = new Scanner(System.in);

			System.out.print("1: Create Auction 2: Show Available Items ");

      String line = standardInput.nextLine();
      switch(Integer.parseInt(line)) {
        case 1:
          System.out.println("Please enter name of item: ");
          String name = standardInput.nextLine();
          System.out.println("Please enter starting price of item: ");
          double minPrice = Integer.parseInt(standardInput.nextLine());
          a.createAuctionItem(name, minPrice, new Date());
        case 2:
          System.out.println(a.showAvailableAuctionItems());
      }
      //for show available auctions do callback that returns the
      // auctionClient.callBack(a); disable temporarily
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

  public void callBack(AuctionHouse a) throws RemoteException {
    a.registerObject(this,"test", 5000);
   }
   public void callBackString(String n) throws RemoteException {
     System.out.println(n);
  }

  public void startHeartbeat(AuctionHouse a) throws RemoteException {
    while (!Thread.currentThread().isInterrupted()) {
    try {
      Thread.sleep(10000);
    } catch(InterruptedException e) {
      System.out.println("InterruptedException");
    }
    System.out.println("pinging server");
      if (a.heartbeatMonitor()) {
        System.out.println("server alive");
      } else {
        System.out.println("server dead");
      }
    }
  }
}
