import java.util.Date;

/* each auction in the auction house has an auction obkect.
auction objects have a current bid value, starting val, closetime, name, (description?)
and a unique identifier */

public class Auction {

  private String name;
  private double minItemValue;
  private Date closeTime;
  private double currentBidValue;

  public Auction(String name, double minItemValue, Date closeTime, int id){
    name = name;
    minItemValue = minItemValue;
    currentBidValue = minItemValue;
    closeTime = closeTime;
    // instead of id here just create global variable we increment with each auction
    id = id;
  }

  public boolean bidOnItem(double bidValue) {
    //need to do a lot more in here
    if (bidValue > minItemValue) {
      minItemValue = bidValue;
      return true;
    }
    return false;
  }

}
