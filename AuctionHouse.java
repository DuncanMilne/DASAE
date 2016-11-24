import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;

public interface AuctionHouse extends java.rmi.Remote {

  public int createAuctionItem(String name, double minItemValue, long closeTime, AuctionClientIntf client) throws java.rmi.RemoteException;

  public String showAuctionItems(int activeOrFinished) throws RemoteException;

  public boolean heartbeatMonitor() throws RemoteException;

  public boolean bidOnItem(int itemID, double bidValue, AuctionClientIntf client) throws RemoteException;

  public String talk() throws RemoteException;

  public int getNextClientID() throws RemoteException;
}
