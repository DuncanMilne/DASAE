import java.util.Calendar;
import java.rmi.*;
import java.util.ArrayList;
import java.rmi.server.*;
import java.net.*;
import java.io.Serializable;

/* each auction in the auction house has an auction obkect.
auction objects have a current bid value, starting val, closetime, name, (description?)
and a unique identifier */

public class Auction extends UnicastRemoteObject implements AuctionIntf, Serializable {

  public String name;
  private double minItemValue;
  private long closeTime;
  private double currentBidValue;
  private int id;
  private AuctionClientIntf currentWinner;  //client currently winning auciton
  private AuctionClientIntf owner;  //client who created auction
  private ArrayList<AuctionClientIntf> toCallback; //list of clients to callback when this auction finishes

  public Auction(String name, double minItemValue, long closeTime, int id, AuctionClientIntf client) throws RemoteException {
    this.name = name;
    this.minItemValue = minItemValue;
    this.currentBidValue = minItemValue;
    this.closeTime = closeTime;
    this.id = id;
    this.owner = client;
    currentBidValue = -1;
    toCallback = new ArrayList<AuctionClientIntf>();
  }

  public boolean bidOnItem(double bidValue, AuctionClientIntf client) throws RemoteException{  //probably need to make sure this is synchronized
    if (bidValue > currentBidValue && (Calendar.getInstance().getTimeInMillis()/1000) < closeTime && this.owner != client) { //check whether the time the bid was placed is after the end of the auction, if so, bid fails
      if (currentWinner!=null)
        currentWinner.overtakenAlert(id, bidValue);
      currentBidValue = bidValue;
      currentWinner = client;
      if (!toCallback.contains(client)){
        toCallback.add(client);
      }
      return true;
    }
    return false;
  }

  public String toAuctionString(int activeOrFinished) {
    long currentSeconds = Calendar.getInstance().getTimeInMillis()/1000;
    String returnString = "";
    if (activeOrFinished == 0) {
      returnString += " has name \"" + name + "\", has a current max bid of " + currentBidValue + " and will end " + ((closeTime - currentSeconds)/1000) + " seconds from now\n";
    } else {
      if (currentBidValue!=-1) {
        returnString += " has name \"" + name + "\" and the winning bid was "+ currentBidValue;
      } else {
        returnString += " has name \"" + name + "\" but the item was never bid on";
      }
    }
    return returnString;
  }

  public long getCloseTime() {
    return closeTime;
  }

  public ArrayList<AuctionClientIntf> getToCallback() {
    return toCallback;
  }

    public AuctionClientIntf getOwner() {
      return owner;
    }

    public AuctionClientIntf getCurrentWinner() {
      return owner;
    }

}
