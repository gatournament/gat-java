package gat_java;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;


public abstract class TrucoAlgorithm extends JavaAlgorithm {

	@Override
	public void processMessage(JSON message) {
		//		if (message.get("action") == "play") {
		//			//			return this.play(message.get("context"));
		//		} else if (message.get("action") == "accept_truco") {
		//            boolean accept = this.acceptTruco(context);
		//            if (accept) {
		//                this.sendResponse(new HashMap()); //{'action': 'accept_truco'}
		//            } else {
		//                this.sendResponse(new HashMap()); //{'action': 'giveup_truco'}
		//            }
		//	}
	}

	@Override
	public abstract void play(Map context);

	public abstract boolean acceptTruco(Map context);

	public boolean canTruco(Map<String, String> context) {
		return Integer.parseInt(context.get("round_value")) < 12;
	}

	public void upcard() throws IOException {
		this.sendResponse(new HashMap()); //{'action': 'upcard', 'hand_card': card}
	}

	public void truco() throws IOException {
		this.sendResponse(new HashMap()); //{'action': 'truco'}
	}
}

