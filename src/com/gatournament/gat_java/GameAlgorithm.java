package com.gatournament.gat_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// public static void main(String[] args) throws Exception {
//     int port = Integer.parseInt(args[0]);
//     int logLevel = Integer.parseInt(args[1]);
//     GameAlgorithm algorithm = new GameAlgorithm();
//     algorithm.configLog(logLevel);
//     algorithm.listen(port);
// }
@SuppressWarnings("unchecked")
public abstract class GameAlgorithm {
	private boolean stopped = true;
	private ServerSocket socket;
	private Socket client;
	private BufferedReader reader;
	protected Logger logger;

	public GameAlgorithm() {
		this.logger = Logger.getLogger("GATJava");
	}

	public void configLog(int logLevel) {
		Formatter formatter = new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getMessage();
			}
		};
		for (Handler h: this.logger.getHandlers()) {
			h.setFormatter(formatter);
		}
		switch (logLevel) {
		case 10:
			logger.setLevel(Level.CONFIG);
			break;
		case 20:
			logger.setLevel(Level.INFO);
			break;
		case 30:
			logger.setLevel(Level.WARNING);
			break;
		case 40:
			logger.setLevel(Level.SEVERE);
			break;
		case 50:
			logger.setLevel(Level.SEVERE);
			break;
		default:
			logger.setLevel(Level.INFO);
			break;
		}
	}

	public void log(String message, Level level) {
		logger.log(level, "[GATJava] " + message);
	}

	public void listen(int port) throws Exception {
		this.socket = new ServerSocket(port);
		this.log("Listening on port " + port, Level.CONFIG);
		this.client = socket.accept();
		this.log("Client connected: " + client.getInetAddress().getHostAddress(), Level.CONFIG);
		this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));

		this.stopped = false;
		while (! this.stopped) {
			try {
				this.readIncomingMessage();
			} catch(Exception e) {
				this.log(e.toString(), Level.SEVERE);
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
