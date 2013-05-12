package gat_java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// public static void main(String[] args) throws Exception {
//     int port = Integer.parseInt(args[0]);
//     JavaAlgorithm algorithm = new JavaAlgorithm();
//     algorithm.listen(port);
// }
@SuppressWarnings("unchecked")
public abstract class JavaAlgorithm {
	private boolean stopped = true;
	private ServerSocket socket;
	private Socket client;

	public JavaAlgorithm() {
	}

	public void listen(int port) throws Exception {
		this.socket = new ServerSocket(port);
		System.out.println("Listening on port " + port);
		this.client = socket.accept();
		System.out.println("Client connected: " + client.getInetAddress().getHostAddress());

		this.stopped = false;
		while (! this.stopped) {
			try {
				this.readIncomingMessage();
			} catch(Exception e) {
				System.out.println(e);
				this.sendError(e.toString());
				this.stop();
				throw e;
			}
		}
		client.close();
		socket.close();
	}

	public void stop() {
		this.stopped = true;
	}

	public void readIncomingMessage() throws Exception {
		//    	BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		//    	String message = reader.readLine();
		//        String message = this.sock.recv_string();
		byte[] bytes = new byte[8192];
		int read = this.client.getInputStream().read(bytes);
		if (read > -1) {
			String message = new String(bytes);
			if (message != null) {
				message = message.trim();
			}
			if (message == null || message.equals("") || message.equals("stop")) {
				this.stop();
			} else {
				JSONParser parser = new JSONParser();
				JSONObject map = (JSONObject) parser.parse(message);
				this.processMessage(map);
			}
		}
	}

	public void processMessage(JSONObject message) throws Exception {
		if (message.get("action").equals("play")) {
			this.play((JSONObject)message.get("context"));
		}
	}

	public abstract void play(JSONObject context) throws Exception;

	public void sendResponse(JSONObject message) throws IOException {
		byte[] bytes = message.toJSONString().getBytes();
		this.client.getOutputStream().write(bytes);
	}

	public void sendError(String errorMessage) throws IOException {
		JSONObject error = new JSONObject();
		error.put("error", errorMessage);
		this.sendResponse(error);
	}

}
