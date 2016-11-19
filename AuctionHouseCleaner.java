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
      while (it.hasNext()){
          HashMap.Entry pair = (HashMap.Entry)it.next();
          cal = Calendar.getInstance();
          auction = (Auction) pair.getValue(); // should always return an auction so safe to cast to auction
          System.out.println("auctions closetime " + auction.getCloseTime());
          System.out.println("auctions closetime " + cal.getTimeInMillis()/1000);
          if (auction.getCloseTime() < (cal.getTimeInMillis()/1000)) {
            System.out.println("found finished auction");
            for (AuctionClientIntf client:a.auctions.get(pair.getKey()).getToCallback()) {
              try {
                System.out.println("here " +  a.auctions.get(pair.getKey()).getToCallback().size());
                client.auctionFinished();
                System.out.println("now here ");
              } catch (RemoteException e) {
                System.out.println("Remote Exception");
              }
            }
            a.auctions.remove(pair.getKey());
            a.finishedAuctions.put((int)pair.getKey(),auction);
          }
        }
    }
  }

  public AuctionHouseCleaner(AuctionHouseImpl a){
    this.a = a;
  }

}
