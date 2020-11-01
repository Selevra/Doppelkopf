package nldoko.game.java.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import nldoko.game.java.data.DokoData.GAME_ROUND_RESULT_TYPE;
import nldoko.game.R;
import nldoko.game.java.game.GameActivity;


public class RoundClass implements Serializable  {


	private static final long serialVersionUID = -7650567591631089724L;
	private int mID;
	private int mPoints;
	private int mBockCount;
	private GAME_ROUND_RESULT_TYPE mRoundType;
	// detailed round infos
	private String mRoundResult;
	private String mRoundTypeDetailed;
	private String mReMembers;
	private String mKontraMembers;
	private String mSuspendedPlayers;
	private String mReAnsagen;
	private String mKontraAnsagen;
	private String mReSpecial;
	private String mKontraSpecial;

	
	public RoundClass(int id,int points,int bockCount){
		this.mID = id;
		this.mBockCount 	= bockCount;
		this.mPoints	= points;
		this.mRoundResult = "";
		this.mReAnsagen = "";
		this.mKontraAnsagen = "";
		this.mReSpecial = "";
		this.mKontraSpecial = "";
	}

    public RoundClass(int id, GAME_ROUND_RESULT_TYPE type, int points,int bockCount){
        this.mID = id;
        this.mBockCount 	= bockCount;
        this.mPoints	= points;
        this.mRoundType = type;
        this.mRoundResult = "";
        this.mReMembers = "";
        this.mKontraMembers = "";
        this.mSuspendedPlayers = "";
        this.mReAnsagen = "";
        this.mKontraAnsagen = "";
		this.mReSpecial = "";
		this.mKontraSpecial = "";
    }
	
	public int getID(){
		return this.mID;
	}
	
	public void setID(int id){
		this.mID =  id;
	}

	public int getPoints(){
		return this.mPoints * (this.mBockCount!=0 ? (int)Math.pow(2, this.mBockCount) : 1 );
	}
	
	public int getPointsWithoutBock(){
		return this.mPoints;
	}
	
	public void setPoints(int p){
		this.mPoints = p;
	}
	
	
	public int getBockCount(){
		return mBockCount;
	}
	
	public void setBockCount(int bc){
		this.mBockCount = bc;
	}
	
	public GAME_ROUND_RESULT_TYPE getRoundType(){
		return mRoundType;
	}

	public void setRoundType(int winner_count, int active_player){
		if(winner_count == 1){
			//Win solo
			this.mRoundType = GAME_ROUND_RESULT_TYPE.WIN_SOLO;
		}
		else if(winner_count == 3 && active_player == 4){
			//Lose solo
			this.mRoundType = GAME_ROUND_RESULT_TYPE.LOSE_SOLO;
		}

		// 5 active
		else if(winner_count == 4 && active_player == 5){
			//Lose solo
			this.mRoundType = GAME_ROUND_RESULT_TYPE.LOSE_SOLO;
		}
		else if(winner_count == 3 && active_player == 5){
			//3 win vs. 2 lose
			this.mRoundType = DokoData.GAME_ROUND_RESULT_TYPE.FIVEPLAYER_3WIN;
		}
		else if(winner_count == 2 && active_player == 5){
			//2 win vs. 3 lose
			this.mRoundType = GAME_ROUND_RESULT_TYPE.FIVEPLAYER_2WIN;
		}

		//6 active
		else if(winner_count == 5 && active_player == 6){
			//Lose solo
			this.mRoundType = GAME_ROUND_RESULT_TYPE.LOSE_SOLO;
		}
		else if(winner_count == 2 && active_player == 6){
			//2 win vs. 4 lose
			this.mRoundType = GAME_ROUND_RESULT_TYPE.SIXPLAYER_2WIN;
		}
		else if(winner_count == 3 && active_player == 6){
			//3 win vs. 3 lose
			this.mRoundType = GAME_ROUND_RESULT_TYPE.SIXPLAYER_3WIN;
		}
		else if(winner_count == 4 && active_player == 6){
			//4 win vs. 2 lose
			this.mRoundType = GAME_ROUND_RESULT_TYPE.SIXPLAYER_4WIN;
		}
		else{
			this.mRoundType = GAME_ROUND_RESULT_TYPE.NORMAL;
		}
	}
		
