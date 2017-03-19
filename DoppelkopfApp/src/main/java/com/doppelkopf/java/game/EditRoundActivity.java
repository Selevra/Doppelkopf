package com.doppelkopf.java.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.doppelkopf.java.DokoActivity;
import com.doppelkopf.java.R;
import com.doppelkopf.java.data.DokoData;
import com.doppelkopf.java.data.DokoData.PLAYER_ROUND_RESULT_STATE;
import com.doppelkopf.java.util.Functions;

public class EditRoundActivity extends DokoActivity {

	private static Intent intent;
	
	private static String TAG = "EditRound";

	private static TextView mTvAddRoundBockPoints;
    private static TextView mTvRoundBockPointsAutoCalc;
    private static TextView mTVRoundBockPointsAutoCalcText;
	private static RadioGroup mNewRoundBockRadioGroup; 
	private static Button mBtnEditRound;
	private static TextView mEtNewRoundPoints;
	
	private static ArrayList<TextView> mRoundPlayer = new ArrayList<TextView>();

    
    private static PlayernameClickListener mPlayernameClickListener;
    private static PlayernameLongClickListener mPlayernameLongClickListener;
    private static btnEditRoundClickListener mBtnEditRoundClickListener;

	private static ArrayList<String> mPlayerNames;
	private static ArrayList<PLAYER_ROUND_RESULT_STATE> mPlayerStates;
	
