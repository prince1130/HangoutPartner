package nyc.pleasure.partner.auth;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

/**
 * Created by Chien on 12/22/2015.
 */
public class PresenceSystem {

    private Firebase myConnectionsRef;
    private Firebase lastOnlineRef;
    private Firebase connectedRef;
    private ValueEventListener connectedListener;

    public void startProcess(String uid) {
        // since I can connect from multiple devices, we store each connection instance separately
        // any time that connectionsRef's value is null (i.e. has no children) I am offline
        myConnectionsRef = new Firebase("https://sizzling-heat-8207.firebaseio.com/user/" + uid + "/connections");
        // stores the timestamp of my last disconnect (the last time I was seen online)
        lastOnlineRef = new Firebase("https://sizzling-heat-8207.firebaseio.com/user/" + uid + "/lastOnline");
        connectedRef = new Firebase("https://sizzling-heat-8207.firebaseio.com/.info/connected");

        connectedListener = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    Firebase con = myConnectionsRef.push();
                    con.setValue(Boolean.TRUE);
                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();
                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }
            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }

    public void endProcess() {
        if (connectedRef != null && connectedListener != null) {
            connectedRef.removeEventListener(connectedListener);
        }
    }


}
