import java.util.Calendar;
import java.rmi.*;
import java.util.ArrayList;
import java.rmi.server.*;
import java.net.*;
/* each auction in the auction house has an auction obkect.
auction objects have a current bid value, starting val, closetime, name, (description?)
and a unique identifier */

public class Auction extends UnicastRemoteObject implements AuctionIntf {

  public String name;
  private double minItemValue;
  private long closeTime;
  private double currentBidValue;
  private int id;
  private AuctionClientIntf currentWinner;  //client currently winning auciton
  private ArrayList<AuctionClientIntf> toCallback; //list of clients to callback when this auction finishes

  public Auction(String name, double minItemValue, long closeTime, int id) throws RemoteException {
    this.name = name;
    this.minItemValue = minItemValue;
    this.currentBidValue = minItemValue;
    this.closeTime = closeTime;
    this.id = id;
  }

  public boolean bidOnItem(double bidValue, AuctionClientIntf client) throws RemoteException{
    //need to do a lot more in here
    if (bidValue > currentBidValue) {
      currentBidValue = bidValue;
      currentWinner = client;
      // alert client theyve been overtaken #TODO
      return true;
    }
    return false;
  }

  public String toString() {
    long currentSeconds = Calendar.getInstance().getTimeInMillis()/1000;
    String returnString = "name is " + name + " current bid is " + currentBidValue + " closeTime is " + (closeTime - currentSeconds) + " milliseconds from now";
    return returnString;
  }

  public long getCloseTime() {
    return closeTime;
  }

}
