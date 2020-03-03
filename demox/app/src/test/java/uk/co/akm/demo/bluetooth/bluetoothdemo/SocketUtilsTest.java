package uk.co.akm.demo.bluetooth.bluetoothdemo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import uk.co.akm.demo.bluetooth.bluetoothdemo.lib.util.SocketUtils;

public class SocketUtilsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldReadString() throws IOException {
        readStringText("value", 16);
    }

    @Test
    public void shouldReadStringWhenBufferIsSmall() throws IOException {
        readStringText("A very long string value to read.", 8);
    }

    @Test
    public void shouldReadStringWhenBufferJustEnough() throws IOException {
        final String expected = "Some value.";
        readStringText(expected, expected.length());
    }

    private void readStringText(String expected, int bufferSize) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        final InputStream is = new ByteArrayInputStream(expected.getBytes());

        final String actual = SocketUtils.readString(is, buffer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotAcceptNullInputStream() throws IOException {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Input stream argument is null.");
        SocketUtils.readString(null, new byte[1]);
    }

    @Test
    public void shouldNotAcceptNullBuffer() throws IOException {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Buffer argument is null.");
        SocketUtils.readString(new ByteArrayInputStream(new byte[1]), null);
    }

    @Test
    public void shouldNotAcceptZeroLengthBuffer() throws IOException {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Buffer argument is a zero length array.");
        SocketUtils.readString(new ByteArrayInputStream(new byte[1]), new byte[0]);
    }
}