	public String getRoundTypeAsAtring(Context c){
		String res;
		switch(mRoundType){
			case LOSE_SOLO:
			case WIN_SOLO:
				res = c.getResources().getString(R.string.str_round_type_solo);
				if (res != null) {
					return res;
				} else {
					return "Solo";
				}
			case FIVEPLAYER_2WIN:
			case FIVEPLAYER_3WIN:
				res = c.getResources().getString(R.string.str_round_type_3vs2);
				if (res != null) {
					return res;
				} else {
					return "3vs2";
				}
			case SIXPLAYER_2WIN:
			case SIXPLAYER_4WIN:
				res = c.getResources().getString(R.string.str_round_type_4vs2);
				if (res != null) {
					return res;
				} else {
					return "4vs2";
				}
			case SIXPLAYER_3WIN:
				res = c.getResources().getString(R.string.str_round_type_3vs3);
				if (res != null) {
					return res;
				} else {
					return "3vs3";
				}
			default:
				res = c.getResources().getString(R.string.str_round_type_2vs2);
				if (res != null) {
					return res;
				} else {
					return "2vs2";
				}
		}
	}

	public void setRoundResult(String roundResult) {
	    this.mRoundResult = roundResult;
        Log.d("XML", "Set Round Result: " + this.mRoundResult);
    }

	public void setRoundTypeDetailed(String roundType) {
		this.mRoundTypeDetailed = roundType;
		Log.d("XML", "Set Round Type (detailed): " + this.mRoundTypeDetailed);
	}

    public void setAnsagen(String reAnsagen, String kontraAnsagen) {
	    if (reAnsagen != null) {
            this.mReAnsagen = reAnsagen;
        }
	    if (kontraAnsagen != null) {
            this.mKontraAnsagen = kontraAnsagen;
        }
        Log.d("XML", "Set Re Ansagen: " + this.mReAnsagen);
        Log.d("XML", "Set Kontra Ansagen: " + this.mKontraAnsagen);
    }

	public void setSpecialPoints(ArrayList<String> reSpecial, ArrayList<String> kontraSpecial) {
		this.mReSpecial = concatenateListElements(reSpecial);
		this.mKontraSpecial = concatenateListElements(kontraSpecial);

		Log.d("XML", "Re Special: " + this.mReSpecial);
		Log.d("XML", "Kontra Special: " + this.mKontraSpecial);
	}

	private String concatenateListElements(ArrayList<String> list) {
		String ret = "";
		for (int i=0; i<list.size(); i++) {
			if (i != 0) {
				ret += ", ";
			}
			ret += list.get(i);
		}
		return ret;
	}

	public void setPartyMember(ArrayList<PlayerClass> players, GameActivity.USER_SELECTED_PLAYER_STATE[] states, DokoData.POINTS_CALCULATION calcType) {
		List<String> re = new ArrayList<String>();
		List<String> kontra = new ArrayList<String>();
		List<String> suspend = new ArrayList<String>();
		// make sure that we do not try to access not existing array elements
		if (states.length != players.size())
		{
			return;
		}
		for(int i=0; i<states.length; i++) {
			if (states[i] == GameActivity.USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) {
				re.add(players.get(i).getName());
			}
			else if (states[i] == GameActivity.USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE) {
				kontra.add(players.get(i).getName());
			}
			else if (states[i] == GameActivity.USER_SELECTED_PLAYER_STATE.SUSPEND_STATE) {
				suspend.add(players.get(i).getName());
			}
		}
		this.mReMembers = constructPartyMemberString(re);
		this.mKontraMembers = constructPartyMemberString(kontra);
		this.mSuspendedPlayers = constructPartyMemberString(suspend);
	}

	private String constructPartyMemberString(List<String> list) {
		String ret = "";
		for (int i=0; i<list.size(); i++) {
			if (list.get(i) == "")
			{
				Log.d("XML", "No Name set for player " + i);
				break;
			}
			if (i != 0) {
				ret += ", ";
			}
			ret += list.get(i);
		}
		return ret;
	}

	public String getRoundResult() {
	    return this.mRoundResult;
    }

	public String getRoundTypeDetailed() {
		return this.mRoundTypeDetailed;
	}

    public String getReAnsagen() {
        return this.mReAnsagen;
    }

    public String getKontraAnsagen() {
	    return this.mKontraAnsagen;
    }

	public String getReSpecial() {
		return this.mReSpecial;
	}

	public String getKontraSpecial() {
		return this.mKontraSpecial;
	}

	public String getReMembers() {
		return this.mReMembers;
	}

	public String getKontraMembers() {
		return this.mKontraMembers;
	}

	public String getSuspendedPlayers() {
		return this.mSuspendedPlayers;
	}
}
