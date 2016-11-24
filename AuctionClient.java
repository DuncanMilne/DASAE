import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Random;

public class AuctionClient extends UnicastRemoteObject implements AuctionClientIntf {

  // each client running this will have their own static client object
  private static AuctionClient auctionClient;
  private static ArrayList<Integer> ownsTheseAuctions; //used to ensure client does not bid on their own item
  public static int id;

  public AuctionClient() throws RemoteException {
    super();
    auctionClient = this;
    ownsTheseAuctions = new ArrayList<Integer>();
  }

  public AuctionClient(int id) throws RemoteException {
    super();
    ownsTheseAuctions = new ArrayList<Integer>();
    this.id = id;
  }

  public static void main(String[] args) {
    AuctionHouse a= null;
    Scanner standardInput = null;

    try {
      // Create the reference to the remote object through the rmiregistry
      a = (AuctionHouse) /*(AuctionHouse) casts it to an AuctionHouse */
        Naming.lookup("rmi://localhost/AuctionHouse");
        standardInput = new Scanner(System.in);
      // create a user defined number of clients if they enter secret codeword
        if (args.length > 0) {
          if (args[0].equals("testingtesting123")){
            System.out.println("How many threads would you like to create: ");
            int numberOfThreads = Integer.parseInt(standardInput.nextLine());
            System.out.println("How many auctions would you like to create: ");
            int numberOfAuctions = Integer.parseInt(standardInput.nextLine());
            System.out.println("How many clients would you like to create: ");
            int numberOfClients = Integer.parseInt(standardInput.nextLine());
            for (int i = 0; i< numberOfThreads; i++) {
              Thread t = null;
              t = new Thread(new AuctionClientThread(numberOfAuctions, a, numberOfClients));
              t.start();
            }

          }
        }

        AuctionClient auctionClient = new AuctionClient();

        auctionClient.id = a.getNextClientID();

        a.login(auctionClient);

      // Catch the exceptions that may occur – bad URL, Remote exception
      // Not bound exception or the arithmetic exception that may occur in
      // one of the methods creates an arithmetic error (e.g. divide by zero)
    }catch (MalformedURLException murle) {
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


      //auctionClient.startHeartbeat(a);

      Timer timer = new Timer();

      timer.scheduleAtFixedRate(new HeartbeatThread(a), 300000, 300000); // heartbeat every 5 mins

      // Now use the reference a to call remote methods

      System.out.println("");
      String line;

      while(true) { //change to run while client is still connected?

        try{
      	System.out.println("1: Create auction 2: Show active auctions 3: Bid on item 4: Check connection status and server load 5: Query recently finished auctions 6: Logout");
        line = standardInput.nextLine();

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
            int returnID = a.createAuctionItem(name, minPrice, cal.getTimeInMillis()/1000, auctionClient);
            ownsTheseAuctions.add(returnID);
            System.out.println("you have successfully created an item, the ID of the item is " + returnID);
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
              System.out.println(a.talk());
            } catch (RemoteException e) {
              System.out.println("Server dead, disconnecting.");
              System.exit(0);
            }
            break;
          case 5:
            System.out.println(a.showAuctionItems(1));
            break;
          case 6:
            a.logout(auctionClient);
            System.exit(0);
        }
      } catch (RemoteException re) {
          System.out.println("RemoteException");
          System.out.println(re);
        } catch (java.lang.ArithmeticException ae) {
          System.out.println("java.lang.ArithmeticException");
          System.out.println(ae);
        } catch (java.lang.NullPointerException npe) {
          System.out.println("java.lang.NullPointerException");
          System.out.println(npe);
        } catch (java.lang.NumberFormatException nfe) {
          System.out.println("java.lang.NumberFormatExceptionException");
          System.out.println(nfe);
        }
      //for show available auctions do callback that returns the
      // auctionClient.callBack(a); disable temporarily
    }
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

  public void auctionFinished(int winnerID, double winningBid) throws RemoteException {
    System.out.println();
    System.out.println("Auction you had bid on has finished. The winner's id was " + winnerID + " and the winning bid was " + winningBid);
  }

  public void auctionFinishedWinner(int itemID, int winnerID, String itemName) throws RemoteException {
    System.out.println();
    System.out.println("Congratulations! You have won " + itemName + ". This was auction number " + itemID);
  }

  public void auctionFinishedOwner(int itemID, int winnerID, String itemName) throws RemoteException {
    System.out.println();
    if (winnerID != -1) {
      System.out.println("Congratulations! Your item has sold. The item was " + itemName + ". The auction number " + itemID + ". The winners id was " + winnerID);
    } else {
      System.out.println("Your item " + itemName + " has not sold");
    }
  }

  public void overtakenAlert(int id, double currentBidValue) throws RemoteException {
    System.out.println("You've been overtaken on item "+ id + ". The new highest bid is " + currentBidValue);
  }

  public int getID() throws RemoteException {
    return id;
  }

  public void setID(int nextID) throws RemoteException {
    this.id = nextID;
  }

}
