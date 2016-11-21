import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Date;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionHouseImpl extends UnicastRemoteObject implements AuctionHouse {

  protected static ConcurrentHashMap<Integer, Auction> auctions = new ConcurrentHashMap<Integer, Auction>(); // int is id, auction is auction pertaining to the id
  protected static ConcurrentHashMap<Integer, Auction> finishedAuctions = new ConcurrentHashMap<Integer, Auction>(); // int is id, auction is auction pertaining to the id
  private static int currentId = 0;
  private static Thread auctionHouseCleaner;

  public AuctionHouseImpl() throws java.rmi.RemoteException {
    super();
    auctionHouseCleaner = (new Thread(new AuctionHouseCleaner(this)));
    auctionHouseCleaner.start();
  }

  public void createAuctionItem(String name, double minItemValue, long closeTime, AuctionClientIntf client) throws java.rmi.RemoteException {
    auctions.put(currentId,new Auction(name, minItemValue, closeTime, currentId, client));
    currentId++;
  }

  public boolean bidOnItem(int itemID, double bidValue, AuctionClientIntf client) throws RemoteException {
    Auction auction = auctions.get(itemID);
    if(auction.bidOnItem(bidValue, client)) {
      return true;
    }
    return false;
  }

  public String displayFinishedAuctions() throws RemoteException {
    Iterator it = finishedAuctions.entrySet().iterator();
    String returnString = "";
    while (it.hasNext()){
        ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry)it.next();
        returnString += "Listing " + pair.getKey() + " Item is " +  pair.getValue().toString() + "\n";
    }
    return returnString;
  }

  //stop returning a hashmap
  public String showAvailableAuctionItems()throws RemoteException {
    Iterator it = auctions.entrySet().iterator();
    String returnString = "";
    while (it.hasNext()){
        ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry)it.next();
        returnString += "Listing " + pair.getKey() + " Item is " +  pair.getValue().toString() + "\n";
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
}
