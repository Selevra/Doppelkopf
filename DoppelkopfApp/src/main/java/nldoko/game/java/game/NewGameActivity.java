package nldoko.game.java.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nldoko.game.java.DokoActivity;
import nldoko.game.R;
import nldoko.game.java.XML.DokoXMLClass;
import nldoko.game.java.data.DokoData;
import nldoko.game.java.data.DokoData.GAME_CNT_VARIANT;


public class NewGameActivity extends DokoActivity {


	private LinearLayout mLayout;
	private ImageView mIv;
	private TextView mTvPlayerCnt;
	private int mPlayerCnt = DokoData.MIN_PLAYER;
	private Spinner mSpActivePlayer;
	private Spinner mSpBockLimit;
	private Spinner mSpGameCntVariant;
	private Spinner mSpPunkteeingabe;
	private CheckBox mCbSuspendMark;
    private LinearLayout mGameSettingsEntry;
    private LinearLayout mGameSettingsList;
	
	private Button mBtnStart;
	
	private ArrayList<Integer> mActivePlayerArrayList = new ArrayList<Integer>();
	private ArrayList<Integer> mBockLimitArrayList = new ArrayList<Integer>();
	private ArrayAdapter<Integer> mSPActivePlayerArrayAdapter;
	private ArrayAdapter<Integer> mSPBockLimitArrayAdapter;
	private ArrayAdapter<String> mSPGameCntVaraintArrayAdapter;
	private ArrayAdapter<String> mSPPunkteeingabeArrayAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_newgame);
        setUI();


        setupDrawerAndToolbar(mContext.getResources().getString(R.string.str_new_game));


        overridePendingTransition(R.anim.right_out, R.anim.left_in);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void setUI() {
    	mLayout = (LinearLayout)findViewById(R.id.new_gamge_layout_content);


    	mLayout = (LinearLayout)findViewById(R.id.player_entry_add_btn); 
    	if(mLayout != null)mLayout.setOnClickListener(new addPlayerClickListener());
    	
    	mSpActivePlayer	 	= (Spinner)findViewById(R.id.sp_act_player_cnt);
    	mSpBockLimit 		= (Spinner)findViewById(R.id.sp_bock_cnt);
    	mSpGameCntVariant	= (Spinner)findViewById(R.id.sp_game_cnt_variant);
		mSpPunkteeingabe	= (Spinner)findViewById(R.id.sp_game_cnt_varian);

        mGameSettingsEntry = (LinearLayout)findViewById(R.id.new_game_settings_entry);
        mGameSettingsEntry.setOnClickListener(new showGameSettingsClickListener());

        mGameSettingsList = (LinearLayout)findViewById(R.id.new_game_settings_entry_list);
        mGameSettingsList.setVisibility(View.GONE);
    	
    	mTvPlayerCnt = (TextView)findViewById(R.id.player_add_player_cnt);


    	mBtnStart = (Button)findViewById(R.id.btn_start_new_game);
    	mBtnStart.setOnClickListener(new startBtnClickListener());
    	
    	setSpinnerValues();
    	setAutoCompleteNames();

        setPlayerColors();
        updatePlayerCnt();
	}
    
    private void setSpinnerValues(){
    	mActivePlayerArrayList.clear();
    	for(int k=DokoData.MIN_PLAYER; k <= mPlayerCnt && k <= DokoData.MAX_ACTIVE_PLAYER;k++) mActivePlayerArrayList.add(k);
    	mSPActivePlayerArrayAdapter = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item,R.id.spinner_text,mActivePlayerArrayList);
        mSPActivePlayerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
   	    mSpActivePlayer.setAdapter(mSPActivePlayerArrayAdapter);
		mSpActivePlayer.setSelection(0);

   	    int i = 0;
   		String[] mGameCntVariantArr  = new String[DokoData.GAME_CNT_VARAINT_ARRAY.length];
   		for (Integer[] entry : DokoData.GAME_CNT_VARAINT_ARRAY) {
   			if (entry != null && entry.length == 2) {
   				String cntName = getResources().getString(entry[0]);
   				mGameCntVariantArr[i] = cntName;
   			}
   			i++;
   		}

   		mSPGameCntVaraintArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,R.id.spinner_text,mGameCntVariantArr);
        mSPGameCntVaraintArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpGameCntVariant.setAdapter(mSPGameCntVaraintArrayAdapter);
		mSpGameCntVariant.setSelection(0);

    	mBockLimitArrayList.clear();
    	for(int k=0;k<=mPlayerCnt;k++) mBockLimitArrayList.add(k);
    	mSPBockLimitArrayAdapter = new ArrayAdapter<Integer>(getApplicationContext(), R.layout.spinner_item, R.id.spinner_text,mBockLimitArrayList);
        mSPBockLimitArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpBockLimit.setAdapter(mSPBockLimitArrayAdapter);
		mSpBockLimit.setSelection(0);

		String[] mPunkteingabeVariantArr = new String[]{"manual", "automatic"};
		mSPPunkteeingabeArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,R.id.spinner_text,mPunkteingabeVariantArr);
		mSPPunkteeingabeArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
		mSpPunkteeingabe.setAdapter(mSPPunkteeingabeArrayAdapter);
		mSpPunkteeingabe.setSelection(1); // TODO: change to 0 for playstore
    }
    
    private void setAutoCompleteNames(){

    	mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
    	loadPlayerNames();


        AutoCompleteTextView last = null;

    	for(int i=0;i<mLayout.getChildCount();i++){
    	    View childView = mLayout.getChildAt(i);
    	    if (childView.getId() == R.id.player_entry){
    	    	View pV = childView.findViewById(R.id.player_entry_auto_complete);

                if (pV instanceof AutoCompleteTextView) {
                    AutoCompleteTextView acView = (AutoCompleteTextView)pV;
                    ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_text,DokoData.PLAYER_NAMES);
                    acView.setAdapter(adapter);
                    acView.setOnTouchListener(new View.OnTouchListener(){
                        @Override
                        public boolean onTouch(View v, MotionEvent event){
                            ((AutoCompleteTextView) v).showDropDown();    return false;
                        }
                    });
                    acView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    if (last != null) {
                        acView.setNextFocusDownId(last.getId());
                    }

                    last = acView;
                }

    	    }  
    	}
    }
    
    private void loadPlayerNames() {
		if(!DokoXMLClass.isAppDirOK(mContext)) {
			return;
		}
		DokoXMLClass.isXMLPresent(mContext,DokoData.PLAYER_NAMES_XML,true);

		DokoXMLClass.getPlayerNamesFromXML(mContext,DokoData.PLAYER_NAMES_XML,DokoData.PLAYER_NAMES);
		
	}

    private void setPlayerColors() {
        LinearLayout mLayout = (LinearLayout)findViewById(R.id.player_view_holder);

        int pCount = 0;
        for (int i = 0; i < mLayout.getChildCount(); ++i) {
            View v = mLayout.getChildAt(i);
            if (v.getId() == R.id.player_entry) {
                View mPlayercolorView = v.findViewById(R.id.player_color);
                if (mPlayercolorView != null) {
                    mPlayercolorView.setBackgroundColor(this.getResources().getColor(DokoData.PLAYERS_COLORS_KEY[pCount]));
                    pCount++;
                }

            }
        }
    }

	private void updatePlayerCnt(){
    	View v;
    	mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
    	mPlayerCnt = 0;
    	for(int i=0;i<mLayout.getChildCount();i++){
    	    v = mLayout.getChildAt(i);
    	    if (v.getId() == R.id.player_entry) mPlayerCnt++;
    	}

        mTvPlayerCnt.setText(String.valueOf(mPlayerCnt));

    	setSpinnerValues();
    }

	private class startBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!isPlayerNameSet(true)){
                showAlertDialog(R.string.str_error, R.string.str_error_player_name);
				return;
			}
			ArrayList<String> mPlayerNames = getPlayerNames();
			Intent i = new Intent(mContext,GameActivity.class);
			for(int k=0;k<mPlayerCnt && k<mPlayerNames.size();k++){
				i.putExtra(DokoData.PLAYERS_KEY[k], mPlayerNames.get(k).toString());
			}
			i.putExtra(DokoData.PLAYER_CNT_KEY, mPlayerCnt);
			i.putExtra(DokoData.BOCKLIMIT_KEY, mSpBockLimit.getSelectedItemPosition());
			i.putExtra(DokoData.ACTIVE_PLAYER_KEY, mSpActivePlayer.getSelectedItemPosition()+4);
			i.putExtra(DokoData.GAME_CNT_VARIANT_KEY, GAME_CNT_VARIANT.values()[mSpGameCntVariant.getSelectedItemPosition()]);
			i.putExtra(DokoData.PUNKTEEINGABE_KEY, DokoData.POINTS_CALCULATION.values()[mSpPunkteeingabe.getSelectedItemPosition()]);

			startActivity(i);
		}
	}
	

	private boolean isPlayerNameSet(boolean saveToXML) {
		ArrayList<String> mPlayerNames = getPlayerNames();
		//Log.d(TAG,mPlayerNames.size()+"+"+ mPlayerCnt);
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
		mLayout = (LinearLayout)findViewById(R.id.player_view_holder);

    	for(int i=0;i<mLayout.getChildCount();i++){
    	    v = mLayout.getChildAt(i);
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
			mLayout = (LinearLayout)findViewById(R.id.player_view_holder);
			LinearLayout mNewPlayerView = (LinearLayout)mInflater.inflate(R.layout.player_entry, null);
			
			mIv = (ImageView)mNewPlayerView.findViewById(R.id.player_entry_remove);
			mIv.setOnClickListener(new removePlayerClickListener());
			mIv.setVisibility(View.VISIBLE);
			mIv.setId(mPlayerCnt+1);
			mLayout.addView(mNewPlayerView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mNewPlayerView.getLayoutParams();

            float marginTop = v.getResources().getDimension(R.dimen.item_space);
            layoutParams.setMargins(0, Math.round(marginTop),0,0);
            mNewPlayerView.setLayoutParams(layoutParams);
			
			updatePlayerCnt();

			setAutoCompleteNames();
            setPlayerColors();
		}
    	
    }

    private class showGameSettingsClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            if (mGameSettingsList.getVisibility() == View.VISIBLE) {
                mGameSettingsList.setVisibility(View.GONE);
            } else {
                mGameSettingsList.setVisibility(View.VISIBLE);
            }

        }

    }
    
    private class removePlayerClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			mLayout = (LinearLayout)findViewById(R.id.player_view_holder);	
			mLayout.removeView((View) v.getParent());
			updatePlayerCnt();
            setPlayerColors();
		}
    	
    }

    private void showHelp() {
        showAlertDialog(getResources().getString(R.string.str_help), createHelpText());
	}

	private String createHelpText() {
		String text = "";
		text += getResources().getString(R.string.str_active_player);
		text += ":\n\n";
		text += getResources().getString(R.string.str_info_active_players_info);

		text += "\n\n";
		text += getResources().getString(R.string.str_bock_limit);
		text += ":\n\n";
		text += getResources().getString(R.string.str_info_bock_cnt_info);

		text += "\n\n";
		text += getResources().getString(R.string.str_info_cnt_cnt_variants);
		text += "\n\n";
		text += getResources().getString(R.string.str_info_cnt_cnt_variant_standard);
		text += "\n\n";
		text += getResources().getString(R.string.str_info_cnt_cnt_variant_lose);
		text += "\n\n";
		text += getResources().getString(R.string.str_info_cnt_cnt_variant_win);

		return text;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_new_game_start_help:
				showHelp();
				return true;
			case R.id.action_new_game_start_clear_playernames:
				boolean success = DokoXMLClass.clearPlayerNamesXML(mContext);
				showAlertDialog(R.string.str_hint, R.string.str_clear_player_name_list_text);
				setAutoCompleteNames();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game_start, menu);
		return true;
	}





}
