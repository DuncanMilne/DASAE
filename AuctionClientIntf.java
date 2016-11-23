import java.rmi.*;
import java.util.HashMap;

public interface AuctionClientIntf extends Remote {

   public void callBack(AuctionHouse a) throws java.rmi.RemoteException;

   public void callBackString(String s) throws java.rmi.RemoteException;

   public void auctionFinishedOwner(int itemID, String itemName) throws RemoteException;

   public void auctionFinishedWinner(int itemID, int winnderID, String itemName) throws RemoteException;

   public void overtakenAlert(int id, double currentBidValue) throws RemoteException;

   public void auctionFinished(int winnerID, long winningBid) throws RemoteException;
}
