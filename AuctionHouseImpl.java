import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class AuctionHouseImpl extends UnicastRemoteObject implements AuctionHouse {

  private static HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>(); // int is id, auction is auction pertaining to the id
  private static int currentId = 0;

  public AuctionHouseImpl() throws java.rmi.RemoteException {
    super();
  }

  public void createAuctionItem(String name, double minItemValue, Date closeTime) throws java.rmi.RemoteException {
    auctions.put(currentId,new Auction(name, minItemValue, closeTime, currentId));
    currentId++;
  }

  public boolean bidOnItem(Auction auction, double bidValue) throws RemoteException {
    auction.bidOnItem(bidValue);
    return true;
  }

  public ArrayList<Auction> displayFinishedAuctions() throws RemoteException {
    return null;
  }

  //stop returning a hashmap
  public String showAvailableAuctionItems()throws RemoteException {
    Iterator it = auctions.entrySet().iterator();
    String returnString = "";
    while (it.hasNext()){
        HashMap.Entry pair = (HashMap.Entry)it.next();
        returnString += "Listing " + pair.getKey() + " Item is " +  pair.getValue().toString() + "\n";
    }
    return returnString;
  }

  public void talk() throws RemoteException {
    System.out.println("got here");
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
