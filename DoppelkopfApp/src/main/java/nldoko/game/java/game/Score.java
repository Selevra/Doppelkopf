package nldoko.game.java.game;

public class Score {
    private int rePoints = 0;
    private int kontraPoints = 0;

    public void addPoints(GameActivity.GAME_PARTY party, int value) {
        if (party == GameActivity.GAME_PARTY.PARTY_RE) {
            rePoints += value;
        } else {
            kontraPoints += value;
        }
    }

    public int getRePoints() {
        return rePoints;
    }

    public int getKontraPoints() {
        return kontraPoints;
    }

    public int getNetPoints() {
        return rePoints - kontraPoints;
    }

    public void reset() {
        rePoints = 0;
        kontraPoints = 0;
    }
}
