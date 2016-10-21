package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.security.PublicKey;

/**
 * Created by student13 on 10/16/2016.
 */

public class ConnectionManager {
    public static final int TIME_OUT = 5000;

    /**
     * Checks if we can connect to the internet
     * @return boolean - canConnect
     */
    public static boolean canConnect(Context context){
        boolean canConnect = true;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // check if mobile data/wifi is connected or connecting
        if(activeNetwork != null && !activeNetwork.isConnectedOrConnecting()){
            canConnect = false;
        }
        return canConnect;
    }
}
