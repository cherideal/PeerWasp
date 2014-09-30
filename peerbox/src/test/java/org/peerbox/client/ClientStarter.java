package org.peerbox.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.api.interfaces.INetworkConfiguration;
import org.hive2hive.core.security.H2HDummyEncryption;
import org.hive2hive.core.security.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientStarter extends AbstractStarter {

	private static final Logger logger = LoggerFactory.getLogger(ClientStarter.class);
	
	private static boolean IS_INITIAL_NODE;
	private static String BOOTSTRAP_HOST_NAME;
	private static final String USER = "h2huser";
	private static final String PASSWORD = "SecretPassword";
	private static final String PIN = "AZBY";
	
	private ClientNode client;
	
	public static void main(String[] args) {
		parseArguments(args);
		
		
		try {
			new ClientStarter().run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseArguments(String[] args) {
		if(args.length != 2) {
			quit(args);
		}
		
		for(int i = 0; i < args.length; ++i) {
			String a = args[i];
			String[] elements = a.split("=");
			if(elements.length != 2) {
				quit(args);
			}
			
			switch(elements[0]) {
				case "initial":
					if(elements[1].equalsIgnoreCase("1")) {
						IS_INITIAL_NODE = true;
					}
					logger.info("IS_INITIAL_NODE={}", IS_INITIAL_NODE);
					break;
				case "bootstrap":
					BOOTSTRAP_HOST_NAME = elements[1];
					logger.info("BOOTSTRAP_HOST_NAME={}", BOOTSTRAP_HOST_NAME);
					break;
				default:
					logger.error("Unknown parameter provided.");
					quit(args);
					break;
			}
		}
	}

	private static void quit(String[] args) {
		logger.error("Wrong arguments provided. Exiting...");
		logger.error("Arguments:");
		for(String a : args) {
			logger.error("\t{}", a);
		}
		logger.error("Usage: initial=[0 or 1] bootstrap=[address to use]");
		System.exit(-1);
	}

	public ClientStarter() throws IOException {
		super();
	}

	private void run() {
		
		try {
			
			setup();

			// teardown();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setup() throws Exception {
		IH2HNode node = createNode();
		credentials = new UserCredentials(USER, PASSWORD, PIN);
		Path rootPath = Paths.get(BASE_PATH.toString(), String.format("%s-%s", 
				USER, String.valueOf(System.currentTimeMillis())));
		
		if(IS_INITIAL_NODE) {
			registerUser(node, credentials);
		}
		
		client = new ClientNode(node, credentials, rootPath);
	}
	
	
	private IH2HNode createNode() throws UnknownHostException {
		InetAddress bootstrapAddress = InetAddress.getByName(BOOTSTRAP_HOST_NAME); // InetAddress.getLocalHost();
		INetworkConfiguration networkConf = null;
		if (IS_INITIAL_NODE) {
			networkConf = NetworkConfiguration.create("initial");
		} else {
			networkConf = NetworkConfiguration.create("node-" + BOOTSTRAP_HOST_NAME + "-" + System.currentTimeMillis(),
					bootstrapAddress);
		}
		
		IH2HNode node = H2HNode.createNode(networkConf, FileConfiguration.createDefault(),
				new H2HDummyEncryption());
		node.connect();
		node.getFileManager().configureAutostart(false);
		node.getUserManager().configureAutostart(false);
		return node;
	}


}