    private static int mWinnerList[];
    private static int mSuspendList[];
	
	
	private static int mActivePlayers;
	private static int mPlayerCnt;
	private static int mRoundPoints = 0;
	private static int mBockRound = 0;
    private static boolean mBockAutoCalcEnable = true;
	private static PLAYER_ROUND_RESULT_STATE mPlayerState = PLAYER_ROUND_RESULT_STATE.LOSE_STATE;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editround);
		setupDrawerAndToolbar(getResources().getString(R.string.str_edit_round));
		setBackArrowInToolbar();
        
    	intent = getIntent();
    	Bundle extras = intent.getExtras();
    	
    	String mName = "";
    	
    	mPlayerNames =  	new ArrayList<String>();
    	mPlayerStates = 	new ArrayList<PLAYER_ROUND_RESULT_STATE>();
    	mWinnerList = 		new int[DokoData.MAX_PLAYER];
    	mSuspendList = 		new int[DokoData.MAX_PLAYER];


    	if(extras != null){
        	mPlayerCnt = extras.getInt(DokoData.PLAYER_CNT_KEY,0);
        	mActivePlayers =  extras.getInt(DokoData.ACTIVE_PLAYER_KEY,0);
        	mBockRound = extras.getInt(DokoData.BOCKROUND_KEY,0);
        	mRoundPoints = extras.getInt(DokoData.ROUND_POINTS_KEY,0);
            mBockAutoCalcEnable = extras.getBoolean(DokoData.AUTO_BOCK_CALC_KEY, true);
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER || 
        			(mPlayerCnt == 0 || mActivePlayers == 0))
        		return;
        	
        	for(int k=0;k<mPlayerCnt;k++){
        		mName = extras.getString(DokoData.PLAYERS_KEY[k],"");
        		mPlayerState = (PLAYER_ROUND_RESULT_STATE)intent.getSerializableExtra(DokoData.PLAYERS_KEY[k]+"_STATE");
        		if(mName == null || mName.length() == 0) return;
        		mPlayerNames.add(mName);
        		mPlayerStates.add(mPlayerState);

        	}
        }
    	
    	LinearLayout mLayout = (LinearLayout)findViewById(R.id.game_edit_round_main_layout);
        if(mLayout != null){
            mPlayernameLongClickListener = new PlayernameLongClickListener();
            mPlayernameClickListener = new PlayernameClickListener();
            mBtnEditRoundClickListener = new btnEditRoundClickListener();
        	setUIEditNewRound(mLayout, mInflater);
        }
        
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }


	private static void setUIEditNewRound(View rootView, LayoutInflater inflater) {
		ImageView mIv;
		TextView mTv;
		String mStr;

        mEtNewRoundPoints = (TextView)rootView.findViewById(R.id.game_add_round_points_entry);
        mEtNewRoundPoints.setText(""+mRoundPoints);

		loadUINewRoundPlayerSection(rootView, inflater);

		mBtnEditRound = (Button)rootView.findViewById(R.id.btn_game_edit_round);
		mBtnEditRound.setOnClickListener(mBtnEditRoundClickListener);

		mTvAddRoundBockPoints = (TextView)rootView.findViewById(R.id.game_add_round_bock_points);
		if(mBockRound > 0){
			mStr = rootView.getResources().getString(R.string.str_bockround)+" ";
			mStr += Functions.getBockCountAsString(mBockRound);
			mTvAddRoundBockPoints.setText(mStr);
			mTvAddRoundBockPoints.setVisibility(View.VISIBLE);
		}

        mTVRoundBockPointsAutoCalcText = (TextView)rootView.findViewById(R.id.game_add_round_bock_auto_calc_text);
        mTvRoundBockPointsAutoCalc = (TextView)rootView.findViewById(R.id.game_add_round_bock_auto_calc_onoff);
        if(mBockAutoCalcEnable){
            mTvRoundBockPointsAutoCalc.setText(rootView.getResources().getString(R.string.str_yes));
        } else {
            mTvRoundBockPointsAutoCalc.setText(rootView.getResources().getString(R.string.str_no));
        }
		if (mBockRound == 0) {
            mTVRoundBockPointsAutoCalcText.setVisibility(View.INVISIBLE);
            mTvRoundBockPointsAutoCalc.setVisibility(View.INVISIBLE);
        } else {
            mTVRoundBockPointsAutoCalcText.setVisibility(View.VISIBLE);
            mTvRoundBockPointsAutoCalc.setVisibility(View.VISIBLE);
        }
		/*mNewRoundBockRadioGroup = (RadioGroup)rootView.findViewById(R.id.game_add_round_bock_radio);
		mRNewRoundBockYes = (RadioButton)rootView.findViewById(R.id.game_add_round_bock_radio_yes);
		mRNewRoundBockNo = (RadioButton)rootView.findViewById(R.id.game_add_round_bock_radio_no);
		mNewRoundBockRadioGroup.setEnabled(false);
		mRNewRoundBockYes.setEnabled(false);
		mRNewRoundBockNo.setEnabled(false);*/

		
		mIv = (ImageView)rootView.findViewById(R.id.icon);
		mIv.setImageDrawable(rootView.getResources().getDrawable(R.drawable.social_cc_bcc));
		
		mTv = (TextView)rootView.findViewById(R.id.fragment_game_round_str_prim);
		if(mActivePlayers < mPlayerCnt)
			mTv.setText(rootView.getResources().getString(R.string.str_game_points_choose_winner_and_suspend));
		else
			mTv.setText(rootView.getResources().getString(R.string.str_game_points_choose_winner));

		LinearLayout mLayout = null; // (LinearLayout)rootView.findViewById(R.id.game_add_round_bock_container);
		if (mLayout != null) {
			if(mBockRound == 0) mLayout.setVisibility(View.INVISIBLE);
			else mLayout.setVisibility(View.VISIBLE);
		}
		
		
		rootView.findViewById(R.id.game_edit_round_main_layout).requestFocus();
	}

    
    private static void loadUINewRoundPlayerSection(View rootView, LayoutInflater inflater) {
    	LinearLayout mLl;
    	TextView mTv;
    	int mTmp;
    	
    	mRoundPlayer.clear();
    	
		LinearLayout mLayout = (LinearLayout)rootView.findViewById(R.id.game_add_round_playersection);

        LinearLayout mPointGrid = (LinearLayout)rootView.findViewById(R.id.point_grid);
        GameActivity.setupGridPointButtonsToEditValues(mPointGrid, mEtNewRoundPoints);


		mTmp = (int) ((double)mPlayerCnt/2 + 0.5d);
		for(int i=0;i<(DokoData.MAX_PLAYER/2) && i<mTmp ;i++){
			mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_add_round_2_player_row, null);
			mLayout.addView(mLl);

            View mPlayerColor = mLl.findViewById(R.id.game_add_round_playercolor_left);
            mPlayerColor.setBackgroundColor(rootView.getContext().getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i*2]));

            mTv = (TextView)mLl.findViewById(R.id.game_add_round_playername_left);
			
			mTv.setText(mPlayerNames.get(i*2));
			mTv.setOnClickListener(mPlayernameClickListener);
			if(mPlayerCnt-mActivePlayers > 0) mTv.setOnLongClickListener(mPlayernameLongClickListener);
			mRoundPlayer.add(mTv);
			
			if(mPlayerStates.get(i*2) == PLAYER_ROUND_RESULT_STATE.WIN_STATE) {
				Log.d(TAG,"win state click");
				mTv.performClick();	
			} else if(mPlayerStates.get(i*2) == PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE) {
				Log.d(TAG,"suspend state long click");
				mTv.performLongClick();	
			}

            mPlayerColor = mLl.findViewById(R.id.game_add_round_playercolor_right);
            mPlayerColor.setBackgroundColor(rootView.getContext().getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i * 2 + 1]));
			mTv = (TextView)mLl.findViewById(R.id.game_add_round_playername_right);
			
			if(mPlayerCnt == 5 && i == 2){
				mLl.removeView(mTv);
			} else if(mPlayerCnt == 7 && i == 3){
				mLl.removeView(mTv);
			}
			else{
				mTv.setText(mPlayerNames.get(i*2+1));
				mTv.setOnClickListener(mPlayernameClickListener);
				if(mPlayerCnt-mActivePlayers > 0) mTv.setOnLongClickListener(mPlayernameLongClickListener);
				mRoundPlayer.add(mTv);
				if(mPlayerStates.get(i*2+1) == PLAYER_ROUND_RESULT_STATE.WIN_STATE) {
					Log.d(TAG,"win state click");
					mTv.performClick();	
				} else if(mPlayerStates.get(i*2+1) == PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE) {
					Log.d(TAG,"suspend state long click");
					mTv.performLongClick();	
				}
			}
		}
	}
    
	private boolean isNewRoundDataOK() {
		if(getNewRoundPoints() == -1) return false;
		if(!isWinnerCntOK() || !isSuspendCntOK() ) return false;
		return true;
	}
	
	private boolean isSuspendCntOK(){
		if(mPlayerCnt-mActivePlayers == 0) return true;
		if(getSuspendCnt() == (mPlayerCnt-mActivePlayers)) return true;
		return false;
	}
	
	private boolean isWinnerCntOK(){
		int mWinnerCnt = getWinnerCnt();
		if(mWinnerCnt >= mActivePlayers || mWinnerCnt == 0) return false;
		return true;
	}
		
	private int getSuspendCnt(){
		int m = 0;
		for(int i=0;i<mSuspendList.length;i++){
			if(mSuspendList[i] == 1) m++;
		}
		return m;
	}
	
	private int getWinnerCnt(){
		int m = 0;
		for(int i=0;i<mWinnerList.length;i++){
			if(mWinnerList[i] == 1) m++;
		}
		return m;
	}
	
	public class PlayernameClickListener implements OnClickListener{
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			if(mRoundPlayer.size() > mSuspendList.length)
				Log.e(TAG,"error Array"+mRoundPlayer.size()+"#"+mSuspendList.length);
			for(int i=0;i<mRoundPlayer.size();i++){
				if(v == mRoundPlayer.get(i) && mSuspendList[i]==0){
					if(mWinnerList[i] == 0 && getWinnerCnt() < mActivePlayers-1){
						((TextView) v).setBackgroundDrawable(v.getResources().getDrawable(R.drawable.select_green));
						mWinnerList[i] = 1;
					}
					else{
						mWinnerList[i] = 0;
						v.setBackgroundColor(v.getResources().getColor(R.color.white));
					}
				}
			}
			return;
		}
    }
	
	public class PlayernameLongClickListener implements OnLongClickListener{
		@SuppressWarnings("deprecation")
		@Override
		public boolean onLongClick(View v) {
			for(int i=0;i<mRoundPlayer.size();i++){
				if(v == mRoundPlayer.get(i) && mWinnerList[i]==0){
					if(mSuspendList[i] == 0 && getSuspendCnt() < mPlayerCnt-mActivePlayers){
						v.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.select_gray));
						mSuspendList[i] = 1;
					}
					else{
						mSuspendList[i] = 0;
						v.setBackgroundColor(v.getResources().getColor(R.color.white));
					}
				}
			}
			return true;	
		}
    }
	
	public class btnEditRoundClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_game_edit_round:
				if(!isNewRoundDataOK()){
					showAlertDialog(R.string.str_error, R.string.str_error_game_new_round_data);
					return;
				}
				
		    	Intent i = intent;
		    	i.putExtra(DokoData.CHANGE_ROUND_KEY, true);
		    	i.putExtra(DokoData.ROUND_POINTS_KEY, getNewRoundPoints());
		    	
				PLAYER_ROUND_RESULT_STATE mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.WIN_STATE;
				for(int k=0; k < mPlayerCnt; k++){
					if (mSuspendList[k] == 1) {
						mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE;
					} else if (mWinnerList[k] == 1) {
						mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.WIN_STATE;
					} else  {
						mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.LOSE_STATE;
					}
					i.putExtra(DokoData.PLAYERS_KEY[k]+"_STATE", mPlayerRoundState.ordinal());
				}
	
				setResult(RESULT_OK, i);
		    	finish();	
				break;
				
			default:
				finish();
				break;
			}
		}
	}




	private int getNewRoundPoints(){
		try{
			return Integer.valueOf(mEtNewRoundPoints.getText().toString());
		}
		catch(Exception e){
            Log.e(TAG,"ERROR:"+e.toString());
			return -1;
		}
	}

}