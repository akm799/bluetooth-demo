package uk.co.akm.demo.bluetooth.bluetoothdemo.lib;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public final class RemoteResponse {
    private static final String DEFAULT_ERROR_MESSAGE = "UNKNOWN ERROR";

    private final boolean success;
    private final String data;

    public RemoteResponse(boolean success, String data) {
        this.success = success;
        this.data = data;
    }

    public boolean success() {
        return success;
    }

    public String getContent() {
        return (success ? data : null);
    }

    public String getErrorMessage() {
        if (success) {
            return null;
        }

        return (data == null ? DEFAULT_ERROR_MESSAGE : data);
    }
}
