package eu.uberdust.communication;

import ch.ethz.inf.vs.californium.coap.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.coap.Request;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
                    LOGGER.debug("SentCoap@" + uri + ":" + path + "{" + payload + "}");

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
        return uberdustURL.substring(0, uberdustURL.lastIndexOf(":")).replaceAll("http://", "");
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


    public static void main(String[] args) throws IOException {
        UberdustClient.getInstance();
        UberdustClient.setUberdustURL("http://192.168.1.10:8081");
        try {
            System.out.println(UberdustClient.getInstance().getUrnPrefix(1));
            System.out.println(UberdustClient.getInstance().getUrnCapabilityPrefix(1));
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Testbed getTestbedById(int testbedID) throws IOException, JSONException {
        Testbed testbed = new Testbed();
        JSONObject testbedJSON = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/json"));
        testbed.setUrnPrefix(testbedJSON.getString("urnPrefix"));
        testbed.setUrnCapabilityPrefix(testbedJSON.getString("urnCapabilityPrefix"));
        testbed.setName(testbedJSON.getString("testbedName"));
        testbed.setId(testbedID);
        return testbed;
    }

    public List<Testbed> listTestbeds() throws IOException, JSONException {
        List<Testbed> testbeds = new ArrayList<Testbed>();
        JSONArray testbedsJSON = new JSONArray(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/json"));
        for (int i = 0; i < testbedsJSON.length(); i++) {
            JSONObject testbedJSON = (JSONObject) testbedsJSON.get(i);
            Testbed testbed = getTestbedById(testbedJSON.getInt("testbedId"));
            testbeds.add(testbed);
        }
        return testbeds;
    }

    public List<Node> listNodes(int testbedId) throws IOException, JSONException {
        List<Node> nodes = new ArrayList<Node>();
        JSONObject nodesJSON = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedId + "/node/json"));
        JSONArray nodesArr = (JSONArray) nodesJSON.get("nodes");
        for (int i = 1; i < nodesArr.length(); i++) {
            String anode = nodesArr.getString(i);
            Node node = new Node();
            node.setName(anode);
            nodes.add(node);
        }
        return nodes;
    }

    public List<Node> listVirtualNodes(int testbedId) throws IOException, JSONException {
        List<Node> nodes = new ArrayList<Node>();
        JSONObject nodesJSON = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedId + "/virtualnode/json"));
        JSONArray nodesArr = (JSONArray) nodesJSON.get("nodes");
        for (int i = 1; i < nodesArr.length(); i++) {
            String anode = nodesArr.getString(i);
            Node node = new Node();
            node.setName(anode);
            nodes.add(node);
        }
        return nodes;
    }

    public List<Link> listLinks(int testbedId) throws IOException, JSONException {
        List<Link> links = new ArrayList<Link>();
        JSONObject linksJSON = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedId + "/link/json"));
        JSONArray linksArr = (JSONArray) linksJSON.get("links");
        for (int i = 1; i < linksArr.length(); i++) {
            JSONObject aLink = (JSONObject) linksArr.get(i);
            Link link = new Link();
            Node source = new Node();
            source.setName(aLink.getString("linkSource"));
            Node target = new Node();
            target.setName(aLink.getString("linkTarget"));
            link.setSource(source);
            link.setTarget(target);
            links.add(link);
        }
        return links;
    }

    public String getUrnPrefix(int testbedID) throws JSONException, IOException {
        JSONObject testbed = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/json"));
        return testbed.get("urnPrefix").toString();
    }

    public String getUrnCapabilityPrefix(int testbedID) throws JSONException, IOException {
        JSONObject testbed = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/json"));
        return testbed.get("urnCapabilityPrefix").toString();
    }


    public JSONObject getNodeCapabilities(final String node) throws JSONException, IOException {
        JSONObject capabilities = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/1/node/" + node + "/capabilities/json"));
        return capabilities;
    }


    public JSONObject getNodes(int testbedID) throws JSONException, IOException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/json"));
        return nodes;
    }

    public JSONObject getLinks(int testbedID) throws JSONException, IOException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/link/json"));
        return nodes;
    }

    public JSONObject getCapabilities(int testbedID) throws JSONException, IOException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/capability/json"));
        return nodes;
    }

    public JSONObject getNodeReading(int testbedID, String node, String capability, int count) throws JSONException, IOException {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/capability/" + capability + "/json/limit/" + count));
        return nodes;
    }

    public JSONObject getLastNodeReading(int testbedID, String node, String capability) throws Exception {
        JSONObject nodes = new JSONObject(RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/capability/" + capability + "/latestreading/json"));
        return nodes;
    }

    public String getNodeX(String testbedID, String node) throws IOException {
        return RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/position/x");
    }

    public String getNodeY(String testbedID, String node) throws IOException {
        return RestClient.getInstance().callRestfulWebService(uberdustURL + "/rest/testbed/" + testbedID + "/node/" + node + "/position/y");
    }

    public List<String> getNodeRooms(int testbedId, String nodeName) throws Exception {
        String jsonObject = ((JSONObject) ((JSONArray) getLastNodeReading(testbedId, nodeName, "room").get("readings")).get(0)).getString("stringReading");
        ArrayList<String> roomsList = new ArrayList<String>();
        Collections.addAll(roomsList, jsonObject.split(","));
        return roomsList;
    }

    public List<String> getNodeWorkstations(int testbedId, String nodeName) throws Exception {
        String jsonObject = ((JSONObject) ((JSONArray) getLastNodeReading(testbedId, nodeName, "workstation").get("readings")).get(0)).getString("stringReading");
        ArrayList<String> roomsList = new ArrayList<String>();
        Collections.addAll(roomsList, jsonObject.split(","));
        return roomsList;
    }

    public List<String> getNodeNodeTypes(int testbedId, String nodeName) throws Exception {
        String jsonObject = ((JSONObject) ((JSONArray) getLastNodeReading(testbedId, nodeName, "nodetype").get("readings")).get(0)).getString("stringReading");
        ArrayList<String> roomsList = new ArrayList<String>();
        Collections.addAll(roomsList, jsonObject.split(","));
        return roomsList;
    }
}
