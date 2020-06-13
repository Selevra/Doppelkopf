package nldoko.game.java.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import nldoko.game.java.data.DokoData.GAME_ROUND_RESULT_TYPE;
import nldoko.game.R;



public class RoundClass implements Serializable  {


	private static final long serialVersionUID = -7650567591631089724L;
	private int mID;
	private int mPoints;
	private int mBockCount;
	private GAME_ROUND_RESULT_TYPE mRoundType;
	private String mRoundResult;
	private String mReAnsagen;
	private String mContraAnsagen;
	private String mReSpecial;
	private String mContraSpecial;

	
	public RoundClass(int id,int points,int bockCount){
		this.mID = id;
		this.mBockCount 	= bockCount;
		this.mPoints	= points;
		this.mRoundResult = "";
		this.mReAnsagen = "";
		this.mContraAnsagen = "";
		this.mReSpecial = "";
		this.mContraSpecial = "";
	}

    public RoundClass(int id, GAME_ROUND_RESULT_TYPE type, int points,int bockCount){
        this.mID = id;
        this.mBockCount 	= bockCount;
        this.mPoints	= points;
        this.mRoundType = type;
        this.mRoundResult = "";
        this.mReAnsagen = "";
        this.mContraAnsagen = "";
		this.mReSpecial = "";
		this.mContraSpecial = "";
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

    public void setAnsagen(String reAnsagen, String contraAnsagen) {
	    if (reAnsagen != null) {
            this.mReAnsagen = reAnsagen;
        }
	    if (contraAnsagen != null) {
            this.mContraAnsagen = contraAnsagen;
        }
        Log.d("XML", "Set Re Ansagen: " + this.mReAnsagen);
        Log.d("XML", "Set Contra Ansagen: " + this.mContraAnsagen);
    }

	public void setSpecialPoints(ArrayList<String> reSpecial, ArrayList<String> contraSpecial) {
		this.mReSpecial = "";
		this.mContraSpecial = "";
		Log.d("XML", "Re Special (count): " + Integer.toString(reSpecial.size()));
		for (int i=0; i<reSpecial.size();i++){
			Log.d("XML", "Re Special: " + reSpecial.get(i));
		}
		Log.d("XML", "Re Special: " + this.mReSpecial);
		for (int i=0; i<reSpecial.size(); i++) {
			this.mReSpecial += reSpecial.get(i);
			if (i < reSpecial.size() - 1) {
				this.mReSpecial += ", ";
			}
			Log.d("XML", "Re Spiecial: " + this.mReSpecial);
		}
	}

	public String getRoundResult() {
        Log.d("XML", "Get Round Result: " + this.mRoundResult);
	    return this.mRoundResult;
    }

    public String getReAnsagen() {
        Log.d("XML", "Get Re Ansagen: " + this.mReAnsagen);
        return this.mReAnsagen;
    }

    public String getContraAnsagen() {
        Log.d("XML", "Set Contra Ansagen: " + this.mContraAnsagen);
	    return this.mContraAnsagen;
    }

	public String getReSpecial() {
//		Log.d("RoundClass", this.mReSpecial.toString());
		Log.d("XML", "Get Re special: " + this.mReSpecial);
		return this.mReSpecial;
	}

	public String getContraSpecial() {
//		Log.d("RoundClass", this.mReSpecial.toString());
		Log.d("XML", "Get contra special: " + this.mReSpecial);
		return this.mContraSpecial;
	}
}
