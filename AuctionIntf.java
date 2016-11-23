import java.rmi.*;
import java.util.HashMap;
public interface AuctionIntf extends Remote {

  public int getID() throws RemoteException;

}
