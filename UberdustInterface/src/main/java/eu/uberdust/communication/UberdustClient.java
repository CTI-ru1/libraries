package eu.uberdust.communication;

import ch.ethz.inf.vs.californium.coap.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.coap.Request;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;


public final class UberdustClient {

    /**
     * Static Logger.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UberdustClient.class);

    public static String getUberdustURL() {
        return uberdustURL;
    }

    public static void setUberdustURL(String uberdustURL) {
        UberdustClient.uberdustURL = uberdustURL;
    }

    private static String uberdustURL;

    /**
     * static instance(ourInstance) initialized as null.
     */
    private static UberdustClient ourInstance = null;
    private Random rand;

    /**
     * RestClient is loaded on the first execution of RestClient.getInstance()
     * or the first access to RestClient.ourInstance, not before.
     *
     * @return ourInstance
     */
    public static UberdustClient getInstance() {
        synchronized (UberdustClient.class) {
            if (ourInstance == null) {
                ourInstance = new UberdustClient();
            }
        }
        return ourInstance;
    }

    /**
     * Private constructor suppresses generation of a (public) default constructor.
     */
    private UberdustClient() {
        rand = new Random();
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));
    }


    public void sendCoapPost(final String uri, final String path, final String payload) {
        Thread d = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(getUberdustHostname(uberdustURL));
                    byte[] sendData;
                    Request request = new Request(CodeRegistry.METHOD_POST, false);
                    request.setURI(path);
                    request.setMID((new Random()).nextInt() % 1024);
                    Option urihost = new Option(OptionNumberRegistry.URI_HOST);
                    urihost.setStringValue(uri);
                    request.addOption(urihost);
                    request.setPayload(payload);
                    request.prettyPrint();

                    sendData = request.toByteArray();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5683);
                    clientSocket.send(sendPacket);
                    clientSocket.close();
                } catch (IOException e) {
                    LOGGER.error(e, e);
                }
            }
        });
        d.start();

    }

    public static String getUberdustHostname(String uberdustURL) {
        return uberdustURL.substring(0,uberdustURL.lastIndexOf(":")).replaceAll("http://", "");
    }


    private static String url = "http://uberdust.cti.gr:8080/sparql";
    private static String ref = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX db: <http://uberdust.cti.gr/resource/>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX map: <http://uberdust.cti.gr/resource/#>\n" +
            "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn/>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX corelf: <http://purl.org/NET/corelf#>\n" +
            "PREFIX vocab: <http://uberdust.cti.gr/resource/vocab/>\n" +
            "PREFIX spt: <http://spitfire-project.eu/ontology/ns/>\n" +
            "PREFIX fn: <http://www.w3.org/2005/xpath-functions#>";


    public String makeSparqlQuery(String query) {
        query = ref + query;
//        System.out.println(query);
        HttpClient client = new HttpClient();

        PostMethod method = new PostMethod(url);

        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        method.addParameter("query", query);

        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            byte[] responseBody = method.getResponseBody();
            return new String(responseBody);

        } catch (HttpException e) {
//            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
//            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return "";
    }

    public static void main(String[] args) {
        UberdustClient.getInstance();
    }


    public JSONObject getNodeCapabilities(final String node) throws JSONException {

        JSONObject capabilities = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/1/node/" + node + "/capabilities/json"));
        return capabilities;
    }

    public JSONObject getNodes(int testbedID) throws JSONException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/json"));
        return nodes;
    }

    public JSONObject getLinks(int testbedID) throws JSONException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/link/json"));
        return nodes;
    }

    public JSONObject getCapabilities(int testbedID) throws JSONException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/capability/json"));
        return nodes;
    }

    public JSONObject getNodeReading(int testbedID, String node, String capability, int count) throws JSONException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/capability/" + capability + "/json/limit/" + count));
        return nodes;
    }

    public JSONObject getLastNodeReading(int testbedID, String node, String capability) throws JSONException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/capability/" + capability + "/latestreading/json"));
        return nodes;
    }
}
