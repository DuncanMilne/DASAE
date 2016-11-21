import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;

public class AuctionClient extends UnicastRemoteObject implements AuctionClientIntf {

  // each client running this will have their own static client object
  private static AuctionClient auctionClient;

  public AuctionClient() throws RemoteException {
    super();
    auctionClient = this;
  }

  public static void main(String[] args) {
    try {
      // Create the reference to the remote object through the rmiregistry
      AuctionHouse a = (AuctionHouse) /*(AuctionHouse) casts it to an AuctionHouse */
      Naming.lookup("rmi://localhost/AuctionHouse");

      AuctionClient auctionClient = new AuctionClient();

      //auctionClient.startHeartbeat(a);

      Timer timer = new Timer();

      timer.scheduleAtFixedRate(new HeartbeatThread(a), 15000000, 15000000); // heartbeat every 5 mins

      System.out.println("past the timer schedulin");
      // Now use the reference a to call remote methods

      Scanner standardInput = new Scanner(System.in);

      while(true) { //change to run while client is still connected?

      	System.out.print("1: Create Auction 2: Show Available Items 3: Bid on Item");
        String line = standardInput.nextLine();

        switch(Integer.parseInt(line)) {
          case 1:
            System.out.println("Please enter name of item: ");
            String name = standardInput.nextLine();
            System.out.println("Please enter starting price of item: ");
            double minPrice = Integer.parseInt(standardInput.nextLine());
            //System.out.println("Please enter date in time in the format DD/MM/YYYY/HH/MM/SS: ");
            //String date = standardInput.nextLine();
            //String[] values = date.split("/");
            Calendar cal = Calendar.getInstance();
            //cal.set(Integer.parseInt(values[2]), Integer.parseInt(values[1]), Integer.parseInt(values[0]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]));
            cal.set(2016, 10, 21, 20, 56, 30);
            a.createAuctionItem(name, minPrice, cal.getTimeInMillis()/1000, auctionClient);
            break;
          case 2:
            System.out.println(a.showAvailableAuctionItems());
            break;
          case 3:
            System.out.println("Enter id of item you would like to bid on ");
            int itemID = Integer.parseInt(standardInput.nextLine());
            System.out.println("How much would you like to bid? ");
            double bidValue = Integer.parseInt(standardInput.nextLine());
            if (a.bidOnItem(itemID, bidValue, auctionClient)) {
              System.out.println("Successfully bid on item");
            } else {
              System.out.println("Bid value not large enough");
            }
        }
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

   public void auctionFinished() throws RemoteException {
     System.out.println();
     System.out.println("Auction you were following has finished");
   }


}
