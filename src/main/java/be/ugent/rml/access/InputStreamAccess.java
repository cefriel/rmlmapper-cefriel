package be.ugent.rml.access;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static be.ugent.rml.Utils.getHashOfString;

public class InputStreamAccess implements Access {

    private String stream;
    private Map<String, InputStream> inputStreamsMap;   

    InputStreamAccess(String stream, Map<String, InputStream> inputStreamsMap) {
        this.inputStreamsMap = inputStreamsMap;
        this.stream = stream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (stream == null)
            throw new IOException("InputStream key not specified");
        if (inputStreamsMap == null)
            throw new IOException("InputStreamMap not initialised");
        InputStream is = inputStreamsMap.get(stream);
        if (is == null)
            throw new IOException("InputStream " + stream + " not found in the InputStreamMap");
        // Copy the stream to avoid consuming it
        ByteArrayOutputStream baos = getOutputStream(is);
        inputStreamsMap.put(stream, new ByteArrayInputStream(baos.toByteArray()));
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private ByteArrayOutputStream getOutputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos;
    }

    /**
     * This methods returns the datatypes of the file.
     * This method always returns null, because the datatypes can't be determined from a input stream for the moment.
     * @return the datatypes of the file.
     */
    @Override
    public Map<String, String> getDataTypes() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InputStreamAccess) {
            InputStreamAccess access  = (InputStreamAccess) o;
            return stream.equals(access.getStream());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getHashOfString(stream);
    }

    /**
     * The method returns the stream name.
     * @return the stream name.
     */
    public String getStream() {
        return stream;
    }

}