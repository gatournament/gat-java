import com.gatournament.gat_java.TrucoAlgorithm;

import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class SampleTrucoAlgorithm extends TrucoAlgorithm {

	/**
	 * You must decide which card of your hand you want to upcard in the table.
	 * And you can truco too.
	 */
	@Override
	public void play(JSONObject context) throws Exception {
		System.out.println(context); // to see all information you have to take your decision

		Random random = new Random();
		boolean randomDecisionToTruco = random.nextInt(10) > 5;
		if (this.canTruco(context) && randomDecisionToTruco) {
			this.truco(); // only call this method if this.can_truco(context) returns true
		} else {
			JSONArray cards = (JSONArray) ((JSONObject) context.get("hand")).get("cards");
			Object randomCard = cards.get(random.nextInt(cards.size()));
			this.upcard(randomCard);
		}
	}

	@Override
	public boolean acceptTruco(JSONObject context) {
		Random random = new Random();
		return random.nextBoolean();
	}

	// Required
	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(args[0]);
		SampleTrucoAlgorithm algorithm = new SampleTrucoAlgorithm();
		algorithm.listen(port);
	}
}
