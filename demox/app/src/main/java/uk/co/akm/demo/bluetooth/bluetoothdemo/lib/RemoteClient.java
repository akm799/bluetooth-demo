package uk.co.akm.demo.bluetooth.bluetoothdemo.lib;

import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public interface RemoteClient {

    Collection<CharSequence> listServers();

    boolean connect(String server);

    void send(String request, ResponseCallback responseCallback);
}
