import java.util.*;
import java.rmi.*;
import java.util.Iterator;

// periodic timer used to clear auctions from auction List

public class AuctionHouseCleaner implements Runnable {

  AuctionHouseImpl a;

  public void run() {
    while (true){
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        System.out.println("Server interrupted");
      }
      Iterator it = a.auctions.entrySet().iterator();
      Calendar cal;
      Auction auction;
      AuctionClientIntf owner;
      AuctionClientIntf winner;
      while (it.hasNext()){
          HashMap.Entry pair = (HashMap.Entry)it.next();
          cal = Calendar.getInstance();
          auction = (Auction) pair.getValue(); // should always return an auction so safe to cast to auction
          //System.out.println(cal.getTimeInMillis()/1000 - auction.getCloseTime());
          if (auction.getCloseTime() < (cal.getTimeInMillis()/1000)) {
            owner = auction.getOwner();
            winner = auction.getCurrentWinner();
            System.out.println("found finished auction");
             //dont callback if dummy auction
            try {
              if (owner.getID() !=0){
                if (auction.getCurrentBid() == -1){
                  owner.auctionFinishedOwner(auction.getID(), -1, auction.getName());
                } else {
                  owner.auctionFinishedOwner(auction.getID(), winner.getID(), auction.getName());
                }
              }
              } catch (RemoteException e) {
                System.out.println("Remote Exception when trying to contact owner");
                a.users.remove(owner);
              }
            for (AuctionClientIntf client:auction.getToCallback()) {
              try {
                if (client == winner) {
                  winner.auctionFinishedWinner(auction.getID(), winner.getID(), auction.getName());
                } else {
                  client.auctionFinished(winner.getID(), auction.getCurrentBid()); //#TODO get id here
                }
              } catch (RemoteException e) {
                System.out.println("Remote Exception when trying to contact bidder");
                a.users.remove(client);
              }
            }
            a.auctions.remove(pair.getKey());
            a.finishedAuctions.put((int)pair.getKey(),auction);
          }
        }
        // iterate over finished auctions and remove them after 5 mins.
        it = a.finishedAuctions.entrySet().iterator();
        while(it.hasNext()) {
          HashMap.Entry pair = (HashMap.Entry)it.next();
          cal = Calendar.getInstance();
          auction = (Auction) pair.getValue();
          if (auction.getCloseTime() + (600) < (cal.getTimeInMillis()/1000)) { // after five minutes remove from finishedAuctions
            a.finishedAuctions.remove(pair.getKey());
            System.out.println("Removing auction " + pair.getKey() + " from list of queryable auctions");
          }
        }
    }
  }

  public AuctionHouseCleaner(AuctionHouseImpl a){
    this.a = a;
  }

}
