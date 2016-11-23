import java.util.Calendar;
import java.rmi.*;
import java.net.*;
import java.util.HashSet;

public class Auction {

  public String name;
  private double minItemValue;
  private long closeTime;
  private double currentBidValue;
  private int id;
  private AuctionClientIntf currentWinner;  //client currently winning auciton
  private AuctionClientIntf owner;  //client who created auction
  private HashSet<AuctionClientIntf> toCallback; //list of clients to callback when this auction finishes

  public Auction(String name, double minItemValue, long closeTime, int id, AuctionClientIntf client) throws RemoteException {
    this.name = name;
    this.minItemValue = minItemValue;
    this.currentBidValue = minItemValue;
    this.closeTime = closeTime;
    this.id = id;
    this.owner = client;
    currentBidValue = -1;
    toCallback = new HashSet<AuctionClientIntf>();
  }

  public synchronized boolean bidOnItem(double bidValue, AuctionClientIntf client) throws RemoteException{  //probably need to make sure this is synchronized
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

  public String toAuctionString() {
    long currentSeconds = Calendar.getInstance().getTimeInMillis()/1000;
    String returnString = "";
    if (currentSeconds < closeTime) {
      if (currentBidValue == -1) {
        returnString += " has name \"" + name + "\", doesn't current have any bids and will end " + ((closeTime - currentSeconds)) + " seconds from now\n";
      } else {
        returnString += " has name \"" + name + "\", has a current max bid of " + currentBidValue + " and will end " + ((closeTime - currentSeconds)) + " seconds from now\n";
      }
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

  public HashSet<AuctionClientIntf> getToCallback() {
    return toCallback;
  }

  public AuctionClientIntf getOwner() {
    return owner;
  }

  public AuctionClientIntf getCurrentWinner() {
    if (currentWinner!=null) {
      return currentWinner;
    } else {
      return owner;
    }
  }

  public int getID() {
    return id;
  }

  public double getCurrentBid() {
    return currentBidValue;
  }

  public String getName() {
    return name;
  }
}
