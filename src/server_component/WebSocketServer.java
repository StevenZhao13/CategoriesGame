package server_component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

public class WebSocketServer {



	public static void runServer() {

		Server server = new Server("localhost", 8025, "/websockets", WebSocketEndPoint.class);


		
		
		try {
			server.start();
			
			
			

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please press a key to stop the server.");
			reader.readLine();			
			
		} catch (DeploymentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			server.stop();
		}


	}


	public static void main(String[] args) {
		runServer();
	}

}
