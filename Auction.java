import java.util.Date;
import java.rmi.*;

import java.rmi.server.*;
import java.net.*;
/* each auction in the auction house has an auction obkect.
auction objects have a current bid value, starting val, closetime, name, (description?)
and a unique identifier */

public class Auction extends UnicastRemoteObject implements AuctionIntf {

  public String name;
  private double minItemValue;
  private Date closeTime;
  private double currentBidValue;
  private int id;

  public Auction(String name, double minItemValue, Date closeTime, int id) throws RemoteException {
    this.name = name;
    this.minItemValue = minItemValue;
    this.currentBidValue = minItemValue;
    this.closeTime = closeTime;
    this.id = id;
  }

  public boolean bidOnItem(double bidValue) throws RemoteException{
    //need to do a lot more in here
    if (bidValue > minItemValue) {
      minItemValue = bidValue;
      return true;
    }
    return false;
  }

  public String toString() {
    String returnString = "name is " + name + " minItemValue is " + minItemValue + " closeTime is ";
    return returnString;
  }

}
