package nldoko.game.java.game;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import nldoko.game.java.DokoActivity;
import nldoko.game.R;
import nldoko.game.java.XML.DokoXMLClass;
import nldoko.game.java.data.GameClass;
import nldoko.game.java.data.DokoData;
import nldoko.game.java.data.DokoData.GAME_CNT_VARIANT;

import java.util.ArrayList;

public class ChangeGameSettingActivity extends DokoActivity {
	private String TAG = "NewGame";

	private int mPlayerCnt = DokoData.MIN_PLAYER;
	private Spinner mSpActivePlayer;
	private Spinner mSpBockLimit;
    private Spinner mSpGameCntVariant;
    private TextView mTvPlayerCnt;

	private Button mBtnChangeGameSettings;
    private LinearLayout mAddPlayerButton;
    private int oldmAddPlayerButtonTextcolor;
	
	private ArrayList<Integer> mActivePlayerArrayList = new ArrayList<Integer>();
	private ArrayList<Integer> mBockLimitArrayList = new ArrayList<Integer>();
	private ArrayAdapter<Integer> mSPActivePlayerArrayAdapter;
	private ArrayAdapter<Integer> mSPBockLimitArrayAdapter;
	private bockLimitChangeSpListener mSpBockLimitChangeListener;
	
	private LinearLayout mPlayerHolder;
	private ArrayList<View> mPlayerViewList = new ArrayList<View>();
	
	private GameClass mGameHolder =  null;
	
