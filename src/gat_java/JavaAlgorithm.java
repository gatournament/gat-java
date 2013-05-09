package gat_java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

// public static void main(String[] args) {
//     int port = Integer.parseInt(args[0]);
//     JavaAlgorithm algorithm = new JavaAlgorithm();
//     algorithm.listen(port);
// }
public abstract class JavaAlgorithm {
	private boolean stopped = true;
	private ServerSocket socket;
	private Socket client;

	public JavaAlgorithm() {
	}

	public void listen(int port) throws IOException, Exception {
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

	public void readIncomingMessage() {
		byte[] bytes = new byte[8192];
		//		int read = this.client.getInputStream().read(bytes);
		//		if (read > -1) {
		//			String message = new String(bytes);
		//			if (message != null) {
		//				message = message.trim();
		//			}
		//			if (message == null || message == "stop") {
		//				this.stop();
		//			} else {
		//				// message = loads(message);
		//				JSON json = JSONSerializer.toJSON(message);
		//				this.processMessage(json);
		//			}
		//		}
		//    	BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
		//    	String message = reader.readLine();
		//        String message = this.sock.recv_string();
	}

	public void processMessage(JSON message) {
		//        if (message.get("action") == "play") {
		//            return this.play(message.get("context"));
		//        }
	}

	public abstract void play(Map<String, String> context);

	public void sendResponse(Map<String, String> message) throws IOException {
		byte[] bytes = "".getBytes();
		JSONSerializer.toJSON(message);
		this.client.getOutputStream().write(bytes);
		// message = dumps(message);
		//        this.sock.send_string(message);
	}

	public void sendError(String errorMessage) {
		Map error = new HashMap();
		error.put("error", errorMessage);
		//        this.sendResponse(error);
	}

}
