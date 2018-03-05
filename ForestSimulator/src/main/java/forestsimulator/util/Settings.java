package forestsimulator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Settings extends Properties {

    private final Map<String, String> values = new LinkedHashMap<>();

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        load(new BufferedReader(new InputStreamReader(inStream)));
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        super.load(new ByteArrayInputStream(preprocessPropertiesFile(buffer(reader)).toByteArray()));
    }

    @Override
    public void store(OutputStream out, String comments) throws IOException {
        internalStore(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
               comments,
               true);
    }

    @Override
    public void store(Writer writer, String comments) throws IOException {
        internalStore(
                (writer instanceof BufferedWriter)?(BufferedWriter)writer : new BufferedWriter(writer),
                comments,
                false);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        return values.put((String) key, (String) value);
    }

    @Override
    public String getProperty(String key) {
        return values.get(key);
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        final Iterator<String> keys = values.keySet().iterator();
        return new Enumeration<Object>() {

            @Override
            public boolean hasMoreElements() {
                return keys.hasNext();
            }

            @Override
            public Object nextElement() {
                return keys.next();
            }
        };
    }

    private ByteArrayOutputStream preprocessPropertiesFile(BufferedReader in) throws IOException {
        Scanner preprocess = new Scanner(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while(preprocess.hasNext()) {
            out.write(preprocess.nextLine().replace("\\", "\\\\").getBytes());
            out.write("\n".getBytes());
        }
        return out;
    }
    
    private void internalStore(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
        if (comments != null) {
            writeComments(bw, comments);
        }
        synchronized (this) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                key = saveWithConversion(key, true, escUnicode);
                /* No need to escape embedded and trailing spaces for value, hence
                 * pass false to flag.
                 */
                val = saveWithConversion(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }

    /*
     * Converts unicodes to encoded &#92;uxxxx and escapes
     * special characters with a preceding slash
     */
    private String saveWithConversion(String theString,
            boolean escapeSpace,
            boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);

        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
//                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '=': // Fall through
//                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    private static void writeComments(BufferedWriter bw, String comments)
            throws IOException {
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        char[] uu = new char[6];
        uu[0] = '\\';
        uu[1] = 'u';
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current) {
                    bw.write(comments.substring(last, current));
                }
                if (c > '\u00ff') {
                    uu[2] = toHex((c >> 12) & 0xf);
                    uu[3] = toHex((c >> 8) & 0xf);
                    uu[4] = toHex((c >> 4) & 0xf);
                    uu[5] = toHex(c & 0xf);
                    bw.write(new String(uu));
                } else {
                    bw.newLine();
                    if (c == '\r'
                            && current != len - 1
                            && comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1
                            || (comments.charAt(current + 1) != '#'
                            && comments.charAt(current + 1) != '!')) {
                        bw.write("#");
                    }
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current) {
            bw.write(comments.substring(last, current));
        }
        bw.newLine();
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    private static char toHex(int nibble) {
        return HEX_DIGITS[(nibble & 0xF)];
    }

    /**
     * A table of hex digits
     */
    private static final char[] HEX_DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private BufferedReader buffer(Reader reader) {
        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        }
        return new BufferedReader(reader);
    }
}
