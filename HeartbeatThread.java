import java.util.*;
import java.rmi.*;

public class HeartbeatThread extends TimerTask {

    AuctionHouse a;

    public void run() {
      try {
        if(a.heartbeatMonitor()!=true) {
          System.out.println("Failed to ping server");
        } else {
          System.out.println("Server still alive");
        }
      } catch (RemoteException e) {
        System.out.println("Not able to access the server.. shutting down");
        System.exit(0);
        //unbind client from server
      }
    }

    public HeartbeatThread(AuctionHouse a){
      this.a = a;
    }
}
