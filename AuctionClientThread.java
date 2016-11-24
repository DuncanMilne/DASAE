// class used in testing
import java.util.Calendar;
import java.rmi.RemoteException;

public class AuctionClientThread implements Runnable {

  private int auctionsToCreate;
  private AuctionClientIntf auctionClientObject;
  private static AuctionHouse a;

  public void run() {
    int j =0;
    while (j<auctionsToCreate) {
      Calendar cal = Calendar.getInstance();
      try {
      a.createAuctionItem("test", 1, cal.getTimeInMillis()/1000 + 3000, auctionClientObject); //create auctions that end in 10 mins
      if (j%10000==0 && j>0){
        System.out.println("creating auction " + j);
      }
    } catch (RemoteException e) {}
      j++;
    }
  }

  public AuctionClientThread(int auctionsToCreate, AuctionHouse a, int clientsToCreate) throws RemoteException {
    this.auctionsToCreate = auctionsToCreate;
    try {
      for (int i = 1; i< clientsToCreate; i++){
        a.login(new AuctionClient());
      }
      auctionClientObject = new AuctionClient();
    } catch (RemoteException e) {

    }
    this.a = a;
  }
}
