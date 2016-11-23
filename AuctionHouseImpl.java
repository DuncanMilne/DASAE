import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Iterator;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionHouseImpl extends UnicastRemoteObject implements AuctionHouse {

  protected static ConcurrentHashMap<Integer, Auction> auctions = new ConcurrentHashMap<Integer, Auction>(); // int is id, auction is auction pertaining to the id
  protected static ConcurrentHashMap<Integer, Auction> finishedAuctions = new ConcurrentHashMap<Integer, Auction>();
  private static int currentId = 3;   // set as 3 initially because 0, 1 and 2 are created to bootstrap the server
  private static Thread auctionHouseCleaner;
  private static AuctionClientIntf dummyClient;
  private static int clientCount;

  public AuctionHouseImpl() throws java.rmi.RemoteException {
    super();
    auctionHouseCleaner = (new Thread(new AuctionHouseCleaner(this)));
    auctionHouseCleaner.start();
    dummyClient = new AuctionClient();
    dummyClient.setID(0);
    Calendar cal = Calendar.getInstance();
    clientCount = 1;
    //create auction that ended a minute ago, ends in 1 min and ends in 5 mins
    auctions.put(0, new Auction("test1", 2, cal.getTimeInMillis()/1000 - 60, 0, dummyClient));
    auctions.put(1, new Auction("test2", 5, cal.getTimeInMillis()/1000 + 60, 1, dummyClient));
    auctions.put(2, new Auction("test3", 10, cal.getTimeInMillis()/1000 + 300, 2, dummyClient));

  }

  public int createAuctionItem(String name, double minItemValue, long closeTime, AuctionClientIntf client) throws java.rmi.RemoteException {
    auctions.put(currentId,new Auction(name, minItemValue, closeTime, currentId, client));
    currentId++;
    return currentId-1;
  }

  public boolean bidOnItem(int itemID, double bidValue, AuctionClientIntf client) throws RemoteException {
    Auction auction = auctions.get(itemID);
    if(auction.bidOnItem(bidValue, client)) {
      return true;
    }
    return false;
  }

  // if argument is 1 return finished if 0 return active
  public String showAuctionItems(int activeOrFinished)throws RemoteException {
    Iterator it;
    if (activeOrFinished != 1) {
      it = auctions.entrySet().iterator();
    } else {
      it = finishedAuctions.entrySet().iterator();
    }
    String returnString = "";
    Auction currentAuction;
    while (it.hasNext()){
        ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry)it.next();
        currentAuction = (Auction) pair.getValue();
        returnString += "Item with ID " + pair.getKey() +  currentAuction.toAuctionString() + "\"\n";
    }
    return returnString;
  }

  public void registerObject(AuctionClientIntf client, String n, int t) throws RemoteException {
   try {
     Thread.sleep(t);
   } catch(InterruptedException e) {}
     System.out.format("Calling client %s\n", n);
     try {
       client.callBackString("server says hi " + n);
     } catch(RemoteException e) {
        e.printStackTrace();
     }
   }

   public boolean heartbeatMonitor() throws RemoteException {
     return true;
   }

   public void talk(){}

   public int getNextClientID() throws RemoteException{
     clientCount++;
     return clientCount - 1;
   }

}