	private boolean mGameSettingsChanged = false;
    private boolean mDeleteBockRounds = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game_settings);

		setupDrawerAndToolbar(mContext.getResources().getString(R.string.str_game_settings));
        setBackArrowInToolbar();

        mGameHolder =  loadGameState();

        if(mGameHolder == null){
			showAlertDialog(R.string.str_error, R.string.str_error_load_game_for_change_game_settings);
        	finish();
        }
        
        mPlayerCnt = mGameHolder.getPlayerCount();
        setUI();
        
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }


    private GameClass loadGameState() {
    	GameClass mGame = null;
    	Intent intent = getIntent();
    	Bundle extras = intent.getExtras();
    	int mActivePlayers,mBockLimit,mPlayerCnt;
    	GAME_CNT_VARIANT mGameCntVaraint;
		DokoData.POINTS_CALCULATION mPunkteeingabe;
    	String mTmp = "";


    	if(extras != null){
        	mPlayerCnt = extras.getInt(DokoData.PLAYER_CNT_KEY,0);
        	mActivePlayers =  extras.getInt(DokoData.ACTIVE_PLAYER_KEY,0);
        	mBockLimit = extras.getInt(DokoData.BOCKLIMIT_KEY,0);

        	mGameCntVaraint = (GAME_CNT_VARIANT)intent.getSerializableExtra(DokoData.GAME_CNT_VARIANT_KEY);
			mPunkteeingabe = (DokoData.POINTS_CALCULATION) intent.getSerializableExtra(DokoData.PUNKTEEINGABE_KEY);
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER || 
        			(mPlayerCnt == 0 || mActivePlayers == 0))
        		return null;
        	
        	mGame = new GameClass(mPlayerCnt, mActivePlayers, mBockLimit, mGameCntVaraint, mPunkteeingabe);
        	for(int k=0;k<mPlayerCnt;k++){
        		mTmp = extras.getString(DokoData.PLAYERS_KEY[k],"");
        		if(mTmp == null || mTmp.length() == 0) return null;
        		mGame.getPlayer(k).setName(mTmp);
        	}
        }
		return mGame;
	}


	private void setUI() {
    	mPlayerHolder = (LinearLayout)findViewById(R.id.player_view_holder);
        LinearLayout v;
    	for(int i=0;i<mGameHolder.getPlayerCount();i++){
    		v = (LinearLayout)mInflater.inflate(R.layout.player_entry, null);

    		mPlayerHolder.addView(v);
    		mPlayerViewList.add(v);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();

            float marginTop = mContext.getResources().getDimension(R.dimen.item_space);
            layoutParams.setMargins(0, Math.round(marginTop),0,0);
            v.setLayoutParams(layoutParams);
    	}

        mAddPlayerButton = (LinearLayout)findViewById(R.id.player_entry_add_btn);
    	if(mAddPlayerButton != null) {
            mAddPlayerButton.setOnClickListener(new addPlayerClickListener());
        }

        mSpGameCntVariant	= (Spinner)findViewById(R.id.sp_game_cnt_variant);
        if (mSpGameCntVariant != null) {
            mSpGameCntVariant.setEnabled(NO);
            mSpGameCntVariant.setClickable(NO);

            int i = 0;
            String[] mGameCntVariantArr  = new String[DokoData.GAME_CNT_VARAINT_ARRAY.length];
            for (Integer[] entry : DokoData.GAME_CNT_VARAINT_ARRAY) {
                if (entry != null && entry.length == 2) {
                    String cntName = getResources().getString(entry[0]);
                    mGameCntVariantArr[i] = cntName;
                }
                i++;
            }
            ArrayAdapter<String> mSPGameCntVaraintArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_text,mGameCntVariantArr);
            mSpGameCntVariant.setAdapter(mSPGameCntVaraintArrayAdapter);
            mSpGameCntVariant.setSelection(0);
        }

    	mSpActivePlayer = (Spinner)findViewById(R.id.sp_act_player_cnt);
    	mSpBockLimit = (Spinner)findViewById(R.id.sp_bock_cnt);


        mSpBockLimitChangeListener = new bockLimitChangeSpListener();
        mSpBockLimit.setOnItemSelectedListener(mSpBockLimitChangeListener);

    	mBtnChangeGameSettings = (Button)findViewById(R.id.change_game_settings_apply_button);
    	mBtnChangeGameSettings.setOnClickListener(new changeGameSettingsBtnClickListener());

        mTvPlayerCnt = (TextView)findViewById(R.id.player_add_player_cnt);
		mTvPlayerCnt.setText(Integer.toString(mGameHolder.getPlayerCount()));

    	setSpinnerValues();
    	setAutoCompleteNames();
	}
    
    private void setSpinnerValues(){
    	mActivePlayerArrayList.clear();

		int selection = 0;
    	for(int k=DokoData.MIN_PLAYER; k <= mPlayerCnt && k <= DokoData.MAX_ACTIVE_PLAYER;k++) {
			mActivePlayerArrayList.add(new Integer(k));

			if (k == mGameHolder.getActivePlayerCount()) {
				selection = k - DokoData.MIN_PLAYER;
			}
		}

    	mSPActivePlayerArrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item,R.id.spinner_text,mActivePlayerArrayList);
		mSPActivePlayerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
		mSpActivePlayer.setAdapter(mSPActivePlayerArrayAdapter);

		mSpActivePlayer.setSelection(selection);

    	mBockLimitArrayList.clear();
    	//disable temp 
    	//TODO
    	mSpBockLimit.setEnabled(false);
    	mSpBockLimit.setClickable(false);

		for(int k=0;k<=mPlayerCnt;k++) {
			mBockLimitArrayList.add(new Integer(k));
		}
    	mSPBockLimitArrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item,R.id.spinner_text,mBockLimitArrayList);
		mSPBockLimitArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
		mSpBockLimit.setAdapter(mSPBockLimitArrayAdapter);

		mSpBockLimit.setSelection(mGameHolder.getBockRoundLimit());
    }
    
    private void setAutoCompleteNames(){
    	View v;
    	ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,DokoData.PLAYER_NAMES);
    	LinearLayout mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
    	loadPlayerNames();
    	for(int i=0;i<DokoData.MAX_PLAYER && i<mPlayerViewList.size();i++){
    	    v = mPlayerViewList.get(i);
    	    if (v.getId() == R.id.player_entry){
                //color
                View mPlayercolorView = v.findViewById(R.id.player_color);
                if (mPlayercolorView != null) {
                    mPlayercolorView.setBackgroundColor(this.getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i]));
                }

    	    	v = v.findViewById(R.id.player_entry_auto_complete);
    	    	if(i < mGameHolder.getPlayerCount()){
    	    		((AutoCompleteTextView) v).setText(mGameHolder.getPlayer(i).getName());
    	    		((AutoCompleteTextView) v).setSelection(0); 
    	    		((AutoCompleteTextView) v).setAdapter((ArrayAdapter<String>)null);
    	    		((AutoCompleteTextView) v).setEnabled(false);
    	    	}
    	    	else{
	    	    	((AutoCompleteTextView) v).setAdapter(adapter);
	    	    	((AutoCompleteTextView) v).setOnTouchListener(new View.OnTouchListener(){
	    	    		   @Override
	    	    		   public boolean onTouch(View v, MotionEvent event){
	    	    			   ((AutoCompleteTextView) v).showDropDown();    return false;
	    	    		   }
	    	    		});
    	    	}
    	    }  
    	}
    }
    
    private void loadPlayerNames() {
		if(!DokoXMLClass.isAppDirOK(mContext)) return;
		DokoXMLClass.isXMLPresent(mContext,DokoData.PLAYER_NAMES_XML,true);
		DokoXMLClass.getPlayerNamesFromXML(mContext,DokoData.PLAYER_NAMES_XML,DokoData.PLAYER_NAMES);
		
	}


	private void updatePlayerCnt(){
    	View v;
    	LinearLayout mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
    	mPlayerCnt = 0;
    	mPlayerViewList.clear();
    	for(int i=0;i<mLayout.getChildCount();i++){
    	    v = mLayout.getChildAt(i);
    	    if (v.getId() == R.id.player_entry){
    	    	mPlayerCnt++;
    	    	mPlayerViewList.add(v);
    	    }
    	}

        mTvPlayerCnt.setText(String.valueOf(mPlayerCnt));
    	
    	setSpinnerValues();
    }
	
	private class bockLimitChangeSpListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View v,	int pos, long id) {
			if(pos < mGameHolder.getBockRoundLimit()) showBockDeleteDialog();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	}
	
	private class changeGameSettingsBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!isPlayerNameSet(true)){
				showAlertDialog(R.string.str_error,  R.string.str_error_player_name);
				return;
			}
			sendGameSettingsAndExit();
		}
	}
	

	private boolean isPlayerNameSet(boolean saveToXML) {
		ArrayList<String> mPlayerNames = getPlayerNames();
		Log.d(TAG,mPlayerNames.size()+"+"+ mPlayerCnt);
		if(mPlayerNames.size() < mPlayerCnt) return false;
		
		for(int i=0;i<mPlayerNames.size();i++){
			if(mPlayerNames.get(i).isEmpty()) return false;
		}
		
    	for(int i=0;i<DokoData.PLAYER_NAMES.size();i++){ 
    		if(!mPlayerNames.contains(DokoData.PLAYER_NAMES.get(i)) )
    			mPlayerNames.add(DokoData.PLAYER_NAMES.get(i));
    	}
		DokoData.PLAYER_NAMES = mPlayerNames;
		DokoXMLClass.savePlayerNamesToXML(mContext,mPlayerNames);
		return true;
	}
	
	private ArrayList<String> getPlayerNames(){
		ArrayList<String> mPlayerNames = new ArrayList<String>();
		View v;
		AutoCompleteTextView ac;

    	for(int i=0;i<mPlayerViewList.size();i++){
    	    v = mPlayerViewList.get(i);
    	    if (v.getId() == R.id.player_entry){
    	    	ac = (AutoCompleteTextView)v.findViewById(R.id.player_entry_auto_complete);
    	    	if(!mPlayerNames.contains(ac.getText().toString().trim())){
    	    		//Log.d(TAG,ac.getText().toString());
    	    		mPlayerNames.add(ac.getText().toString().trim());
    	    	}
    	    }
    	}

    	return mPlayerNames;
	}
    
    private class addPlayerClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(mPlayerCnt == DokoData.MAX_PLAYER) {
                return;
            }

            if (mPlayerCnt == DokoData.MAX_PLAYER - 1) {
                // disbale button if last player which can be added
                mAddPlayerButton.setEnabled(NO);
                TextView mTv = (TextView)mAddPlayerButton.findViewById(R.id.fragment_game_round_number);
                if (mTv!= null) {
                    oldmAddPlayerButtonTextcolor = mTv.getCurrentTextColor();
                    mTv.setTextColor(getResources().getColor(R.color.light_gray));
                }
            }

			LinearLayout layout = (LinearLayout)findViewById(R.id.player_view_holder);
			v = mInflater.inflate(R.layout.player_entry, null);
			
			ImageView mIv = (ImageView)v.findViewById(R.id.player_entry_remove);
			mIv.setOnClickListener(new removePlayerClickListener());
			mIv.setVisibility(View.VISIBLE);
			mIv.setId(mPlayerCnt+1);
            layout.addView(v);
			mPlayerViewList.add(v);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();

            float marginTop = mContext.getResources().getDimension(R.dimen.item_space);
            layoutParams.setMargins(0, Math.round(marginTop),0,0);
            v.setLayoutParams(layoutParams);
			
			updatePlayerCnt();
			
			setAutoCompleteNames();
		}
    	
    }
    
    private class removePlayerClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			LinearLayout mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
            mLayout.removeView((View) v.getParent());
			
			for(int i=0;i<mPlayerViewList.size();i++){
				Log.d(TAG,mLayout.getId()+"+"+mPlayerViewList.get(i).getId());
			}
			updatePlayerCnt();
            mAddPlayerButton.setEnabled(YES);
            TextView mTv = (TextView)mAddPlayerButton.findViewById(R.id.fragment_game_round_number);
            if (mTv!= null) {
                mTv.setTextColor(oldmAddPlayerButtonTextcolor);;
            }

		}
    	
    }
    
	private void showExitDialog(){
        DialogInterface.OnClickListener okListerner = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sendGameSettingsAndExit();
            }
        };

        DialogInterface.OnClickListener abortListerner = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        };

        showAlertDialog(R.string.str_change_game_settings_save, R.string.str_change_game_settings_save_q,
                R.string.str_yes, okListerner,
                R.string.str_no, abortListerner);
	}
	
	private void showBockDeleteDialog(){
		DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mDeleteBockRounds = true;
				sendGameSettingsAndExit();
			}
		};

        DialogInterface.OnClickListener abortListerner = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mSpBockLimit.setSelection(mGameHolder.getBockRoundLimit());
			}
		};

		showAlertDialog(R.string.str_change_game_settings_delete_bockrounds,
				R.string.str_change_game_settings_delete_bockrounds_q,
				R.string.str_accept, okListener,
				R.string.str_abort, abortListerner);

	}
  
    
    protected void sendGameSettingsAndExit() {
    	Intent i = getIntent();
    	
    	i.putExtra(DokoData.CHANGE_GAME_SETTINGS_KEY, true);
    	
		i.putExtra(DokoData.PLAYER_CNT_KEY, mPlayerCnt);
		i.putExtra(DokoData.BOCKLIMIT_KEY, mSpBockLimit.getSelectedItemPosition());
		i.putExtra(DokoData.ACTIVE_PLAYER_KEY, mSpActivePlayer.getSelectedItemPosition()+4);
		
		ArrayList<String> mPlayerNames = getPlayerNames();
		for(int k=0;k<mPlayerCnt && k<mPlayerNames.size();k++){
			i.putExtra(DokoData.PLAYERS_KEY[k], mPlayerNames.get(k).toString());
		}

		setResult(RESULT_OK, i);
    	finish();	
	}
    


    @Override
    public void onBackPressed(){
    	if(mGameSettingsChanged) {
			showExitDialog();
		}
    	else {
			finish();
		}
    	return;
    }

	@Override
  	public boolean onOptionsItemSelected(MenuItem item){
    	// same as using a normal menu
    	switch(item.getItemId()) {
    	case android.R.id.home:
        	if(mGameSettingsChanged) {
				showExitDialog();
			}
        	else {
				finish();
			}
    	}
    	return true;
    }
    
}
