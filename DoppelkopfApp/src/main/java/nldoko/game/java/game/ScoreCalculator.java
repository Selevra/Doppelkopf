package nldoko.game.java.game;

public class ScoreCalculator {

    private final int declarationValueRe;
    private final int declarationValueKontra;
    // gameValue: the number of achieved thresholds (1 = No120, 2 = No90, etc.)
    private final int gameValue;
    private final GameActivity.ROUND_RESULT_OR_POINTS_WINNER roundWinner;

    public ScoreCalculator(int declarationValueRe, int declarationValueKontra, int gameValue, GameActivity.ROUND_RESULT_OR_POINTS_WINNER roundWinner) {
        this.declarationValueRe = declarationValueRe;
        this.declarationValueKontra = declarationValueKontra;
        this.gameValue = gameValue;
        this.roundWinner = roundWinner;
    }

    // Example getter methods if needed
    public int getDeclarationValueRe() { return declarationValueRe; }
    public int getDeclarationValueKontra() { return declarationValueKontra; }
    public int getGameValue() { return gameValue; }

    public GameActivity.ROUND_RESULT_OR_POINTS_WINNER getRoundWinner() {
        return roundWinner;
    }

    public int calculatePointsForParty(GameActivity.GAME_PARTY party) {
        int points = 0;
        int declarationPoints = calculateDeclarationPoints(party);
        int declarationValueOpponent = (party == GameActivity.GAME_PARTY.PARTY_RE ? this.declarationValueKontra : this.declarationValueRe);

        // declarations need to be successful to get the points
        if (wasDeclarationSuccessful(party)) {
            points += declarationPoints;
            // get normal points
            if (isGameWinner(party)) {
                // get game value but at least one in case of a tie
                points += Math.max(1, this.gameValue);
            }
            // check how much better the party was compared to what the opponent declared
            if (declarationValueOpponent > 0) {
                points += (declarationValueOpponent - 1);
            }
        } else {
            // all declarations are lost and counted negative
            points -= declarationPoints;
        }

        return points;
    }

    private boolean wasDeclarationSuccessful(GameActivity.GAME_PARTY party) {
        GameActivity.GAME_PARTY otherParty = (party == GameActivity.GAME_PARTY.PARTY_RE) ? GameActivity.GAME_PARTY.PARTY_KONTRA : GameActivity.GAME_PARTY.PARTY_RE;
        int declarationValue = (party == GameActivity.GAME_PARTY.PARTY_RE) ? this.declarationValueRe : this.declarationValueKontra;

        // party has won the round and it was declared => success or no success possible depending on score
        if (isGameWinner(party)) {
            return declarationValue <= this.gameValue;
        } else {
            // party lost the round but the other team declared more than they achieved (and own team said nothing or just re/kontra)
            return declarationValue <= 1 && !wasDeclarationSuccessful(otherParty);
        }
    }

    private int calculateDeclarationPoints(GameActivity.GAME_PARTY party) {
        int declarationValue = this.declarationValueRe;
        if (party == GameActivity.GAME_PARTY.PARTY_KONTRA) {
            declarationValue = this.declarationValueKontra;
        }
        if (declarationValue > 0) {
            return declarationValue + 1; // RE / Kontra yields 2 points instead of one
        }
        else {
            return 0;
        }
    }

    private GameActivity.GAME_PARTY getTieWinner() {
        if ((this.declarationValueKontra == 0) || (this.declarationValueKontra < this.declarationValueRe)) {
            return GameActivity.GAME_PARTY.PARTY_KONTRA;
        }
        else {
            return GameActivity.GAME_PARTY.PARTY_RE;
        }
    }

    private boolean isGameWinner(GameActivity.GAME_PARTY party) {
        if (party == GameActivity.GAME_PARTY.PARTY_RE) {
            return (this.roundWinner == GameActivity.ROUND_RESULT_OR_POINTS_WINNER.RE) || (this.roundWinner == GameActivity.ROUND_RESULT_OR_POINTS_WINNER.TIE && getTieWinner() == party);
        } else {
            return (this.roundWinner == GameActivity.ROUND_RESULT_OR_POINTS_WINNER.KONTRA) || (this.roundWinner == GameActivity.ROUND_RESULT_OR_POINTS_WINNER.TIE && getTieWinner() == party);
        }
    }
}
