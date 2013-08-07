package com.gatournament.gat_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// public static void main(String[] args) throws Exception {
//     int port = Integer.parseInt(args[0]);
//     GameAlgorithm algorithm = new GameAlgorithm();
//     algorithm.listen(port);
// }
@SuppressWarnings("unchecked")
public abstract class GameAlgorithm {
	private boolean stopped = true;
	private ServerSocket socket;
	private Socket client;
	private BufferedReader reader;

	public GameAlgorithm() {
	}

	public void listen(int port) throws Exception {
		this.socket = new ServerSocket(port);
		System.out.println("Listening on port " + port);
		this.client = socket.accept();
		System.out.println("Client connected: " + client.getInetAddress().getHostAddress());
		this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

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
		String message = reader.readLine();
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

	public void processMessage(JSONObject message) throws Exception {
		if (message.get("action").equals("play")) {
			this.play((JSONObject)message.get("context"));
		}
	}

	public abstract void play(JSONObject context) throws Exception;

	public void sendResponse(JSONObject message) throws IOException {
		String messageToSend = message.toJSONString() + "\n";
		byte[] bytes = messageToSend.getBytes();
		this.client.getOutputStream().write(bytes);
	}

	public void sendError(String errorMessage) throws IOException {
		JSONObject error = new JSONObject();
		error.put("error", errorMessage);
		this.sendResponse(error);
	}

}
