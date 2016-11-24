import java.rmi.*;
import java.util.HashMap;

public interface AuctionClientIntf extends Remote {

   public void callBackString(String s) throws java.rmi.RemoteException;

   public void auctionFinishedOwner(int itemID, int winnerID,  String itemName) throws RemoteException;

   public void auctionFinishedWinner(int itemID, int winnerID, String itemName) throws RemoteException;

   public void overtakenAlert(int id, double currentBidValue) throws RemoteException;

   public void auctionFinished(int winnerID, double winningBid) throws RemoteException;

   public int getID() throws RemoteException;

   public void setID(int nextID) throws RemoteException;
}
