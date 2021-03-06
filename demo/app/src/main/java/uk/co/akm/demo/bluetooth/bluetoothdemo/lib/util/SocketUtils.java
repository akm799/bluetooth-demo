package uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Thanos Mavroidis on 18/05/2017.
 */
public class SocketUtils {
    private static final byte EOT = 4;
    private static final int EOF_INT = -1;
    private static final String TAG = SocketUtils.class.getSimpleName();

    public static String readString(InputStream is, byte[] buffer) throws IOException {
        final StringBuilder sb = new StringBuilder();
        readString(is, buffer, sb);

        return sb.toString();
    }

    private static void readString(InputStream is, byte[] buffer, StringBuilder sb) throws IOException {
        ReadResult readResult = new ReadResult();
        while (readResult.moreToRead) {
            readResult = fillBuffer(is, buffer);
            if (readResult.nRead > 0) {
                sb.append(new String(buffer, 0, readResult.nRead));
            }
        }
    }

    private static ReadResult fillBuffer(InputStream is, byte[] buffer) throws IOException {
        int totalRead = 0;
        int nReadInStep = 0;
        boolean eotRead = false;
        boolean spaceLeftInBuffer = true;

        while (!eotRead && spaceLeftInBuffer && nReadInStep != EOF_INT) {
            nReadInStep = is.read(buffer, totalRead, buffer.length - totalRead);
            if (nReadInStep > 0) {
                totalRead += nReadInStep;
            }

            spaceLeftInBuffer = (totalRead < buffer.length);
            eotRead = (buffer[totalRead - 1] == EOT);
        }

        if (eotRead) {
            totalRead--; // Don't include the EOT character in our output. It is only to tell us that there is no more data to read.
        }

        final boolean moreToRead = (!eotRead && nReadInStep != EOF_INT);
        return new ReadResult(totalRead, moreToRead);
    }

    public static final void writeString(String s, OutputStream os) throws IOException {
        os.write(s.getBytes());
        os.write(EOT); // Signal to the input stream on the other end that there is no more data to read.
        os.flush();
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                Log.e(TAG, "I/O error when closing resource.", ioe);
            }
        }
    }

    private SocketUtils() {}

    private static final class ReadResult {
        final int nRead;
        final boolean moreToRead;

        ReadResult() {
            this(0, true); // Initialization before the start of the read operation.
        }

        ReadResult(int nRead, boolean moreToRead) {
            this.nRead = nRead;
            this.moreToRead = moreToRead;
        }
    }
}
