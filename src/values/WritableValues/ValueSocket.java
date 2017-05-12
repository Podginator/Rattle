package values.WritableValues;

import parser.ParseException;
import values.Value;
import values.ValueAbstract;

import java.io.*;
import java.net.Socket;

public class ValueSocket extends ValueAbstract implements Writable {

    Socket echoSocket;
    String socketResponse;

    public ValueSocket(String endpoint, int port) throws ParseException {
        this(endpoint, port, 1000);

    }

    public ValueSocket(String endpoint, int port, int timeout) throws ParseException {
        try {
            echoSocket = new Socket(endpoint, port);
            echoSocket.setSoTimeout(timeout);
        } catch (IOException e) {
            throw new ParseException("Cannot connect to endpoint");
        }
    }

    @Override
    public String getName() {
        return "Socket @ " + echoSocket.getInetAddress().toString();
    }

    public String toString() {
        return socketResponse;
    }

    @Override
    public void write(String content) {
        try
        {
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            out.write(content);
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            socketResponse = in.readLine();

        }
        catch ( IOException e)
        {

        }
    }


    @Override
    public int compare(Value v) {
        return v.getName().compareTo(getName());
    }



}
