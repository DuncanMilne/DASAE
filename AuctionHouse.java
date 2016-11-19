import java.util.ArrayList;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
public interface AuctionHouse extends java.rmi.Remote {

  public void createAuctionItem(String name, double minItemValue, long closeTime) throws java.rmi.RemoteException;

  public String showAvailableAuctionItems() throws RemoteException;

  public String displayFinishedAuctions() throws RemoteException;

  public void registerObject(AuctionClientIntf client, String n, int t) throws RemoteException;

  public void talk() throws RemoteException;

  public boolean heartbeatMonitor() throws RemoteException;

  public boolean bidOnItem(int itemID, double bidValue, AuctionClientIntf client) throws RemoteException;
}
