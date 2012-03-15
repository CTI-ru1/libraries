package eu.uberdust.network.test;

import eu.uberdust.network.NetworkManager;

public class NetworkManagerTest {


    public static void main(String[] args) {

        NetworkManager.getInstance().start("uberdust.cti.gr:80", 1);

    }
}
