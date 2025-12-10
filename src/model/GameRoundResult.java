package model;

public class GameRoundResult {

    private final int playerId;
    private final int correctMaxFlow;
    private final int playerAnswer;
    private final String result;
    private final TrafficNetwork network;

    public GameRoundResult(int playerId,
                           int correctMaxFlow,
                           int playerAnswer,
                           String result,
                           TrafficNetwork network) {
        this.playerId = playerId;
        this.correctMaxFlow = correctMaxFlow;
        this.playerAnswer = playerAnswer;
        this.result = result;
        this.network = network;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCorrectMaxFlow() {
        return correctMaxFlow;
    }

    public int getPlayerAnswer() {
        return playerAnswer;
    }

    public String getResult() {
        return result;
    }

    public TrafficNetwork getNetwork() {
        return network;
    }
}
