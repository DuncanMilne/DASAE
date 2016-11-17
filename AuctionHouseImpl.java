import java.util.ArrayList;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Date;
import java.io.Serializable;

public class AuctionHouseImpl extends UnicastRemoteObject implements AuctionHouse {

  public AuctionHouseImpl() throws java.rmi.RemoteException {
    super();
  }

  public Auction createAuctionItem(String name, double minItemValue, String closeTime) throws java.rmi.RemoteException {
    return new Auction("test", 2.2, new Date(), 1); // format closetime to pass as date
  }

  public boolean bidOnItem(Auction auction, double bidValue) throws RemoteException {
    auction.bidOnItem(bidValue);
    return true;
  }

  public ArrayList<Auction> displayFinishedAuctions() throws RemoteException {
    return null;
  }

  public ArrayList<Auction> showAvailableAuctionItems()throws RemoteException {
    return null;
  }


  public void registerObject(AuctionClient client, String n, int t) throws RemoteException {
   try {
     Thread.sleep(t);
   } catch(InterruptedException e) {}
     System.out.format("Calling client %s\n", n);
     try {
       client.callBack("server says hi to: " + n);
     } catch(RemoteException e) {
        e.printStackTrace();
     }
   }
}
