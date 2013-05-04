package gat_java;

import gat_java.JavaAlgorithm;


public abstract class TrucoAlgorithm extends JavaAlgorithm {

    @Override
    public void processMessage(String message) {
        if (message.get("action") == "play") {
            return this.play(message.get("context"));
        } else if (message.get("action") == "accept_truco") {
            boolean accept = this.acceptTruco(context);
            if (accept) {
                this.sendResponse(new HashMap()); //{'action': 'accept_truco'}
            } else {
                this.sendResponse(new HashMap()); //{'action': 'giveup_truco'}
            }
        }
    }

    @Override
    public abstract void play(Map context);

    public abstract boolean acceptTruco(Map context);

    public boolean canTruco(Map context) {
        return Integer.parseInt(context.get("round_value")) < 12;
    }

    public void upcard() {
        this.sendResponse(new HashMap()); //{'action': 'upcard', 'hand_card': card}
    }

    public void truco() {
        this.sendResponse(new HashMap()); //{'action': 'truco'}
    }
}

