package com.gatournament.gat_java;

import java.io.IOException;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public abstract class TrucoAlgorithm extends GameAlgorithm {
	@Override
	public void processMessage(JSONObject message) throws Exception {
		if (message.get("action").equals("play")) {
			this.play((JSONObject) message.get("context"));
		} else if (message.get("action").equals("accept_truco")) {
		    boolean accept = this.acceptTruco((JSONObject) message.get("context"));
		    if (accept) {
				JSONObject response = new JSONObject();
				response.put("action", "accept_truco");
				this.sendResponse(response);
		    } else {
				JSONObject response = new JSONObject();
				response.put("action", "giveup_truco");
				this.sendResponse(response);
		    }
		}
	}

	@Override
	public abstract void play(JSONObject context) throws Exception;

	public abstract boolean acceptTruco(JSONObject context);

	public boolean canTruco(JSONObject context) {
		return ((Boolean) context.get("round_can_truco"));
	}

	public void upcard(Object card) throws IOException {
		JSONObject response = new JSONObject();
		response.put("action", "upcard");
		response.put("hand_card", card);
		this.sendResponse(response);
	}

	public void truco() throws IOException {
		JSONObject response = new JSONObject();
		response.put("action", "truco");
		this.sendResponse(response);
	}

}

