package gat_java;

// public static void main(String[] args) {
//     JavaAlgorithm algorithm = new JavaAlgorithm();
//     algorithm.listen();
// }
public abstract class JavaAlgorithm {
    private boolean stopped = true;
    private Object sock;

    public JavaAlgorithm() {
        context = new zmq.Context();
        this.sock = context.socket(zmq.REP);
    }

    public void listen(String host, int port) {
        this.sock.bind("ipc://" + host + ":" + port);
        System.out.println("Listening");
        this.stopped = false;
        while (! this.stopped) {
            try {
                this.readIncomingMessage();
            } catch(Exception e) {
                System.out.println(e);
                this.send_error(e);
                this.stop();
                throw e;
            }
        }
    }

    public void stop() {
        this.stopped = true;
    }

    public void readIncomingMessage() {
        String message = this.sock.recv_string();
        // message = loads(message);
        if (message == "stop") {
            this.stop();
        }
        this.processMessage(message);
    }

    public void processMessage(Map message) {
        if (message.get("action") == "play") {
            return this.play(message.get("context"));
        }
    }

    public abstract void play(Map context);

    public void sendResponse(message) {
        // message = dumps(message);
        this.sock.send_string(message);
    }

    public void sendError(errorMessage) {
        Map error = new HashMap();
        error.put("error", errorMessage);
        this.sendResponse(error);
    }

}
