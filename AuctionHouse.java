import java.util.ArrayList;
import java.rmi.RemoteException;

public interface AuctionHouse extends java.rmi.Remote {

  public Auction createAuctionItem(String name, double minItemValue, String closeTime) throws java.rmi.RemoteException;

  public ArrayList<Auction> showAvailableAuctionItems() throws RemoteException;

  public ArrayList<Auction> displayFinishedAuctions() throws RemoteException;

  public void registerObject(AuctionClientIntf client, String n, int t) throws RemoteException;

  public void talk() throws RemoteException;
}
