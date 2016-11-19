import java.rmi.*;
import java.util.HashMap;
public interface AuctionClientIntf extends Remote {
   public void callBack(AuctionHouse a) throws java.rmi.RemoteException;

   public void callBackString(String s) throws java.rmi.RemoteException;

   public void auctionFinished() throws RemoteException; 
}
