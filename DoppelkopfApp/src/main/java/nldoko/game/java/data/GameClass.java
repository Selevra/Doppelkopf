package nldoko.game.java.data;

import android.util.Log;

import nldoko.game.java.data.DokoData.GAME_CNT_VARIANT;
import nldoko.game.java.XML.DokoXMLClass;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GameClass  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private GAME_CNT_VARIANT cntVariant;

	private ArrayList<PlayerClass> mPlayers;
	private ArrayList<RoundClass> mRoundList;
	private ArrayList<RoundClass> mPreRoundList;
	
	private int mPlayerCount;
	private int mActivePlayerCount;
	private int mBockRoundLimit;
	private String mCurrentFilename;

    private java.sql.Timestamp createDate;

	public GameClass(String fromFile){
		setDefaults();
		this.mCurrentFilename = fromFile;
	}
	
	public GameClass(int playerCount, int activePlayer, int bockLimit, GAME_CNT_VARIANT cntVariant){
		this.mPlayers = new ArrayList<PlayerClass>();
		this.mRoundList = new ArrayList<RoundClass>();
		this.mPreRoundList = new ArrayList<RoundClass>();
    	this.mPlayerCount = playerCount;
    	this.mActivePlayerCount = activePlayer;    
    	this.mBockRoundLimit = bockLimit;

    	this.cntVariant = cntVariant;
    	
    	for(int i=0;i<getMAXPlayerCount();i++){
    		this.mPlayers.add(new PlayerClass(i));
    	}

        this.createDate = new Timestamp(System.currentTimeMillis());

	}
	
	private void setDefaults(){
		this.mPlayers = new ArrayList<PlayerClass>();
		this.mRoundList = new ArrayList<RoundClass>();
		this.mPreRoundList = new ArrayList<RoundClass>();
    	this.mPlayerCount = 0;
    	this.mActivePlayerCount = 0;    
    	this.mBockRoundLimit = 0;
    	this.cntVariant = GAME_CNT_VARIANT.CNT_VARIANT_NORMAL;
	}
	
	public void setPlayers(ArrayList<PlayerClass> playerList){
		this.mPlayers = playerList;
	}


    public void setRoundList(ArrayList<RoundClass> roundList){
        this.mRoundList = roundList;
    }

    public void setPreRoundList(ArrayList<RoundClass> preRoundList){
        this.mPreRoundList = preRoundList;
    }

    public boolean isAutoBockCalculationOn() {
        return true;
    }

	public void setGameCntVariant(GAME_CNT_VARIANT variant){
		this.cntVariant = variant;
	}
		
	public PlayerClass getPlayer(int pos){
		return this.mPlayers.get(pos);
	}
	
	public ArrayList<PlayerClass> getPlayers(){
		return this.mPlayers;
	}
	
	public boolean isMarkSuspendedPlayersEnable() {
		return true;
	}


	public void addRound(RoundClass round){
		this.mRoundList.add(round);
	}

	public int getRoundCount(){
		return this.mRoundList.size();
	}
		
	public ArrayList<RoundClass> getRoundList(){
		return mRoundList;
	}
	
	public ArrayList<RoundClass> getPreRoundList(){
		return mPreRoundList;
	}
	
	public ArrayList<RoundClass> mTmp(){
		return mPreRoundList;
	}
		
	public RoundClass getNewRound(){
		if((mBockRoundLimit == 0 || mPreRoundList.size() == 0) && mRoundList.size() != 0 ){
			return new RoundClass(mRoundList.get(mRoundList.size()-1).getID()+1, 0, 0);
		}
		else if(mPreRoundList.size() > 0){
			return getPreRound();
		}
		return new RoundClass(0, 0, 0);
	}
	
	private RoundClass getPreRound(){
		RoundClass r = this.mPreRoundList.get(0);
		mPreRoundList.remove(0);
		return r;
	}
	
	
	public void setActivePlayerCount(int activePlayerCount){
		this.mActivePlayerCount = activePlayerCount;
	}
	
	public int getActivePlayerCount(){
		return this.mActivePlayerCount;
	}
	
	public int getPlayerCount(){
		return this.mPlayerCount;
	}
	
	public void setPlayerCount(int playerCount){
		this.mPlayerCount = playerCount;
	}

	public int getBockRoundLimit(){
		return this.mBockRoundLimit;
	}
	
	public void setBockRoundLimit(int bockRoundLimit){
		this.mBockRoundLimit = bockRoundLimit;
	}
	
	public int getMAXPlayerCount(){
		return DokoData.MAX_PLAYER;
	}
	
	public GAME_CNT_VARIANT getGameCntVariant(){
		return this.cntVariant;
	}
	
	
	
	public boolean isReady(){
		if(mPlayers.size()== getMAXPlayerCount() && mPreRoundList.size() >= getMAXPlayerCount() ){
			return true;
		}
		return false;
	}

	public void updateBockCountPreRounds(Integer mGameBockRoundsCount, Integer mGameBockRoundsGameCount) {
		if(mBockRoundLimit == 0) {
			return;

		}
		else if(mGameBockRoundsCount == 0 && mGameBockRoundsGameCount == 0) {
			return;
		}
		else if(mBockRoundLimit <= getPlayerCount()){
            for (int games = 0; games<mGameBockRoundsCount; games++) {
                int mBockHeap = mGameBockRoundsGameCount;
                int i = 0;
                int mID = 0;
                RoundClass mRound;
                while(mBockHeap > 0){
                    //Log.d(TAG,"i: "+i+" preSize:"+mPreRoundList.size()+ " heap"+mBockHeap);
                    if(i >= mPreRoundList.size()){
                        if(mPreRoundList.size() > 0){
                            //Log.d(TAG,"1");
                            mID = mPreRoundList.get(mPreRoundList.size()-1).getID()+1;
                        }
                        else if (mRoundList.size() > 0){
                            //Log.d(TAG,"2");
                            mID = mRoundList.get(mRoundList.size()-1).getID()+1;
                        }
                        else{
                            //Log.d(TAG,"3");
                            mID = 0;
                        }
                        //Log.d(TAG,"add");

                        this.mPreRoundList.add(new RoundClass(mID,0,0));
                    }
                    mRound = mPreRoundList.get(i);
                    if(mRound.getBockCount() < mBockRoundLimit){
                        mRound.setBockCount(mRound.getBockCount()+1);
                        mBockHeap--;
                    }
                    i++;
                }
            }
		}		
	}
	
	public void editLastRound(int newRoundPoints, int[] states) {
		RoundClass mRound = mRoundList.get(mRoundList.size()-1);
		
		if (mRound != null) {
			mRound.setPoints(newRoundPoints);
			mRound.setRoundType(getWinnerCnt(states),mActivePlayerCount);
			updatePlayerPoints(mRound,states);
		}
	}

	public void addNewRound(int newRoundPoints, Integer mGameBockRoundsCount, Integer mGameBockRoundsGameCount, int[] states) {
		RoundClass mRound = getNewRound();


		mRound.setPoints(newRoundPoints);
		mRound.setRoundType(getWinnerCnt(states),mActivePlayerCount);
		//Log.d("GAMECLASS",mRound.getResultText());		
		updatePlayerPoints(mRound,states);
		addRound(mRound);

		if(mGameBockRoundsCount > 0 && mGameBockRoundsGameCount > 0) {
			updateBockCountPreRounds(mGameBockRoundsCount, mGameBockRoundsGameCount);
		}
	}

    private int getWinnerCnt(int[] states){
        int m = 0;
        for(int i=0;i<states.length;i++){
            DokoData.PLAYER_ROUND_RESULT_STATE stateForPosition  =  DokoData.PLAYER_ROUND_RESULT_STATE.valueOf(states[i]);
            if(stateForPosition == DokoData.PLAYER_ROUND_RESULT_STATE.WIN_STATE) {
                m++;
            }
        }
        return m;
    }

	private void updatePlayerPoints(RoundClass mRound, int[] states) {
		int mSoloWinPos = 0;
		int mSoloLosePos = 0;



        for(int i=0;i<getPlayerCount();i++){
            DokoData.PLAYER_ROUND_RESULT_STATE stateForPosition  =  DokoData.PLAYER_ROUND_RESULT_STATE.valueOf(states[i]);

			if(stateForPosition == DokoData.PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE) {
                getPlayer(i).updatePoints(mRound.getID(), (float) 0);
            }
			else if(stateForPosition == DokoData.PLAYER_ROUND_RESULT_STATE.WIN_STATE){
				mSoloWinPos = i;
			}
			else {
                mSoloLosePos = i;
            }
		}
		
		if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.WIN_SOLO){
			//Win solo 1vs3, 1vs4, 1vs5
			soloPointUpdate(mRound,true,mSoloWinPos,states);
		}
		else if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.LOSE_SOLO){
			//Lose solo - 3vs1, 4vs1, 5vs1
			soloPointUpdate(mRound,false,mSoloLosePos,states);
		}
		else if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.FIVEPLAYER_3WIN){
			//3vs2
			for(int i=0;i<getPlayerCount();i++){
                DokoData.PLAYER_ROUND_RESULT_STATE stateForPosition  =  DokoData.PLAYER_ROUND_RESULT_STATE.valueOf(states[i]);

				if(stateForPosition == DokoData.PLAYER_ROUND_RESULT_STATE.WIN_STATE){
					//Win
					if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_WIN) {
                        getPlayer(i).updatePoints(mRound.getID(), (float) mRound.getPoints());
                    }
					else{
                        getPlayer(i).updatePoints(mRound.getID(),(float)0);
                    }
				}
				else if(stateForPosition != DokoData.PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE){
					if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_LOSE) {
                        getPlayer(i).updatePoints(mRound.getID(), (float) ((float) mRound.getPoints() * 1.5 * -1));
                    }
					else {
                        getPlayer(i).updatePoints(mRound.getID(),(float)0);
                    }
				}
			}
		}
		else {
			float mWinFactor = 1.0f; // 2vs2 & 3vs3
            double mLoseFactor = 1.0f;

			if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.FIVEPLAYER_2WIN) {
                // 2vs3
				mWinFactor = 1.5f;
			} else if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.SIXPLAYER_2WIN) {
                // 2vs4
                mWinFactor = 2.0f;

            } else if(mRound.getRoundType() == DokoData.GAME_ROUND_RESULT_TYPE.SIXPLAYER_4WIN) {
                // 4vs2
                mLoseFactor = 2.0f;
            }

			for(int i=0;i<getPlayerCount();i++){
                DokoData.PLAYER_ROUND_RESULT_STATE stateForPosition  =  DokoData.PLAYER_ROUND_RESULT_STATE.valueOf(states[i]);

				if(stateForPosition == DokoData.PLAYER_ROUND_RESULT_STATE.WIN_STATE){
					//Win
					if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_WIN) {
						getPlayer(i).updatePoints(mRound.getID(), (mRound.getPoints() * mWinFactor));
					}
					else {
						getPlayer(i).updatePoints(mRound.getID(), 0.0f);
					}
				}
				else if(stateForPosition != DokoData.PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE){
					if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_LOSE) {
						getPlayer(i).updatePoints(mRound.getID(), (float) (-mRound.getPoints() * mLoseFactor));
					}
					else {
						getPlayer(i).updatePoints(mRound.getID(), 0.0f);
					}
				}
			}
		}

		
		//Inactive player 
		for(int i=getPlayerCount();i<getMAXPlayerCount();i++){
			getPlayer(i).updatePoints(mRound.getID(),(float)0);
		}
	}

	private void soloPointUpdate(RoundClass mRound, boolean isSoloWinner, int mSoloPos, int[] states) {
		int mPoints = 0;
		
		if(isSoloWinner) {
            mPoints = mRound.getPoints() * -1;
        }
		else {
            mPoints = mRound.getPoints();
        }

		
		for(int i=0; i<getPlayerCount();i++){
            DokoData.PLAYER_ROUND_RESULT_STATE stateForPosition  =  DokoData.PLAYER_ROUND_RESULT_STATE.valueOf(states[i]);

			if(i!=mSoloPos &&  stateForPosition != DokoData.PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE){
				if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || (isSoloWinner &&  cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_LOSE)
						|| (!isSoloWinner &&  cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_WIN)) {
					getPlayer(i).updatePoints(mRound.getID(), (float) mPoints);
				}
				else {
					getPlayer(i).updatePoints(mRound.getID(),(float)0);
				}
			}	
		}
		if(cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_NORMAL || (isSoloWinner &&  cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_WIN)
				 || (!isSoloWinner &&  cntVariant == GAME_CNT_VARIANT.CNT_VARIANT_LOSE)) {
			getPlayer(mSoloPos).updatePoints(mRound.getID(), (float) (getActivePlayerCount() - 1) * mPoints * -1);
		}
		else {
			getPlayer(mSoloPos).updatePoints(mRound.getID(),(float)0);
		}
	}


	public String toString(){
		String mStr = "";
		mStr += "Active Player: "+mActivePlayerCount+" Bock Limit: "+mBockRoundLimit+" Player Count: "+mPlayerCount;
		for(int i=0;i<mPlayerCount;i++){
			mStr += " ("+mPlayers.get(i).getName()+" = "+mPlayers.get(i).getPoints()+")";
		}
		return mStr;
	}


    public void setCurrentFilename(String filepath) {
        mCurrentFilename = filepath;
    }
	public String currentFilename() {
		return mCurrentFilename;
	}
		
	public String generateNewFilename(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate + DokoXMLClass.SAVED_GAME_FILE_SUFFIX;
	}

    public String getCreateDate(String format) {
        if (this.createDate == null) {
            return "";
        }

        String f = "MM/dd/yyyy"; // default
        if (format.length() > 0 ) {
            f = format;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(f);
            return sdf.format(this.createDate);
        } catch (Exception e) {
            Log.d("GameClass", e.toString());
            return "";
        }

    }

    public java.sql.Timestamp getCreateDateTimestamp() {
        return this.createDate;
    }

    public void setCreateDate(java.sql.Timestamp timestamp) {
        this.createDate = timestamp;
    }


}
