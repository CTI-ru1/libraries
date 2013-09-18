package eu.uberdust.communication;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 10/10/11
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public final class RestClient {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RestClient.class);


    /**
     * static instance(ourInstance) initialized as null.
     */
    private static RestClient ourInstance = null;

    /**
     * RestClient is loaded on the first execution of RestClient.getInstance()
     * or the first access to RestClient.ourInstance, not before.
     *
     * @return ourInstance
     */
    public static RestClient getInstance() {
        synchronized (RestClient.class) {
            if (ourInstance == null) {
                ourInstance = new RestClient();
            }
        }
        return ourInstance;
    }

    /**
     * Private constructor suppresses generation of a (public) default constructor.
     */
    private RestClient() {
    }

    /**
     * Call Remote  Rest Interface.
     *
     * @param address the address
     * @return the return String
     */
    public String callRestfulWebService(final String address) throws IOException {
        final URLConnection yc;
        BufferedReader in = null;
        try {
            final URL url = new URL(address);


            yc = url.openConnection();

            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            final StringBuilder inputLine = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                inputLine.append(str);
            }
            in.close();

            if (address.contains("payload")) {
                if (!inputLine.toString().contains("OK")) {
                    LOGGER.info("BAD RESPONSE");
                    throw new RuntimeException("Bad Response");
                }
                System.out.println("OK");
                LOGGER.info("OK");
            }
            LOGGER.debug(inputLine.toString());
            return inputLine.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        }
    }

    public String callPut(String s) throws IOException {
        LOGGER.info("calling " + s);
        URL u = new URL(s);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();

        c.setRequestMethod("PUT");
        c.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(c.getOutputStream());
        out.write("title=hello+world");
        out.close();
        c.connect();
        System.out.println(c.getResponseCode() + " " + c.getResponseMessage());

        return "";
    }

//    /**
//     * Main.
//     *
//     * @param args Arguments.
//     */
//    public static void main(final String[] args) {
//        RestClient.getInstance().callRestfulWebService(
//                "http://uberdust.cti.gr/rest/sendCommand/destination/urn:wisebed:ctitestbed:0x494/payload/1,ff,1");
//    }
}
