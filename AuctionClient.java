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
  private static ArrayList<Integer> ownsTheseAuctions; //used to ensure client does not bid on their own item

  public AuctionClient() throws RemoteException {
    super();
    auctionClient = this;
    ownsTheseAuctions = new ArrayList<Integer>();
  }

  public static void main(String[] args) {
    try {
      // Create the reference to the remote object through the rmiregistry
      AuctionHouse a = (AuctionHouse) /*(AuctionHouse) casts it to an AuctionHouse */
      Naming.lookup("rmi://localhost/AuctionHouse");

      AuctionClient auctionClient = new AuctionClient();

      //auctionClient.startHeartbeat(a);

      Timer timer = new Timer();

      timer.scheduleAtFixedRate(new HeartbeatThread(a), 300000, 300000); // heartbeat every 5 mins

      System.out.println("past the timer schedulin");
      // Now use the reference a to call remote methods

      Scanner standardInput = new Scanner(System.in);

      while(true) { //change to run while client is still connected?

      	System.out.println("1: Create auction 2: Show active auctions 3: Bid on item 4: Check connection status 5: Query recently finished auctions");
        String line = standardInput.nextLine();

        switch(Integer.parseInt(line)) {
          case 1:
            System.out.println("Please enter name of item: ");
            String name = standardInput.nextLine();
            double minPrice = -1;
            System.out.println("Please enter starting price of item: ");
            while (minPrice < 0 ) {
              minPrice = Integer.parseInt(standardInput.nextLine());
              if (minPrice < 0) {
                System.out.println("Please enter a positive value");
              }
            }
            System.out.println("How many seconds from now would you like the auction to end? ");
            String sec = standardInput.nextLine();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, Integer.parseInt(sec));
            ownsTheseAuctions.add(a.createAuctionItem(name, minPrice, cal.getTimeInMillis()/1000, auctionClient));
            break;
          case 2:
            System.out.println(a.showAuctionItems(0));
            break;
          case 3:
            System.out.println("Enter id of item you would like to bid on ");
            int itemID = Integer.parseInt(standardInput.nextLine());
            double bidValue = -1;
            if (!ownsTheseAuctions.contains(itemID)){
              System.out.println("How much would you like to bid? ");
              while (bidValue < 0){
                bidValue = Integer.parseInt(standardInput.nextLine());
                if (bidValue > 0){
                  if (a.bidOnItem(itemID, bidValue, auctionClient)) {
                    System.out.println("Successfully bid on item");
                  } else {
                    System.out.println("Bid value not large enough");
                  }
                } else {
                  System.out.println("Please enter a value greater than zero");
                }
              }
            } else {
                System.out.println("You cannot bid on an auction you created.");
            }
          case 4:
            try{
              a.talk();
              System.out.println("Server still alive");
            } catch (RemoteException e) {
              System.out.println("Server died");
            }
            break;
          case 5:
            System.out.println(a.showAuctionItems(1));
            break;

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

  public void auctionFinished(int winnerID, long winningBid) throws RemoteException {
    System.out.println();
    System.out.println("Auction you had bid on has finished. The winner's id was " + winnerID + " and the winning bid was " + winningBid);
  }

  public void auctionFinishedWinner(int itemID, String itemName) throws RemoteException {
    System.out.println();
    System.out.println("Congratulations! You have won " + itemName ". This was auction number " + itemID);
  }

  public void auctionFinishedOwner(int itemID, int winnerID, String itemName) throws RemoteException {
    System.out.println();
    System.out.println("Congratulations! Your item has sold. The item was " + itemName ". The auction number " + itemID + ". The winners id was " + winnerID);
  }

  public void overtakenAlert(int id, double currentBidValue) throws RemoteException {
    System.out.println("You've been overtaken on item "+ id + ". The new highest bid is " + currentBidValue);
  }

}
