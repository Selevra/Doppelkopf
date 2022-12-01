package nldoko.game.java.game;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nldoko.game.java.DokoActivity;
import nldoko.game.R;
import nldoko.game.java.XML.DokoXMLClass;
import nldoko.game.java.data.DokoData;
import nldoko.game.java.data.DokoData.GAME_CNT_VARIANT;
import nldoko.game.java.data.DokoData.GAME_VIEW_TYPE;
import nldoko.game.java.data.DokoData.PLAYER_ROUND_RESULT_STATE;
import nldoko.game.java.data.GameClass;
import nldoko.game.java.util.Functions;

public class GameActivity extends DokoActivity {

	final private String TAG = "Game";

    private static DokoActivity dokoActivity;
		
	private static ListView mLvRounds;
	private static GameMainListAdapter mLvRoundAdapter;

	private static LinearLayout mLayout;
	private static LinearLayout mGameRoundsInfoSwipe;
	private static LinearLayout mBottomInfos;
	private static TextView mBottomInfoBockRoundCount;
	private static TextView mBottomInfoBockRoundPreview;

	private static TextView mTvAddRoundBockPoints;


	private static Spinner mGameBockRoundsCnt;
	private static Spinner mGameBockRoundsGameCnt;
	private static ArrayAdapter<Integer> mGameBockRoundsCntAdapter;
	private static ArrayAdapter<Integer> mGameBockRoundsGameCntAdapter;

	private static Button mBtnAddRound;
	private static TextView mEtNewRoundPoints;

	private static ArrayList<TextView> mGameAddRoundPlayerState = new ArrayList<TextView>();
	
	private SwipePagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
	private static final int mIndexGameMain 		= 0;
	private static final int mIndexGameNewRound 	= 1;
	protected static GameClass mGame;
    private static PLAYER_ROUND_RESULT_STATE mNewRoundPlayerWinState[] = new PLAYER_ROUND_RESULT_STATE[DokoData.MAX_PLAYER];
	private static USER_SELECTED_PLAYER_STATE mUserSelectedPlayerState[] = new USER_SELECTED_PLAYER_STATE[DokoData.MAX_PLAYER];


    private static CheckBox mCBNewBockRound;
	private static ImageView mBockRoundInfoIcon;
	private static LinearLayout mGameBockDetailContainer;

	// detailed info of round: general items
//	private static CheckBox mCBDetailedRoundInfo;
	private static LinearLayout mDetailedRoundInfo;
//	private static ImageView mDetailedRoundInfoIcon;
//	private static LinearLayout mDetailedRoundInfoContainer;
	private static LinearLayout mDetailedRoundInfoTypeContainer;
	private static LinearLayout mDetailedRoundInfoAnsagenContainer;
	private static LinearLayout mDetailedRoundInfoSpecialContainer;
	private static ImageView mDetailedRoundInfoTypeArrow;
	private static ImageView mDetailedRoundInfoAnsagenArrow;
	private static ImageView mDetailedRoundInfoSpecialArrow;
	// ... round result buttons
	private static RadioGroup mRadioGroup;
	private static Map<GAME_PARTY, RadioButton> mMapRBWinner = new HashMap<GAME_PARTY, RadioButton>();
	private static ToggleButton mTBNo120;
	private static ToggleButton mTBNo90;
	private static ToggleButton mTBNo60;
	private static ToggleButton mTBNo30;
	private static ToggleButton mTBNo0;
	private static ArrayList<ToggleButton> mListGameResultTBs = new ArrayList<ToggleButton>();
	// round type buttons
	private static Map<ROUND_TYPES, ToggleButton> mMapTbRoundType = new HashMap<ROUND_TYPES, ToggleButton>();
	// ... "Ansagen" Re
	private static ToggleButton mTBReRe;
	private static ToggleButton mTBRe90;
	private static ToggleButton mTBRe60;
	private static ToggleButton mTBRe30;
	private static ToggleButton mTBRe0;
    private static ArrayList<ToggleButton> mListReAnsagenTBs = new ArrayList<ToggleButton>();
	// ... "Ansagen" Kontra
	private static ToggleButton mTBKontraKontra;
	private static ToggleButton mTBKontra90;
	private static ToggleButton mTBKontra60;
	private static ToggleButton mTBKontra30;
	private static ToggleButton mTBKontra0;
    private static ArrayList<ToggleButton> mListKontraAnsagenTBs = new ArrayList<ToggleButton>();
	// "Sonderpunkte" Re
	private enum SpecialPoints
	{
		DOPPELKOPF, FUCHS, KARLCHEN, HEART, GEGEN
	}
	private static ToggleButton mTBReDK;
	private static SeekBar mSBReDK;
	private static ToggleButton mTBReFuchs;
	private static SeekBar mSBReFuchs;
	private static ToggleButton mTBReKarlchen;
	private static ToggleButton mTBReHeart;
	// "Sonderpunkte" Kontra
	private static ToggleButton mTBKontraDK;
	private static SeekBar mSBKontraDK;
	private static ToggleButton mTBKontraFuchs;
	private static SeekBar mSBKontraFuchs;
	private static ToggleButton mTBKontraKarlchen;
	private static ToggleButton mTBKontraHeart;
	private static ToggleButton mTBKontraGegen; // gegen die Alten
	// Projected points
	private static TextView mTVProjectedPoints;
	private static TextView mTVProjectedPointsWinner;
	// Arrays for Toggle Buttons of Re
	private static final Map<String, ToggleButton> mMapTBsRe = new HashMap<String, ToggleButton>();
	private static final Map<String, ToggleButton> mMapTBsKontra = new HashMap<String, ToggleButton>();
	private static final ArrayList<String> mReSpecialXML = new ArrayList<String>();
	private static final ArrayList<String> mKontraSpecialXML = new ArrayList<String>();

    private static GameAddRoundPlayernameClickListener mAddRoundPlayernameClickListener;
    private static GameAddRoundPlayernameLongClickListener mAddRoundPlayernameLongClickListener;
    private static btnAddRoundClickListener mBtnAddRoundClickListener;

    private ProgressDialog progressDialog;

	private static Drawable suspendDraw;
	private static Drawable winnerDraw;
	private static Drawable loserDraw;

	public enum GAME_PARTY {
		PARTY_RE,
		PARTY_KONTRA,
	}

	// values must be aligned (same order) with "PLAYER_ROUND_RESULT_STATE"
	public enum USER_SELECTED_PLAYER_STATE {
		WIN_OR_RE_STATE,
		LOSE_OR_KONTRA_STATE,
		SUSPEND_STATE,
	}

	// the team that actual scored the positive points (or a tie)
	// values must be aligned (same order) with "PLAYER_ROUND_RESULT_STATE"
	public enum ROUND_RESULT_POINTS_WINNER {
		RE,
		KONTRA,
		TIE,
	}

//	public enum ROUND_RESULT {
//		BOTH_120,
//		NO_120,
//		NO_90,
//		NO_60,
//		NO_30,
//		SCHWARZ
//	}
//
//	public enum ROUND_ANSAGEN {
//		RE_KONTRA,
//		NO_90,
//		NO_60,
//		NO_30,
//		SCHWARZ
//	}

	public enum ROUND_TYPES {
		NORMAL, HOCHZEIT, ARMUT, OBERSOLO, UNTERSOLO, FLEISCHLOSER, FARBSOLO, STILLE_HOCHZEIT,
	}

	// TODO: do we need separate maps?
	private static final Map<ROUND_TYPES, Integer> normalRoundTypeButtons = new HashMap<ROUND_TYPES, Integer>() {{
		put(ROUND_TYPES.NORMAL, R.id.btn_normal);
		put(ROUND_TYPES.HOCHZEIT, R.id.btn_hochzeit);
		put(ROUND_TYPES.ARMUT, R.id.btn_armut);
	}};

	private static final Map<ROUND_TYPES, Integer> soloRoundTypeButtons = new HashMap<ROUND_TYPES, Integer>() {{
		put(ROUND_TYPES.OBERSOLO, R.id.btn_obersolo);
		put(ROUND_TYPES.UNTERSOLO, R.id.btn_untersolo);
		put(ROUND_TYPES.FLEISCHLOSER, R.id.btn_fleischloser);
		put(ROUND_TYPES.FARBSOLO, R.id.btn_farbsolo);
		put(ROUND_TYPES.STILLE_HOCHZEIT, R.id.btn_stille_hochzeit);
	}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_game);

        dokoActivity = this;

        setupDrawerAndToolbar(mContext.getResources().getString(R.string.str_game));

        mGame = getGame(savedInstanceState);

        if(mGame == null){
        	Toast.makeText(this, getResources().getString(R.string.str_error_game_start), Toast.LENGTH_LONG).show();
        	finish();
        }

        loadDefaultPlayerStates(mNewRoundPlayerWinState, mUserSelectedPlayerState);

        loadSwipeViews();

		View v = findViewById(R.id.my_toolbar_shadow);
		if (v != null) {
			v.setVisibility(View.GONE);
		}

        mAddRoundPlayernameClickListener = new GameAddRoundPlayernameClickListener();
        mAddRoundPlayernameLongClickListener = new GameAddRoundPlayernameLongClickListener();
        mBtnAddRoundClickListener = new btnAddRoundClickListener();

        //findViewById(R.id.game_main_layout).requestFocus();

        overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }

    private static void loadDefaultPlayerStates(PLAYER_ROUND_RESULT_STATE[] winStates, USER_SELECTED_PLAYER_STATE[] userSelectedState) {
		//default
		for (int i = 0; i < winStates.length; i++) {
            winStates[i] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE;
			userSelectedState[i] = USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE;
		}
	}

    private void loadSwipeViews(){
    	  mDemoCollectionPagerAdapter =  new SwipePagerAdapter(getSupportFragmentManager());
          mViewPager = (ViewPager) findViewById(R.id.game_pager);
          mViewPager.setAdapter(mDemoCollectionPagerAdapter);
          mViewPager.setOnPageChangeListener(new GamePageChangeListener());
    }

    
    private void reloadSwipeViews(){
    	loadSwipeViews();
    }
    
    
    private class GamePageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
			String mStr = "";
            switch (position) {
			case mIndexGameMain:
				changeToolbarTitle(getResources().getString(R.string.str_game));
				return;
			case mIndexGameNewRound:
				changeToolbarTitle(getResources().getString(R.string.str_game_new_round));
	  			if(mGame != null && mGame.getPreRoundList().size() > 0 && mGame.getPreRoundList().get(0).getBockCount() > 0){
	  				mStr = getResources().getString(R.string.str_bockround)+" ";
	  				mStr += Functions.getBockCountAsString(mGame.getPreRoundList().get(0).getBockCount());
	  				mTvAddRoundBockPoints.setText(mStr);
	  			}
	  			else {
                    mTvAddRoundBockPoints.setText(mStr);
                }
	  			
	  			return;
			default:
				changeToolbarTitle(getResources().getString(R.string.str_game));
				break;
			}
        }
    }
    
    private GameClass getGame(Bundle savedInstanceState){
    	GameClass mGame;
    	Intent intent = getIntent();
    	Bundle extras = intent.getExtras();
    	int mActivePlayers,mBockLimit,mPlayerCnt;
    	GAME_CNT_VARIANT mGameCntVaraint;
		DokoData.POINTS_CALCULATION mPunkteeingabe;
    	String mTmp = "";
        boolean mAutoBockCalc = true;
    	
    	//Log.d(TAG,"getgame");
        //Check if a game exists else try to create one 
        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
        	mGame =  loadStateData(savedInstanceState);
        }
        else if(extras != null && extras.getBoolean("RestoreGameFromXML", false)){
            progressDialog = new ProgressDialog(GameActivity.this);
            progressDialog.setTitle(this.getResources().getString(R.string.str_plz_wait));
            progressDialog.setMessage(this.getResources().getString(R.string.str_game_load));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

        	String file = extras.getString("filename");
        	Log.d(TAG,"Game from XML file:"+file);
        	mGame =  DokoXMLClass.restoreGameStateFromXML(this,file, true);
        	if (mGame != null) {
        		// if success delete old file
                DokoXMLClass.saveGameStateToXML(mContext, mGame);
        	}

            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) { }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                }
            }.start();

        }
        else if(extras != null){
        	mPlayerCnt 		= extras.getInt(DokoData.PLAYER_CNT_KEY,0);
        	mActivePlayers 	= extras.getInt(DokoData.ACTIVE_PLAYER_KEY,0);
        	mBockLimit		= extras.getInt(DokoData.BOCKLIMIT_KEY,0);
        	mGameCntVaraint = (GAME_CNT_VARIANT)intent.getSerializableExtra(DokoData.GAME_CNT_VARIANT_KEY);
        	mPunkteeingabe	= (DokoData.POINTS_CALCULATION)intent.getSerializableExtra(DokoData.PUNKTEEINGABE_KEY);
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt
        			|| (mPlayerCnt == 0 || mActivePlayers == 0))
        		return null;

        	mGame = new GameClass(mPlayerCnt, mActivePlayers, mBockLimit, mGameCntVaraint, mPunkteeingabe);

        	for(int k=0;k<mPlayerCnt;k++){
        		//Log.d(TAG,mTmp+"k:"+k);
        		mTmp = extras.getString(DokoData.PLAYERS_KEY[k],"");
        		if(mTmp == null || mTmp.length() == 0) return null;
        		//Log.d(TAG,mTmp);
        		mGame.getPlayer(k).setName(mTmp);
        	}
        }
        else{
        	mGame = new GameClass(5, 4, 1, GAME_CNT_VARIANT.CNT_VARIANT_NORMAL, DokoData.POINTS_CALCULATION.POINTS_CALCULATION_MANUAL);
	    	
        	mGame.getPlayer(0).setName("Johannes");
        	mGame.getPlayer(1).setName("Christoph");
        	mGame.getPlayer(2).setName("P3");
	    	mGame.getPlayer(3).setName("P4");
	    	mGame.getPlayer(4).setName("P5");
	    	mGame.getPlayer(5).setName("P6");
	    	mGame.getPlayer(6).setName("P7");
	    	mGame.getPlayer(7).setName("P8");
        }
        return mGame;
    }
    

    
    
	 // Since this is an object collection, use a FragmentStatePagerAdapter,
	 // and NOT a FragmentPagerAdapter.
	 public class SwipePagerAdapter extends FragmentPagerAdapter {
	    private static final int mIndexCnt	= 2;

	    
	    public SwipePagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	
	    @Override
	    public Fragment getItem(int i) {
            DemoObjectFragment fragment = new DemoObjectFragment();
	        Bundle args = new Bundle();
	        // Our object is just an integer :-P
	        args.putInt(DemoObjectFragment.mPageIndex, i);
	        fragment.setArguments(args);
	        return fragment;
	    }
	
	    @Override
	    public int getCount() {
	        return mIndexCnt;
	    }
	
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	Log.d(TAG,"frag getPageTitle");
	        return "OBJECT " + (position);
	    }
	}

	// Instances of this class are fragments representing a single
	//object in our collection.
	public static class DemoObjectFragment extends Fragment {
	  public static final String mPageIndex = "pageIndex";

	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	      // The last two arguments ensure LayoutParams are inflated
	      // properly.
		  Bundle args = getArguments();
		  View rootView;
		  switch(args.getInt(mPageIndex)){
		  	case mIndexGameMain:
		  		rootView  = inflater.inflate(R.layout.fragment_game_main, container, false);
		  		setUIMain(rootView, inflater, this.getContext());
		  		break;
		  	case mIndexGameNewRound:
		  		rootView  = inflater.inflate(R.layout.fragment_game_add_round, container, false);
		  		setUINewRound(rootView, inflater, this.getContext());
		  		break;
		  	default:
		  		rootView  = inflater.inflate(R.layout.spacer_gray, container, false);
		  }
		  return rootView;
	  }
	}
	
	private static void setUIMain(View rootView, LayoutInflater inflater, Context context) {
		mLvRounds = (ListView)rootView.findViewById(R.id.fragment_game_round_list);

		mLvRoundAdapter = new GameMainListAdapter(context, mGame.getRoundList(),mGame);
		mLvRounds.setAdapter(mLvRoundAdapter);
		mLvRoundAdapter.changeRoundListViewMode(GAME_VIEW_TYPE.ROUND_VIEW_TABLE);
		mGameRoundsInfoSwipe = (LinearLayout)rootView.findViewById(R.id.fragment_game_rounds_infos);
		if(mGame != null && mGame.getRoundCount() > 0 && mLvRoundAdapter.getRoundListViewMode() == GAME_VIEW_TYPE.ROUND_VIEW_DETAIL)
			mGameRoundsInfoSwipe.removeAllViews();
		
		if(mGame != null && mGame.getRoundCount() > 0 && mLvRoundAdapter.getRoundListViewMode() == GAME_VIEW_TYPE.ROUND_VIEW_TABLE){
			mGameRoundsInfoSwipe.removeAllViews();
			createTableHeader(inflater, context);
		}
		
		mBottomInfos = (LinearLayout)rootView.findViewById(R.id.fragment_game_bottom_infos);
		mBottomInfoBockRoundCount = (TextView)rootView.findViewById(R.id.fragment_game_bottom_infos_content_bock_count);
		mBottomInfoBockRoundPreview = (TextView)rootView.findViewById(R.id.fragment_game_bottom_infos_content_bock_count_preview);

		if(mGame != null){
			if(mGame.getBockRoundLimit() == 0 )
				mBottomInfos.setVisibility(View.GONE);
			else {
                setBottomInfo(context);
            }
		}
		
	}

	
	private static void setBottomInfo(Context context) {
		int mBockRoundCnt = 0, mTmp = 0;
		StringBuilder mBockPreviewStr = new StringBuilder();
		if(mGame != null && mGame.getBockRoundLimit() > 0){
			for(int i=0;i<mGame.getPreRoundList().size();i++){
				mTmp = mGame.getPreRoundList().get(i).getBockCount();
				if(mTmp > 0){
					mBockRoundCnt++;
					mBockPreviewStr.append(Functions.getBockCountAsString(mTmp)).append("  ");
				}
			}
		}
		if(mBockPreviewStr.length() > 0) {
            mBockPreviewStr.substring(0, mBockPreviewStr.length()-1);
        }

        if (mBockRoundCnt == 0) {
            mBottomInfoBockRoundCount.setText(context.getResources().getString(R.string.str_no_bock));

        } else {
            mBottomInfoBockRoundCount.setText(String.valueOf(mBockRoundCnt));
        }

		mBottomInfoBockRoundPreview.setText(mBockPreviewStr.toString());
	}


    private static void setUINewRound(View rootView, LayoutInflater inflater, Context context) {
		winnerDraw = rootView.getResources().getDrawable(R.drawable.layer_game_add_player_win);
		loserDraw = rootView.getResources().getDrawable(R.drawable.layer_game_add_player_lose);
		suspendDraw = rootView.getResources().getDrawable(R.drawable.layer_game_add_player_suspended);


		mEtNewRoundPoints = (TextView)rootView.findViewById(R.id.game_add_round_points_entry);
        mEtNewRoundPoints.clearFocus();

		GameActivity.loadUINewRoundPlayerSection(rootView, inflater);

		mBtnAddRound = (Button)rootView.findViewById(R.id.btn_game_add_new_round);
		mBtnAddRound.setOnClickListener(mBtnAddRoundClickListener);
		
		mTvAddRoundBockPoints = (TextView)rootView.findViewById(R.id.game_add_round_bock_points);

		mGameBockDetailContainer = (LinearLayout)rootView.findViewById(R.id.game_add_round_bock_details_container);
        mBockRoundInfoIcon = (ImageView) rootView.findViewById(R.id.game_add_round_bock_info);
        mBockRoundInfoIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(R.string.str_help, R.string.str_game_points_choose_bock_info, dokoActivity);
            }
        });

        mCBNewBockRound = (CheckBox)rootView.findViewById(R.id.game_add_round_bock_cb);
		mCBNewBockRound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mBockRoundInfoIcon != null) {
					mBockRoundInfoIcon.setVisibility(mCBNewBockRound.isChecked() ? View.VISIBLE : View.INVISIBLE);
				}

				if (mGameBockDetailContainer != null) {
                    mGameBockDetailContainer.setVisibility(mCBNewBockRound.isChecked() ? View.VISIBLE : View.GONE);
				}
			}
		});

        mLayout = (LinearLayout)rootView.findViewById(R.id.game_add_round_bock_container);
		if(mGame.getBockRoundLimit() == 0) {
            mLayout.setVisibility(View.GONE);
        }
		else {
            mLayout.setVisibility(View.VISIBLE);
        }

        // bock spinners
        mGameBockRoundsCnt = (Spinner)rootView.findViewById(R.id.game_bock_rounds_cnt);
        mGameBockRoundsGameCnt = (Spinner)rootView.findViewById(R.id.game_bock_rounds_game_cnt);

        ArrayList<Integer>mMaxPlayerInteger = new ArrayList<Integer>();
        for(int k=1;k<=DokoData.MAX_PLAYER;k++) {
            mMaxPlayerInteger.add(k);
        }

        mGameBockRoundsCntAdapter = new ArrayAdapter<Integer>(context, R.layout.spinner_item,R.id.spinner_text,mMaxPlayerInteger);
        mGameBockRoundsCntAdapter.setDropDownViewResource(R.layout.spinner_item);
        mGameBockRoundsCnt.setAdapter(mGameBockRoundsCntAdapter);
        mGameBockRoundsCnt.setSelection(0);

        mGameBockRoundsGameCntAdapter = new ArrayAdapter<Integer>(context, R.layout.spinner_item,R.id.spinner_text,mMaxPlayerInteger);
        mGameBockRoundsGameCntAdapter.setDropDownViewResource(R.layout.spinner_item);
        mGameBockRoundsGameCnt.setAdapter(mGameBockRoundsGameCntAdapter);
        mGameBockRoundsGameCnt.setSelection(mGame.getPlayerCount() - 1);

        mRadioGroup = (RadioGroup)rootView.findViewById(R.id.rb_group);

		RadioButton rbWinnerRe = (RadioButton)rootView.findViewById(R.id.rb_re_won);
		rbWinnerRe.setOnClickListener(view -> {
			Log.d("INFO", "Re radio button was pressed");
			// check "No 120" button if not already done
			if (!mListGameResultTBs.get(0).isChecked()) {
				Log.d("INFO", "120 is unchecked when Re-Party was selected as winner");
				// set inverse state of 120 button
				mListGameResultTBs.get(0).setChecked(true);
				setAllWinnerButtonsUntil(120);
			}
			updatePointsAndTextView();
		});
		RadioButton rbWinnerKontra = (RadioButton)rootView.findViewById(R.id.rb_kontra_won);
		rbWinnerKontra.setOnClickListener(view -> {
			Log.d("INFO", "Kontra radio button was pressed");
			// check at least "No 120" button if not already done
			if (!mListGameResultTBs.get(0).isChecked()) {
				Log.d("INFO", "120 is unchecked when Kontra-Party was selected as winner");
				// set inverse state of 120 button
				mListGameResultTBs.get(0).setChecked(true);
				setAllWinnerButtonsUntil(120);
			}
			updatePointsAndTextView();
		});
		mMapRBWinner.put(GAME_PARTY.PARTY_RE, rbWinnerRe);
		mMapRBWinner.put(GAME_PARTY.PARTY_KONTRA, rbWinnerKontra);
		mTBNo120 = (ToggleButton)rootView.findViewById(R.id.tb_no120);
		mTBNo120.setOnClickListener(view -> {
			if (mTBNo120 != null) {
				setAllWinnerButtonsUntil(120);
			}
		});
		mTBNo90 = (ToggleButton)rootView.findViewById(R.id.tb_no90);
		mTBNo90.setOnClickListener(view -> {
			if (mTBNo90 != null) {
				setAllWinnerButtonsUntil(90);
			}
		});
		mTBNo60 = (ToggleButton)rootView.findViewById(R.id.tb_no60);
		mTBNo60.setOnClickListener(view -> {
			if (mTBNo60 != null) {
				setAllWinnerButtonsUntil(60);
			}
		});
		mTBNo30 = (ToggleButton)rootView.findViewById(R.id.tb_no30);
		mTBNo30.setOnClickListener(view -> {
			if (mTBNo30 != null) {
				setAllWinnerButtonsUntil(30);
			}
		});
		mTBNo0 = (ToggleButton)rootView.findViewById(R.id.tb_no0);
		mTBNo0.setOnClickListener(view -> {
			if (mTBNo0 != null) {
				setAllWinnerButtonsUntil(0);
			}
		});
		Log.d("INFO", "Size of mListGameResultTBs before adding the Game result buttons: " + mListGameResultTBs.size());
		mListGameResultTBs.add(mTBNo120);
        mListGameResultTBs.add(mTBNo90);
        mListGameResultTBs.add(mTBNo60);
        mListGameResultTBs.add(mTBNo30);
        mListGameResultTBs.add(mTBNo0);

        // buttons for round type
		// normal games
		for (Map.Entry<ROUND_TYPES, Integer> entry : normalRoundTypeButtons.entrySet()) {
			mMapTbRoundType.put(entry.getKey(), (ToggleButton)rootView.findViewById(entry.getValue()));
			mMapTbRoundType.get(entry.getKey()).setOnClickListener(view -> {
				if (mMapTbRoundType.get(entry.getKey()) != null) {
					setRoundType(entry.getKey());
				}
			});
		}

//		// solo games
		for (Map.Entry<ROUND_TYPES, Integer> entry : soloRoundTypeButtons.entrySet()) {
			mMapTbRoundType.put(entry.getKey(), (ToggleButton)rootView.findViewById(entry.getValue()));
			mMapTbRoundType.get(entry.getKey()).setOnClickListener(view -> {
				if (mMapTbRoundType.get(entry.getKey()) != null) {
					setRoundType(entry.getKey());
				}
			});
		}

		// set normal as default for new round
		setRoundType(ROUND_TYPES.NORMAL);

        // Toggle buttons for "ansagen"
        mTBReRe = (ToggleButton)rootView.findViewById(R.id.tb_re_re);
        mTBReRe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBReRe != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE, "Re");
                }
            }
        });
        mTBRe90 = (ToggleButton)rootView.findViewById(R.id.tb_re_90);
        mTBRe90.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe90 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"90");
                }
            }
        });
        mTBRe60 = (ToggleButton)rootView.findViewById(R.id.tb_re_60);
        mTBRe60.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe60 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE, "60");
                }
            }
        });
        mTBRe30 = (ToggleButton)rootView.findViewById(R.id.tb_re_30);
        mTBRe30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe30 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"30");
                }
            }
        });
        mTBRe0 = (ToggleButton)rootView.findViewById(R.id.tb_re_0);
        mTBRe0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe0 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"0");
                }
            }
        });
        mListReAnsagenTBs.add(mTBReRe);
        mListReAnsagenTBs.add(mTBRe90);
        mListReAnsagenTBs.add(mTBRe60);
        mListReAnsagenTBs.add(mTBRe30);
        mListReAnsagenTBs.add(mTBRe0);

		mTBKontraKontra = (ToggleButton)rootView.findViewById(R.id.tb_kontra_kontra);
		mTBKontraKontra.setOnClickListener(view -> {
			if (mTBKontraKontra != null) {
				setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_KONTRA, "Kontra");
			}
		});
		mTBKontra90 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_90);
		mTBKontra90.setOnClickListener(view -> {
			if (mTBKontra90 != null) {
				setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_KONTRA,"90");
			}
		});
		mTBKontra60 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_60);
		mTBKontra60.setOnClickListener(view -> {
			if (mTBKontra60 != null) {
				setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_KONTRA, "60");
			}
		});
		mTBKontra30 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_30);
		mTBKontra30.setOnClickListener(view -> {
			if (mTBKontra30 != null) {
				setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_KONTRA,"30");
			}
		});
		mTBKontra0 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_0);
		mTBKontra0.setOnClickListener(view -> {
			if (mTBKontra0 != null) {
				setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_KONTRA,"0");
			}
		});
		mListKontraAnsagenTBs.add(mTBKontraKontra);
		mListKontraAnsagenTBs.add(mTBKontra90);
		mListKontraAnsagenTBs.add(mTBKontra60);
		mListKontraAnsagenTBs.add(mTBKontra30);
		mListKontraAnsagenTBs.add(mTBKontra0);

        // detailed round info general layouts, buttons and co.
		mDetailedRoundInfo = (LinearLayout)rootView.findViewById(R.id.game_detailed_round_info_container);
		if(mGame.getPointsCalculationType() != DokoData.POINTS_CALCULATION.POINTS_CALCULATION_DETAILED) {
			mDetailedRoundInfo.setVisibility(View.GONE);
		}
		else {
			mDetailedRoundInfo.setVisibility(View.VISIBLE);
		}
		mDetailedRoundInfoTypeContainer = (LinearLayout)rootView.findViewById(R.id.ll_round_type);
		mDetailedRoundInfoAnsagenContainer = (LinearLayout)rootView.findViewById(R.id.ll_ansagen);
		mDetailedRoundInfoSpecialContainer = (LinearLayout)rootView.findViewById(R.id.ll_special_points);
		mDetailedRoundInfoTypeArrow = (ImageView) rootView.findViewById(R.id.im_round_type);
		mDetailedRoundInfoTypeArrow.setOnClickListener(view -> {
			if (mDetailedRoundInfoTypeArrow != null) {
				if (mDetailedRoundInfoTypeContainer.getVisibility() == View.VISIBLE) {
					mDetailedRoundInfoTypeContainer.setVisibility(View.GONE);
				}
				else {
					mDetailedRoundInfoTypeContainer.setVisibility(View.VISIBLE);
				}
			}
		});
		mDetailedRoundInfoAnsagenArrow = (ImageView) rootView.findViewById(R.id.im_ansagen);
		mDetailedRoundInfoAnsagenArrow.setOnClickListener(view -> {
			if (mDetailedRoundInfoAnsagenArrow != null) {
				if (mDetailedRoundInfoAnsagenContainer.getVisibility() == View.VISIBLE) {
					mDetailedRoundInfoAnsagenContainer.setVisibility(View.GONE);
				}
				else {
					mDetailedRoundInfoAnsagenContainer.setVisibility(View.VISIBLE);
				}
			}
		});
		mDetailedRoundInfoSpecialArrow = (ImageView) rootView.findViewById(R.id.im_special);
		mDetailedRoundInfoSpecialArrow.setOnClickListener(view -> {
			if (mDetailedRoundInfoSpecialArrow != null) {
				if (mDetailedRoundInfoSpecialContainer.getVisibility() == View.VISIBLE) {
					mDetailedRoundInfoSpecialContainer.setVisibility(View.GONE);
				}
				else {
					mDetailedRoundInfoSpecialContainer.setVisibility(View.VISIBLE);
				}
			}
		});

		// text views for projected points
		mTVProjectedPoints = (TextView)rootView.findViewById(R.id.text_projected_points);
		mTVProjectedPointsWinner = (TextView)rootView.findViewById(R.id.text_winner);

		// detailed round info buttons for "Sonderpunkte"
		mSBReDK = (SeekBar)rootView.findViewById(R.id.re_seekBar_DK);
		mSBReDK.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsAndTextView();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mTBReDK = (ToggleButton)rootView.findViewById(R.id.btn_re_doppelkopf);
		mMapTBsRe.put("Doppelkopf", mTBReDK);
		mTBReDK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBReDK != null) {
					updatePointsAndTextView();
				}
				if (mSBReDK != null && mTBReDK != null) {
					mSBReDK.setVisibility(mTBReDK.isChecked() ? View.VISIBLE : View.GONE);
				}
			}
		});

		mSBReFuchs = (SeekBar)rootView.findViewById(R.id.re_seekBar_Fuchs);
		mSBReFuchs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsAndTextView();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mTBReFuchs = (ToggleButton)rootView.findViewById(R.id.btn_re_fuchs);
		mMapTBsRe.put("Fuchs", mTBReFuchs);
		mTBReFuchs.setOnClickListener(view -> {
			if (mTBReFuchs != null) {
				updatePointsAndTextView();
			}
			if (mSBReFuchs != null && mTBReFuchs != null) {
				mSBReFuchs.setVisibility(mTBReFuchs.isChecked() ? View.VISIBLE : View.GONE);
			}
		});
		mTBReKarlchen = (ToggleButton)rootView.findViewById(R.id.btn_re_karl);
		mMapTBsRe.put("Karlchen", mTBReKarlchen);
		mTBReKarlchen.setOnClickListener(view -> {
			if (mTBReKarlchen != null) {
				updatePointsAndTextView();
			}
		});
		mTBReHeart = (ToggleButton)rootView.findViewById(R.id.btn_re_heart);
		mMapTBsRe.put("Herz", mTBReHeart);
		mTBReHeart.setOnClickListener(view -> {
			if (mTBReHeart != null) {
				updatePointsAndTextView();
			}
		});

		mSBKontraDK = (SeekBar)rootView.findViewById(R.id.kontra_seekBar_DK);
		mSBKontraDK.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsAndTextView();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mTBKontraDK = (ToggleButton)rootView.findViewById(R.id.btn_kontra_doppelkopf);
		mMapTBsKontra.put("Doppelkopf", mTBKontraDK);
		mTBKontraDK.setOnClickListener(view -> {
			if (mTBKontraDK != null) {
				updatePointsAndTextView();
			}
			if (mSBKontraDK != null && mTBKontraDK != null) {
				mSBKontraDK.setVisibility(mTBKontraDK.isChecked() ? View.VISIBLE : View.GONE);
			}
		});

		mSBKontraFuchs = (SeekBar)rootView.findViewById(R.id.kontra_seekBar_Fuchs);
		mSBKontraFuchs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsAndTextView();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mTBKontraFuchs = (ToggleButton)rootView.findViewById(R.id.btn_kontra_fuchs);
		mMapTBsKontra.put("Fuchs", mTBKontraFuchs);
		mTBKontraFuchs.setOnClickListener(view -> {
			if (mTBKontraFuchs != null) {
				updatePointsAndTextView();
			}
			if (mSBKontraFuchs != null && mTBKontraFuchs != null) {
				mSBKontraFuchs.setVisibility(mTBKontraFuchs.isChecked() ? View.VISIBLE : View.GONE);
			}
		});
		mTBKontraKarlchen = (ToggleButton)rootView.findViewById(R.id.btn_kontra_karl);
		mMapTBsKontra.put("Karlchen", mTBKontraKarlchen);
		mTBKontraKarlchen.setOnClickListener(view -> {
			if (mTBKontraKarlchen != null) {
				updatePointsAndTextView();
			}
		});
		mTBKontraHeart = (ToggleButton)rootView.findViewById(R.id.btn_kontra_heart);
		mMapTBsKontra.put("Herz", mTBKontraHeart);
		mTBKontraHeart.setOnClickListener(view -> {
			if (mTBKontraHeart != null) {
				updatePointsAndTextView();
			}
		});
		mTBKontraGegen = (ToggleButton)rootView.findViewById(R.id.btn_kontra_gegen);
		mMapTBsKontra.put("Gegen", mTBKontraGegen);
		mTBKontraGegen.setOnClickListener(view -> {
			if (mTBKontraGegen != null) {
				updatePointsAndTextView();
			}
		});

		resetNewRoundFields(context);
		
		rootView.findViewById(R.id.game_add_round_main_layout).requestFocus();
	}
	
	
    private static void loadUINewRoundPlayerSection(View rootView, LayoutInflater inflater) {
    	LinearLayout mLl;
    	TextView mTv;
    	int mTmp;

        mGameAddRoundPlayerState.clear();
    	
		mLayout = (LinearLayout)rootView.findViewById(R.id.game_add_round_playersection);

        LinearLayout mPointGrid = (LinearLayout)rootView.findViewById(R.id.point_grid);
        setupGridPointButtonsToEditValues(mPointGrid, mEtNewRoundPoints);
//        LinearLayout pointGrid = (LinearLayout)rootView.findViewById(R.id.game_add_round_point_grid);
        if (mGame.getPointsCalculationType() == DokoData.POINTS_CALCULATION.POINTS_CALCULATION_DETAILED) {
			mPointGrid.setVisibility(View.GONE);
		}
        else {
			mPointGrid.setVisibility(View.VISIBLE);
		}

		mTmp = (int) ((double)mGame.getPlayerCount()/2 + 0.5d);
		for(int i=0;i<(DokoData.MAX_PLAYER/2) && i<mTmp ;i++){
			mLl = (LinearLayout)inflater.inflate(R.layout.fragment_game_add_round_2_player_row, null);
			mLayout.addView(mLl);

            // left
            LinearLayout mLeftLayout = (LinearLayout)mLl.findViewById(R.id.game_add_round_player_left);

            View mPlayerColor = mLl.findViewById(R.id.game_add_round_playercolor_left);
            mPlayerColor.setBackgroundColor(rootView.getContext().getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i * 2]));

			mTv = (TextView)mLl.findViewById(R.id.game_add_round_playername_left);

			mTv.setText(mGame.getPlayer(i*2).getName());


            mLeftLayout.setOnClickListener(mAddRoundPlayernameClickListener);
			if(mGame.getPlayerCount()-mGame.getActivePlayerCount() > 0) {
                mLeftLayout.setOnLongClickListener(mAddRoundPlayernameLongClickListener);
			}

			TextView mLeftStateView = (TextView)mLeftLayout.findViewById(R.id.game_add_round_player_left_state);
            mGameAddRoundPlayerState.add(mLeftStateView);

            // right
            LinearLayout mRightLayout = (LinearLayout)mLl.findViewById(R.id.game_add_round_player_right);

            mPlayerColor = mLl.findViewById(R.id.game_add_round_playercolor_right);
            mPlayerColor.setBackgroundColor(rootView.getContext().getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i * 2+ 1]));

			mTv = (TextView)mLl.findViewById(R.id.game_add_round_playername_right);
			
			if(mGame.getPlayerCount() == 5 && i == 2){
                mRightLayout.setVisibility(View.GONE);
			} else if(mGame.getPlayerCount() == 7 && i == 3){
                mRightLayout.setVisibility(View.GONE);
			} else{
				mTv.setText(mGame.getPlayer(i*2+1).getName());

                mRightLayout.setOnClickListener(mAddRoundPlayernameClickListener);
                if(mGame.getPlayerCount()-mGame.getActivePlayerCount() > 0) {
                    mRightLayout.setOnLongClickListener(mAddRoundPlayernameLongClickListener);
                }
                TextView mRightStateView = (TextView)mRightLayout.findViewById(R.id.game_add_round_player_right_state);
                mGameAddRoundPlayerState.add(mRightStateView);

			}
		}
	}


    public static void setupGridPointButtonsToEditValues(LinearLayout grid, final TextView valueField) {
        OnClickListener clickPoints = new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                valueField.setText(valueField.getText() + tv.getText().toString());
            }
        };

        TextView v = (TextView)grid.findViewById(R.id.point_grid_top_left);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_top_center);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_top_right);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_center_left);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_center_center);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_center_right);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_bottom_left);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_bottom_center);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_bottom_right);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_footer_center);
        v.setOnClickListener(clickPoints);

        v = (TextView)grid.findViewById(R.id.point_grid_footer_right);
        v.setOnClickListener(v1 -> valueField.setText(""));
    }

	public class btnAddRoundClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(mGame.getRoundCount() == 0){
				if(mLvRoundAdapter.getRoundListViewMode() == GAME_VIEW_TYPE.ROUND_VIEW_TABLE){
					mGameRoundsInfoSwipe.removeAllViews();
					createTableHeader(mInflater, mContext);
				}
				else if(mLvRoundAdapter.getRoundListViewMode() == GAME_VIEW_TYPE.ROUND_VIEW_DETAIL){ 
					mGameRoundsInfoSwipe.removeAllViews();
				}
			}

			// new round player results were not set until now, only user selected info's (win/loss or re/kontra depending on point calculation method)
			updatePlayerWinStates();

			// check if round data is valid show error dialog in case it is not
			if(!isNewRoundDataOK()){
				Toast.makeText(v.getContext(), R.string.str_error_game_new_round_data, Toast.LENGTH_SHORT).show();
				return;
			}

            Integer mGameBockRoundsCount =  (Integer)mGameBockRoundsCnt.getSelectedItem();
            Integer mGameBockRoundsGameCount =  (Integer)mGameBockRoundsGameCnt.getSelectedItem();

            if (!isNewBockRoundSet()) {
                mGameBockRoundsCount = 0;
                mGameBockRoundsGameCount = 0;
            }

            Log.d("XML", "Round Result: " + getRoundResultXMLString());
			mGame.addNewRound(getNewRoundPoints(), mGameBockRoundsCount, mGameBockRoundsGameCount, mNewRoundPlayerWinState, mUserSelectedPlayerState, getRoundResultXMLString(), getRoundTypeXMLString(), getRoundAnsagenXMLString(GAME_PARTY.PARTY_RE), getRoundAnsagenXMLString(GAME_PARTY.PARTY_KONTRA), mReSpecialXML, mKontraSpecialXML);
			Log.d(TAG,mGame.toString());
			notifyDataSetChanged();
			 
			
			mViewPager.setCurrentItem(0,true);
			mLvRounds.requestFocus();
			
			if(mLvRounds.getCount() >= 1) {
                mLvRounds.setSelection(mLvRounds.getCount() - 1);
            }
			
			resetNewRoundFields(mContext);
			
			DokoXMLClass.saveGameStateToXML(mContext, mGame);
			
			setBottomInfo(mContext);
		}
	}

	private void updatePlayerWinStates()
	{
		for(int i=0;i<mGameAddRoundPlayerState.size();i++) {
			if (!mGame.isDetailedRoundInfo()) {
				mNewRoundPlayerWinState[i] = PLAYER_ROUND_RESULT_STATE.values()[mUserSelectedPlayerState[i].ordinal()]; // convert between Enums
			}
			else {
				// who was won?
				ROUND_RESULT_POINTS_WINNER winner = getPointsWinner();
				if (mUserSelectedPlayerState[i] == USER_SELECTED_PLAYER_STATE.SUSPEND_STATE) {
					mNewRoundPlayerWinState[i] = PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE;
				}
				// 1) Game is a tie
				else if (winner == ROUND_RESULT_POINTS_WINNER.TIE) {
					mNewRoundPlayerWinState[i] = PLAYER_ROUND_RESULT_STATE.TIE_STATE;
				}
				// 2) Player is in the team that has won the round
				else if (winner.ordinal() == mUserSelectedPlayerState[i].ordinal()) {
					mNewRoundPlayerWinState[i] = PLAYER_ROUND_RESULT_STATE.WIN_STATE;
				}
				else {
					mNewRoundPlayerWinState[i] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE;
				}
			}

			Log.d("PLAYER", "mNewRoundPlayerWinState[" + i + "]: " + mNewRoundPlayerWinState[i]);
		}
	}
	
	private void  notifyDataSetChanged() {
		if(mLvRounds.getAdapter() instanceof ArrayAdapter<?>) mLvRoundAdapter.notifyDataSetChanged();
	}
	
	
	private static void createTableHeader(LayoutInflater inflater, Context context) {
		LinearLayout mLl = null;
		TextView mTv = null;
		switch(mGame.getPlayerCount()){
			case 4: mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_round_view_table_4_player_header, null); break;
			case 5: mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_round_view_table_5_player_header, null); break;
			case 6: mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_round_view_table_6_player_header, null); break;
			case 7: mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_round_view_table_7_player_header, null); break;
			case 8: mLl = (LinearLayout) inflater.inflate(R.layout.fragment_game_round_view_table_8_player_header, null); break;
		}
		if(mLl ==  null || DokoData.mTvTablePlayerName.length < mGame.getPlayerCount()) {
            return;
        }
		
		for(int i=0;i<mGame.getPlayerCount();i++){
			mTv = (TextView)mLl.findViewById(DokoData.mTvTablePlayerName[i]);
			mTv.setText(mGame.getPlayer(i).getName());

            View color = mLl.findViewById(DokoData.mTvTablePlayerNameColor[i]);
            color.setBackgroundColor(color.getResources().getColor(DokoData.PLAYERS_COLORS_KEY[i]));
		}
		mGameRoundsInfoSwipe.removeAllViews();
		mGameRoundsInfoSwipe.addView(mLl);
		mGameRoundsInfoSwipe.setGravity(Gravity.LEFT);
	}
	
	public class GameAddRoundPlayernameClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			for(int i=0;i<mGameAddRoundPlayerState.size();i++){
                TextView mTvState = mGameAddRoundPlayerState.get(i);
                // maybe right or left
                TextView mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_left_state);
                if (mTvStateOfView == null) {
                    mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_right_state);
                }

                USER_SELECTED_PLAYER_STATE stateForPosition  =  mUserSelectedPlayerState[i];

				if(mTvState != null && mTvStateOfView == mTvState && stateForPosition != USER_SELECTED_PLAYER_STATE.SUSPEND_STATE) {
					if(stateForPosition == USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE && getUserWinOrReCnt() < mGame.getActivePlayerCount() - 1) {
						mUserSelectedPlayerState[i] = USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE;
                        changePlayerViewState(mTvState, winnerDraw, getRoundPlayerStateText(mUserSelectedPlayerState[i]), YES);
					}
					else{
						mUserSelectedPlayerState[i] = USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE;
                        changePlayerViewState(mTvState, loserDraw, getRoundPlayerStateText(mUserSelectedPlayerState[i]), YES);
					}
				}
			}
		}
    }
	
	public class GameAddRoundPlayernameLongClickListener implements OnLongClickListener{
		@Override
		public boolean onLongClick(View v) {
			for(int i=0;i<mGameAddRoundPlayerState.size();i++){
                TextView mTvState = mGameAddRoundPlayerState.get(i);

                // maybe right or left
                TextView mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_left_state);
                if (mTvStateOfView == null) {
                    mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_right_state);
                }

				USER_SELECTED_PLAYER_STATE stateForPosition  =  mUserSelectedPlayerState[i];

                if(mTvState != null && mTvStateOfView == mTvState){
					if(stateForPosition != USER_SELECTED_PLAYER_STATE.SUSPEND_STATE && getUserSuspendCnt() < mGame.getPlayerCount()-mGame.getActivePlayerCount()){
                        changePlayerViewState(mTvState, suspendDraw, R.string.str_game_points_suspend_select_text, YES);
						mUserSelectedPlayerState[i] = USER_SELECTED_PLAYER_STATE.SUSPEND_STATE;
                    }
					else if(stateForPosition == USER_SELECTED_PLAYER_STATE.SUSPEND_STATE){
						mUserSelectedPlayerState[i] = USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE;
						changePlayerViewState(mTvState, loserDraw, getRoundPlayerStateText(mUserSelectedPlayerState[i]), YES);
					}
				}
			}
			return true;	
		}
    }

    private static int getRoundPlayerStateText(USER_SELECTED_PLAYER_STATE state) {
		// string to set depends on how points are calculated (manual: set win/lose, automatic: set re/kontra)
		if (mGame.getPointsCalculationType() == DokoData.POINTS_CALCULATION.POINTS_CALCULATION_DETAILED) {
			if (state == USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) {
				return R.string.str_game_points_re_select_text;
			}
			else {
				return R.string.str_game_points_kontra_select_text;
			}
		}
		else {
			if (state == USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) {
				return R.string.str_game_points_winner_select_text;
			}
			else {
				return R.string.str_game_points_lose_select_text;
			}
		}
	}

//	// TODO: do we need this function?
//	private int userSelectedToPlayerState(USER_SELECTED_PLAYER_STATE state, int pointsInRound) {
//		Log.d("PLAYER", "calcRoundPlayerState: sate " + state);
//		boolean reHasWon = pointsInRound
//		// suspended state is independent of point calculation type
//		if (state == USER_SELECTED_PLAYER_STATE.SUSPEND_STATE) return PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE.ordinal();
//
//		if (mGame.getPointsCalculationType() == DokoData.POINTS_CALCULATION.POINTS_CALCULATION_MANUAL) {
//			if (state == USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) return PLAYER_ROUND_RESULT_STATE.WIN_STATE.ordinal();
//			else if (state == USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE) return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//			// should never be reached
//			else return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//		}
//		else if (mGame.getPointsCalculationType() == DokoData.POINTS_CALCULATION.POINTS_CALCULATION_DETAILED) {
////			Log.d("PLAYER", "Automatic points calculation");
//			// player is Re
//			if (state == USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) {
////				Log.d("PLAYER", "Player is Re");
//				// Re has not lost the round (won or 0 point game)
//				if (calculatePointsForRe() >= 0) return PLAYER_ROUND_RESULT_STATE.WIN_STATE.ordinal();
//				// Re has lost
//				else return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//			}
//			// player is kontra
//			else if (state == USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE) {
////				Log.d("PLAYER", "Player is Kontra");
//				// kontra has won
//				if (calculatePointsForRe() < 0) return PLAYER_ROUND_RESULT_STATE.WIN_STATE.ordinal();
//				// kontra has lost
//				else return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//			}
//			// should never be reached
//			else return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//		}
//		// should never be reached
//		else return PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
//	}


	private void changePlayerViewState(TextView mTvStateView, Drawable newDrawable, int stringID, boolean animate) {
		changePlayerViewState(mTvStateView, newDrawable, stringID, animate, mContext);
	}


    public static void changePlayerViewState(TextView mTvStateView, Drawable newDrawable, int stringID, boolean animate, Context context) {
        if (animate) {
            Drawable[] backgrounds = new Drawable[2];
            // Resources res = context.getResources();
            backgrounds[0] = mTvStateView.getBackground();
            backgrounds[1] = newDrawable;

            TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
            crossfader.setCrossFadeEnabled(true);

            mTvStateView.setBackgroundDrawable(crossfader);

            crossfader.startTransition(600);
        } else {
            mTvStateView.setBackgroundDrawable(newDrawable);
        }

        mTvStateView.setText(context.getResources().getString(stringID));
    }
	
	private boolean isNewRoundDataOK() {
		if(getNewRoundPoints() == -1) { // -1 is exception during calculation
            return false;
        }
		if ((isWinnerOrTeamCntOK() && isSuspendCntOK()))
		{
			return true;
		}
		// for manual points entering, we don't need further checks if points = 0
		else if (!mGame.isDetailedRoundInfo() && getNewRoundPoints() == 0)
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	private static  void resetNewRoundFields(Context context) {
		TextView mTv = null;
		mEtNewRoundPoints.setText("");
        loadDefaultPlayerStates(mNewRoundPlayerWinState, mUserSelectedPlayerState);
		
		for(int i=0;i<mGameAddRoundPlayerState.size();i++){
			mTv = mGameAddRoundPlayerState.get(i);
            changePlayerViewState(mTv, loserDraw, getRoundPlayerStateText(USER_SELECTED_PLAYER_STATE.LOSE_OR_KONTRA_STATE), NO, context);
		}

        mCBNewBockRound.setChecked(false);

        mGameBockRoundsCnt.setSelection(0);
        mGameBockRoundsGameCnt.setSelection(mGame.getPlayerCount() - 1);
        mGameBockDetailContainer.setVisibility(View.GONE);

        resetDetailedRoundInfos();

		if (mBtnAddRound != null && mBtnAddRound.getParent() != null && mBtnAddRound.getParent().getParent() != null && (mBtnAddRound.getParent().getParent() instanceof ScrollView)) {
			ScrollView sv = (ScrollView)mBtnAddRound.getParent().getParent();
			sv.fullScroll(ScrollView.FOCUS_UP);
		}
	}

	private static void resetDetailedRoundInfos() {
		// game result buttons
		for (int i = 0; i< mListGameResultTBs.size(); i++)
		{
			mListGameResultTBs.get(i).setChecked(false);
		}
		mRadioGroup.clearCheck(); // setting all radio buttons to false individually does not work
		// hide expandable containers
		mDetailedRoundInfoTypeContainer.setVisibility(View.GONE);
		mDetailedRoundInfoAnsagenContainer.setVisibility(View.GONE);
		mDetailedRoundInfoSpecialContainer.setVisibility(View.GONE);
		// round type
		for (Map.Entry<ROUND_TYPES, ToggleButton> entry : mMapTbRoundType.entrySet()) {
			entry.getValue().setChecked(false);
		}
		// an/absagen buttons
		for (int i=0; i<mListReAnsagenTBs.size(); i++) {
			mListReAnsagenTBs.get(i).setChecked(false);
		}
		for (int i=0; i<mListKontraAnsagenTBs.size(); i++) {
			mListKontraAnsagenTBs.get(i).setChecked(false);
		}
		// special points
		for (Map.Entry<String, ToggleButton> entry : mMapTBsRe.entrySet()) {
			entry.getValue().setChecked(false);
		}
		mSBReDK.setVisibility(View.GONE);
		mSBReFuchs.setVisibility(View.GONE);
		for (Map.Entry<String, ToggleButton> entry : mMapTBsKontra.entrySet()) {
			entry.getValue().setChecked(false);
		}
		mSBKontraDK.setVisibility(View.GONE);
		mSBKontraFuchs.setVisibility(View.GONE);
		updatePointsAndTextView();
	}

	private boolean isNewBockRoundSet(){
        return mCBNewBockRound.isChecked();
	}

    private int getNewRoundPoints(){
        int mPoints;
		// TODO: do we need the try block?
        try{
			if (mGame.isDetailedRoundInfo()) {
				mPoints = Math.abs(calculatePointsForRe()); // Use absolute value because Kontra points are represented with "-" in internal calculations
			}
			else {
				mPoints = Integer.parseInt(mEtNewRoundPoints.getText().toString());
			}

            if (!mGame.isAutoBockCalculationOn() && mGame.getPreRoundList().size() > 0) {
                int mBockCountRound = mGame.getPreRoundList().get(0).getBockCount();
                if (mBockCountRound > 0) {
                    mPoints /= (mGame.getPreRoundList().get(0).getBockCount() * 2);
                }
            }
            return mPoints;
        }
        catch(Exception e){
            return -1;
        }
    }

	private ROUND_RESULT_POINTS_WINNER getPointsWinner() {
		int pointsForRe = calculatePointsForRe();
		if (pointsForRe > 0)
			return ROUND_RESULT_POINTS_WINNER.RE;
		else if (pointsForRe < 0)
			return ROUND_RESULT_POINTS_WINNER.KONTRA;
		else
			return ROUND_RESULT_POINTS_WINNER.TIE;
	}
	
	private boolean isSuspendCntOK(){
		if(mGame.getPlayerCount()-mGame.getActivePlayerCount() == 0){
            return true;
        }
		return getSuspendCnt() == (mGame.getPlayerCount() - mGame.getActivePlayerCount());
	}
	
	private boolean isWinnerOrTeamCntOK(){
		if (mGame.isDetailedRoundInfo()) {
			Log.d("Debug", "Game uses detailed round info");
		}
		if (soloRoundTypeButtons.containsKey(getRoundType())) {
			Log.d("Debug", "Round type IS a solo");
		}
		// number of expected winners / members in Re party depends on type of game
		if (mGame.isDetailedRoundInfo()) {
			if (soloRoundTypeButtons.containsKey(getRoundType())) {
				return getUserWinOrReCnt() == 1; // for a solo we expect exactly one in re team
			}
			else {
				return getUserWinOrReCnt() == 2;
			}
		}
		else {
			int winnerCnt = getWinnerCnt();
			return winnerCnt < mGame.getActivePlayerCount() && winnerCnt != 0;
		}
	}
	
	private int getSuspendCnt(){
		int count = 0;
		for (PLAYER_ROUND_RESULT_STATE state : mNewRoundPlayerWinState) {
			if (state == PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE) {
				count++;
			}
		}
		return count;
	}

	private int getUserSuspendCnt() {
		int m = 0;
		for (USER_SELECTED_PLAYER_STATE user_selected_player_state : mUserSelectedPlayerState) {
			if (user_selected_player_state == USER_SELECTED_PLAYER_STATE.SUSPEND_STATE) {
				m++;
			}
		}
		return m;
	}
	
	private int getWinnerCnt(){
        int m = 0;
		for (PLAYER_ROUND_RESULT_STATE state : mNewRoundPlayerWinState) {
//			PLAYER_ROUND_RESULT_STATE stateForPosition = PLAYER_ROUND_RESULT_STATE.valueOf(state);
			if (state == PLAYER_ROUND_RESULT_STATE.WIN_STATE) {
				m++;
			}
		}
        return m;
	}

	private int getUserWinOrReCnt(){
		int winOrReCnt = 0;
		for (USER_SELECTED_PLAYER_STATE user_selected_player_state : mUserSelectedPlayerState) {
			if (user_selected_player_state == USER_SELECTED_PLAYER_STATE.WIN_OR_RE_STATE) {
				winOrReCnt++;
			}
		}
		return winOrReCnt;
	}

    private void saveStateData(Bundle outState) {
		if (mGame != null) {
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			try {
				ObjectOutput out1 = new ObjectOutputStream(bos1);
				out1.writeObject(mGame);
				out1.flush();
				out1.close();
				outState.putByteArray("GAME_KEY", bos1.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private GameClass loadStateData(Bundle savedState){
		GameClass mGame = null;
    	if(savedState != null){
			if(savedState.containsKey("GAME_KEY")){
				ObjectInputStream objectIn;
				try {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(savedState.getByteArray("GAME_KEY")));
					Object obj = objectIn.readObject();
					mGame = (GameClass) obj;

					objectIn.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
    	return mGame;
    }

	private void showExitDialog(){
		DialogInterface.OnClickListener okListener = (dialog, whichButton) -> finish();

		DialogInterface.OnClickListener abortListerner = (dialog, whichButton) -> {

		};

		showAlertDialog(R.string.str_exit_game,
				R.string.str_exit_game_q,
				R.string.str_yes, okListener,
				R.string.str_no, abortListerner);
	}
	

	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }
    
    
    @Override
  	public boolean onOptionsItemSelected(MenuItem item){
        Intent i;
    	switch(item.getItemId()) {
//    		case R.id.menu_switch_game_list_view:
//    			GAME_VIEW_TYPE mRoundListViewMode = mLvRoundAdapter.getRoundListViewMode();
//	    		if(mRoundListViewMode == GAME_VIEW_TYPE.ROUND_VIEW_DETAIL){
//	    			createTableHeader(mInflater, mContext);
//	    			mLvRoundAdapter.changeRoundListViewMode(GAME_VIEW_TYPE.ROUND_VIEW_TABLE);
//	    		}
//	    		else{
//	    			mGameRoundsInfoSwipe.removeAllViews();
//	    			mLvRoundAdapter.changeRoundListViewMode(GAME_VIEW_TYPE.ROUND_VIEW_DETAIL);
//	    		}
//
//	    		notifyDataSetChanged();
//				mLvRounds.requestFocus();
//
//				if(mLvRounds.getCount() >= 1)
//					mLvRounds.setSelection(mLvRounds.getCount()-1);
//    		break;
    		
    		case R.id.menu_change_game_settings:
    			i = new Intent(this, ChangeGameSettingActivity.class);
    			for(int k=0;k<mGame.getPlayerCount();k++){
    				i.putExtra(DokoData.PLAYERS_KEY[k], mGame.getPlayer(k).getName());
    			}
    			i.putExtra(DokoData.PLAYER_CNT_KEY, mGame.getPlayerCount());
    			i.putExtra(DokoData.BOCKLIMIT_KEY, mGame.getBockRoundLimit());
    			i.putExtra(DokoData.ACTIVE_PLAYER_KEY, mGame.getActivePlayerCount());
    			startActivityForResult(i,DokoData.CHANGE_GAME_SETTINGS_ACTIVITY_CODE);
    		break;
    		
//    		case R.id.menu_bock_preview_on_off:
//    			if(mBockPreviewOnOff){
//    				mBockPreviewOnOff = false;
//					mBottomInfos.animate()
//							.translationY(mBottomInfos.getHeight())
//							.alpha(0.0f)
//							.setDuration(300)
//							.setListener(new AnimatorListenerAdapter() {
//								@Override
//								public void onAnimationEnd(Animator animation) {
//									super.onAnimationEnd(animation);
//									mBottomInfos.setVisibility(View.GONE);
//								}
//							});
//    			}
//    			else{
//    				if(mGame != null && mGame.getBockRoundLimit() == 0){
//    					Toast.makeText(mContext, getResources().getString(R.string.str_bock_preview_not_posible), Toast.LENGTH_LONG).show();
//    					return true;
//    				}
//    				mBockPreviewOnOff = true;
//					mBottomInfos.animate()
//							.translationY(0)
//							.alpha(1.0f)
//							.setDuration(300)
//							.setListener(new AnimatorListenerAdapter() {
//								@Override
//								public void onAnimationEnd(Animator animation) {
//									super.onAnimationEnd(animation);
//									mBottomInfos.setVisibility(View.VISIBLE);
//								}
//							});
//    			}
//    		break;
    		
    		case R.id.menu_edit_round:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

				showAlertDialog(R.string.str_hint, R.string.str_edit_round_info);
    			break;

            case R.id.menu_game_result:
                i = new Intent(this, GameResultActivity.class);
                for(int k=0;k<mGame.getPlayerCount();k++){
                    i.putExtra(DokoData.PLAYERS_KEY[k], mGame.getPlayer(k).getName());
                    i.putExtra(DokoData.PLAYERS_POINTS_KEY[k], mGame.getPlayer(k).getPoints());
                }
                i.putExtra(DokoData.PLAYER_CNT_KEY, mGame.getPlayerCount());

                startActivityForResult(i,DokoData.GAME_RESULT_ACTIVITY);
                break;
    		
    		case R.id.menu_exit_game:
    			showExitDialog();
    		break;

            case R.id.menu_game_help:
                showAlertDialog(R.string.str_help, R.string.str_info_game);
    	}
    	return true;
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

    	switch (requestCode) {
			case DokoData.CHANGE_GAME_SETTINGS_ACTIVITY_CODE:
				handleChangeGameSettingsFinish(requestCode, resultCode, data);
				break;

			case DokoData.EDIT_ROUND_ACTIVITY_CODE:
				handleEditRoundFinish(requestCode, resultCode, data);

            case DokoData.GAME_RESULT_ACTIVITY:
				break;

            default:
                break;
		}
    }
    
    private void handleEditRoundFinish(int requestCode, int resultCode, Intent data) {
    	Bundle extras = null;
    	int mNewRoundPoints;
		PLAYER_ROUND_RESULT_STATE[] mEditRoundRoundPlayerStates = new PLAYER_ROUND_RESULT_STATE[DokoData.MAX_PLAYER];
        loadDefaultPlayerStates(mEditRoundRoundPlayerStates, new USER_SELECTED_PLAYER_STATE[DokoData.MAX_PLAYER]);

		
    	if(data != null) {
            extras = data.getExtras();
        }

    	if(extras != null && extras.getBoolean(DokoData.CHANGE_ROUND_KEY,false)){

    		mNewRoundPoints = extras.getInt(DokoData.ROUND_POINTS_KEY,0);

        	for(int k=0; k<mGame.getPlayerCount(); k++){
				PLAYER_ROUND_RESULT_STATE mPlayerRoundState;
				try {
					mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.values()[extras.getInt(DokoData.PLAYERS_KEY[k] + "_STATE", -1)];
				}
				catch(Exception e) {
					Toast.makeText(mContext, getResources().getString(R.string.str_edit_round_error), Toast.LENGTH_LONG).show();
					return;
				}
				switch (Objects.requireNonNull(mPlayerRoundState)) {
					case WIN_STATE:
						mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.WIN_STATE;
						break;

					case SUSPEND_STATE:
						mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE;
						break;
					case LOSE_STATE:
						mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE;
						break;
					default:
						break;
        		}
        	}

        	mGame.editLastRound(mNewRoundPoints, mEditRoundRoundPlayerStates);
        	reloadSwipeViews(); 
        	//Log.d("GA after",mGame.toString());
        	DokoXMLClass.saveGameStateToXML(mContext, mGame);
        	
        	Toast.makeText(mContext, getResources().getString(R.string.str_edit_round_finish), Toast.LENGTH_LONG).show();
        }
    }
    
    private void handleChangeGameSettingsFinish(int requestCode, int resultCode, Intent data) {
    	Bundle extras = null;
    	int mActivePlayers,mBockLimit,mPlayerCnt,mOldPlayerCnt;
    	String mName = "";

    	    	   	
    	if(data != null) extras = data.getExtras();
    	if(extras != null && extras.getBoolean(DokoData.CHANGE_GAME_SETTINGS_KEY,false)){
    		mPlayerCnt = extras.getInt(DokoData.PLAYER_CNT_KEY,0);
        	mActivePlayers =  extras.getInt(DokoData.ACTIVE_PLAYER_KEY,0);
        	mBockLimit = extras.getInt(DokoData.BOCKLIMIT_KEY,0);
        	
        	if(mPlayerCnt > DokoData.MAX_PLAYER || mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER)
        		return;
        	
        	//set new game settings
        	mOldPlayerCnt = mGame.getPlayerCount(); 
        	mGame.setPlayerCount(mPlayerCnt);
        	mGame.setActivePlayerCount(mActivePlayers);
        	mGame.setBockRoundLimit(mBockLimit);


        	for(int k=mOldPlayerCnt;k<mPlayerCnt;k++){
        		mName = extras.getString(DokoData.PLAYERS_KEY[k],"");
        		if(mName == null || mName.length() == 0) return;
        		mGame.getPlayer(k).setName(mName);
        	}

        	reloadSwipeViews(); 
        	DokoXMLClass.saveGameStateToXML(mContext, mGame);
        	
        	Toast.makeText(mContext, getResources().getString(R.string.str_change_game_settings_finish), Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onBackPressed(){
    	showExitDialog();
    }
    
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
		saveStateData(outState);
	    super.onSaveInstanceState(outState);
    }

    private String getRoundResultXMLString() {
		String ret = "";
		if (mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked()) {
			ret = "Re, ";
		}
		else if (mMapRBWinner.get(GAME_PARTY.PARTY_KONTRA).isChecked()) {
			ret = "Kontra, ";
		}
		else {
			ret = "Tie, ";
		}

		String[] ansagen = {"No 120", "No 90", "No 60", "No 30", "0"};
		ret += concatenateRoundResultStrings(ansagen, getConsecutiveChecked(mListGameResultTBs));
		return ret;
	}

	private ROUND_TYPES getRoundType() {
		for (Map.Entry<ROUND_TYPES, ToggleButton> entry : mMapTbRoundType.entrySet()) {
			if (entry.getValue().isChecked()) {
				return entry.getKey();
			}
		}
		return ROUND_TYPES.NORMAL;
	}

	private String getRoundTypeXMLString() {
		return getRoundType().name();
	}

	private String getRoundAnsagenXMLString(GAME_PARTY party) {
		ArrayList<ToggleButton> tmpList;
		String partyString;
		if (party == GAME_PARTY.PARTY_RE) {
			partyString = "Re";
			tmpList = mListReAnsagenTBs;
		}
		else if (party == GAME_PARTY.PARTY_KONTRA) {
			partyString = "Kontra";
			tmpList = mListKontraAnsagenTBs;
		}
		// should never be reached
		else return "";

		String[] ansagen = {partyString, "No 90", "No 60", "No 30", "0"};
		return concatenateRoundResultStrings(ansagen, getConsecutiveChecked(tmpList));
	}

	private String concatenateRoundResultStrings(String[] text, int elementsToUse) {
		StringBuilder ret = new StringBuilder();
		for (int i=0; i<elementsToUse; i++) {
			if (i != 0) {
				ret.append(", ");
			}
			ret.append(text[i]);
		}
		return ret.toString();
	}

    // TODO: merge both "set all until" functions
	private static void setAllWinnerButtonsUntil(Integer lastButtonToSet) {
		Log.d("INFO", "setAllWinnerButtonsUntil: Button clicked: " + lastButtonToSet);
    	ArrayList<Integer> buttons = new ArrayList<>(Arrays.asList(120, 90, 60, 30, 0));
    	// check if size of both array lists are equal
        if (buttons.size() != mListGameResultTBs.size())
        {
        	// FIXME: improve error handling. E.g. unset all buttons?
            Log.d("ERROR", "GameActivity: unexpected amount of buttons to set game result (no 120, no 90, ...). Size of mListGameResultTBs: " + mListGameResultTBs.size());
            return;
        }

    	boolean buttonStateToSet = true;
		// in case the button to activate until to was already the last activated button (check against false, because button state changed before call to this function), deactivate all buttons
		if (!(mListGameResultTBs.get(buttons.indexOf(lastButtonToSet)).isChecked())) {
			if ((buttons.indexOf(lastButtonToSet) == buttons.size() - 1) || !(mListGameResultTBs.get(buttons.indexOf(lastButtonToSet) + 1).isChecked())) {
				Log.d("INFO", "Trying to activate button that was the last activated => deactivate all buttons + Winner Radio-Button");
				buttonStateToSet = false;
				mRadioGroup.clearCheck();
			}
		}
    	for (int i=0; i<buttons.size(); i++) {
			mListGameResultTBs.get(i).setChecked(buttonStateToSet);
    		if (buttons.get(i) == lastButtonToSet) {
				buttonStateToSet = false;
			}
		}
		updatePointsAndTextView();
	}

    private static void setAllAnsagenButtonsForPartyUntil(GAME_PARTY party, String lastButtonToSet) {
		Log.d("INFO", "setAllAnsagenButtonsForPartyUntil: called... for Party " + party + ". Button clicked: " + lastButtonToSet);
		// get relevant button-list and XML member variables for party
		ArrayList<ToggleButton> list;
		String partyString;

		// first element "Re" respectively "Kontra" is missing and has to be added below
		ArrayList<String> buttons = new ArrayList<String>(Arrays.asList("90", "60", "30", "0"));

		if (party == GAME_PARTY.PARTY_RE)
		{
			partyString = "Re";
			list = mListReAnsagenTBs;
		}
		else {
			partyString = "Kontra";
			list = mListKontraAnsagenTBs;
		}
		buttons.add(0, partyString);

        // check if size of both array lists are equal
        if (buttons.size() != list.size())
        {
            Log.d("ERROR", "GameActivity: unexpected amount of buttons to set ansagen of Re/Kontra (no 90, no 60, ...)");
            return;
        }

        boolean buttonStateToSet = true;
		// in case the button to activate until to was already the last activated button (check against false, because button state changed before call to this function), deactivate all buttons
		if (!(list.get(buttons.indexOf(lastButtonToSet)).isChecked())) {
			if ((buttons.indexOf(lastButtonToSet) == buttons.size() -1) || !(list.get(buttons.indexOf(lastButtonToSet) + 1).isChecked())) { // there is no next element if element to process is already the last
				Log.d("INFO", "Trying to activate button that was the last activated => deactivate all");
				buttonStateToSet = false;
			}
		}
        for (int i=0; i<buttons.size(); i++) {
            list.get(i).setChecked(buttonStateToSet);
            if (buttonStateToSet && Objects.equals(buttons.get(i), lastButtonToSet)) {
                buttonStateToSet = false;
            }
        }
		updatePointsAndTextView();
    }

	private static void setRoundType(ROUND_TYPES type) {
		Log.d("INFO", "Button pressed: " + type);
		for (Map.Entry<ROUND_TYPES, ToggleButton> entry : mMapTbRoundType.entrySet()) {
			entry.getValue().setChecked(Objects.equals(entry.getKey(), type));
		}
	}

	// TODO: IMplement me
	// return if current round type set is a solo
//	private static boolean isRoundTypeSolo() {
//		ArrayList<String> normalGames = new ArrayList<String>[ROUND_TYPES.HOCHZEIT];
//		for (Map.Entry<String, ToggleButton> entry : mMapTbRoundType.entrySet()) {
//			if (entry.getValue().isChecked()) {
//				if ([ROUND_TYPES.HOCHZEIT].contains(entry.getKey()) )
//			}
//		}
//	}

    private static void updatePointsAndTextView() {
		Log.d("INFO", "\nUpdating projected points...");
		int points = calculatePointsForRe();
//		if (!mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked() && !mMapRBWinner.get(GAME_PARTY.PARTY_KONTRA).isChecked()) {
//			mTVProjectedPointsWinner.setText("Pls. select winner");
//			mTVProjectedPoints.setText("-");
//		}
		String projectedPoints = Math.abs(points) + " P.";
		mTVProjectedPoints.setText(projectedPoints);
		if (points > 0) {
			mTVProjectedPointsWinner.setText("(Re)");
		}
		else if (points < 0) {
			mTVProjectedPointsWinner.setText("(Kontra)");
		}
		else {
			mTVProjectedPointsWinner.setText("");
		}
	}

	private static int calculatePointsForRe() {
		// negative points equal positive points for Kontra party
    	int rePoints = 0;
    	int newPoints = 0; // only used for debugging

		// get value of Ansagen for both parties
		int ansagenValueRe = getAnsagenPointsValue(GAME_PARTY.PARTY_RE);
		int ansagenValueKontra = getAnsagenPointsValue(GAME_PARTY.PARTY_KONTRA);

    	// calculate points depending on if "Ansagen" were correct
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
			newPoints = ansagenValueRe;
			rePoints += ansagenValueRe;
		}
		// Ansage points are only counted if the other party was successful with their ansage
		else if (wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
			newPoints = -ansagenValueRe;
			rePoints -= ansagenValueRe;
		}
		Log.d("POINTS", "Points after calculateAnsagenPoints for Re: " + rePoints + ", new Points: " + newPoints);
		// calculate points for Kontra Ansage
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
			newPoints = -ansagenValueKontra;
			rePoints -= ansagenValueKontra;
		}
		// Ansage points are only counted if the other party was successful with their ansage
		else if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
			newPoints = ansagenValueKontra;
			rePoints += ansagenValueKontra;
		}
		Log.d("POINTS", "Points after calculateAnsagenPoints for Kontra: " + rePoints + ", new Points: " + newPoints);

		newPoints = calculateNormalPointsRe();
		rePoints += calculateNormalPointsRe();
		Log.d("POINTS", "Points after calculateNormalPointsForRe: " + rePoints + ", new Points: " + newPoints);

		newPoints = calculateSpecialPointsForParty(GAME_PARTY.PARTY_RE);
		rePoints += calculateSpecialPointsForParty(GAME_PARTY.PARTY_RE);
		Log.d("POINTS", "Points after special points for Re: " + rePoints + ", new Points: " + newPoints);

		newPoints = calculateSpecialPointsForParty(GAME_PARTY.PARTY_KONTRA);
		rePoints -= calculateSpecialPointsForParty(GAME_PARTY.PARTY_KONTRA);
		Log.d("POINTS", "Points after special points for Kontra: " + rePoints + ", new Points: " + newPoints);
    	return rePoints;
	}

	private static int calculateNormalPointsRe() {
		int points = 0;
		int gameValue = getConsecutiveChecked(mListGameResultTBs);
		boolean reWonRound = mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked();
		boolean kontraWonRound = mMapRBWinner.get(GAME_PARTY.PARTY_KONTRA).isChecked();
		boolean isTie = !reWonRound && !kontraWonRound;

		//int reAnsageValue = getAnsagenPointsValue(GAME_PARTY.PARTY_RE);
		//int kontraAnsageValue = getAnsagenPointsValue(GAME_PARTY.PARTY_CONTRA);
//		boolean kontraAnsageSuccess = wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA);

		boolean takeReAnsageIntoAccount = getAnsagenPointsValue(GAME_PARTY.PARTY_RE) != 0; // TODO: WHY??
		boolean takeKontraAnsageIntoAccount = getAnsagenPointsValue(GAME_PARTY.PARTY_KONTRA) != 0;
		// catch error case
		if (takeReAnsageIntoAccount && takeKontraAnsageIntoAccount && wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
			Log.d("ERROR", "GameActivity: Both parties had success with their Ansagen (value > 0) which is not possible!");
			return 0;
		}

		// points for having more points (no 120, ..) are only awarded to any party if the Ansage of at least one party was correct
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE) || wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
			// check who won the round
			if (reWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points += gameValue;
			}
			else if (kontraWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
				points -= gameValue;
			}
			// make sure that "round won" points is taken into account (2 possible cases)
			else if (kontraWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points += 1;
			}
			else if (reWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
				points -= 1;
			}
			//else return 0; // leave function since we do not know who has more points scored (button is not set by user)
		}
		Log.d("POINTS", "Points after checking for at least 1 successful Ansage: " + points);
//		// Re won, but ansage failed => no points for Re and possibly additional points for kontra for each "no x" that has failed
//		if (takeReAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && reWonRound) {
//			return -(getConsectuiveChecked(mListReAnsagenTBs) - gameValue);
//		}
//		// re lost and ansage failed => game value is fully added to kontra's points
//		else if(takeReAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && kontraWonRound) {
//			return -(getConsectuiveChecked(mListReAnsagenTBs) - 1 + gameValue); // -1 since no 120 would be taken into account twice then
//		}
//		// Kontra won, but ansage failed => no points for Kontra and possibly additional points for re for each "no x" that has failed
//		else if (takeKontraAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA) && kontraWonRound) {
//			return getConsectuiveChecked(mListKontraAnsagenTBs) - gameValue;
//		}
//		// kontra lost and ansage failed => game value is fully added to kontra's points
//		else if(takeKontraAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA) && reWonRound) {
//			return (getConsectuiveChecked(mListKontraAnsagenTBs) - 1 + gameValue); // -1 since no 120 would be taken into account twice then
//		}
//		else {
//
//		}
//		// both Ansagen were wrong => ignore normal game value and only give credit for absagen of the other party that have failed
//		if (takeReAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && takeKontraAnsageIntoAccount && !wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA))
//		{

		if (reWonRound) {
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
				points += getConsecutiveChecked(mListKontraAnsagenTBs) - 1; // all Absagen of other team were beaten (only take no 90 and lower into account => reason for "-1")
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (1): " + points);
			// check for points due to failed Ansagen of Re
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points -= (getConsecutiveChecked(mListReAnsagenTBs) - 1 - gameValue);
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (2): " + points);
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && !wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA) && gameValue != 0) {
				// check for correct Ansagen "no 90" or lower
				points += gameValue - 1; // only no 90 or lower
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (3): " + points);
		}
		else if (kontraWonRound) {
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points -= getConsecutiveChecked(mListReAnsagenTBs) - 1;  // all Absagen of other team were beaten (only take no 90 and lower into account => reason for "-1")
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (1): " + points);
			// check for points due to failed Ansagen of Kontra
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA)) {
				points += (getConsecutiveChecked(mListKontraAnsagenTBs) - 1 - gameValue);
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (2): " + points);
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && !wasAnsageSuccessful(GAME_PARTY.PARTY_KONTRA) && gameValue != 0) {
				// check for correct Ansagen "no 90" or lower
				points -= gameValue - 1; // only no 90 or lower
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (3): " + points);
		}
		// Tie
		else {
			// Kontra "wins" with a tie
			points -= 1;
		}
		Log.d("POINTS", "Points after checking for tie: " + points);

		return points;
	}

	private static int getConsecutiveChecked(@NonNull ArrayList<ToggleButton> list) {
		int consecutiveChecked = 0;
		for (ToggleButton tb : list) {
			if (tb.isChecked()) {
				consecutiveChecked++;
			}
			else break;
		}
		return consecutiveChecked;
	}

	private static int getAnsagenPointsValue(GAME_PARTY party) {
		ArrayList<ToggleButton> tmpList;
		if (party == GAME_PARTY.PARTY_RE) tmpList = mListReAnsagenTBs;
		else if (party == GAME_PARTY.PARTY_KONTRA) tmpList = mListKontraAnsagenTBs;
		else {
			// TODO: How to handle this case?
			Log.d("ERROR", "Unexpected party provided to determine if Ansage was successful");
			return 0;
		}

		int ansageValue = getConsecutiveChecked(tmpList);
		if (ansageValue > 0) {
			return ansageValue + 1; // First "Ansage" equals 2 points
		}
		else return 0;
	}

	private static boolean wasAnsageSuccessful(GAME_PARTY party) {
		ArrayList<ToggleButton> ansagenList;
		GAME_PARTY otherParty;
//		ArrayList<ToggleButton> otherPartyAnsagenList;

//		// it has to be selected which team has more points
//		if (!mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked() && !mMapRBWinner.get(GAME_PARTY.PARTY_KONTRA).isChecked())
//		{
//			Log.d("INFO", "It wasn't selected which team has more points => cannot say if Ansage was correct");
//			return false;
//		}
    	if (party == GAME_PARTY.PARTY_RE) {
    		ansagenList = mListReAnsagenTBs;
    		otherParty = GAME_PARTY.PARTY_KONTRA;
//    		otherPartyAnsagenList = mListKontraAnsagenTBs;
		}
    	else if (party == GAME_PARTY.PARTY_KONTRA) {
    		ansagenList = mListKontraAnsagenTBs;
    		otherParty = GAME_PARTY.PARTY_RE;
//			otherPartyAnsagenList = mListReAnsagenTBs;
		}
    	else {
    		Log.d("ERROR", "Unexpected party provided to determine if Ansage was successful");
    		return false;
		}

    	// compare "highest" Ansage with game result to see if it was successful
		int consecutiveAnsagen = getConsecutiveChecked(ansagenList);
		if (consecutiveAnsagen == 0) {
//			Log.d("INFO", "Ansage-Success: No ansage done for party");
			return true;
		}
		// case 1: other party has more points
		if (mMapRBWinner.get(otherParty).isChecked()) {
			if (!wasAnsageSuccessful(otherParty) && consecutiveAnsagen == 1) { //other parties Ansage failed but party correctly thought that it will happen saying re/kontra
				return consecutiveAnsagen < getConsecutiveChecked(mListGameResultTBs);
			}
			else {
				return false;
			}
//			Log.d("INFO", "Ansage-Success: Other party has won and therefore Ansage is false");
//			return false;
		}
		// case 2: party won the round, but was Ansage correct?
		else if (mMapRBWinner.get(party).isChecked()) {
			return consecutiveAnsagen <= getConsecutiveChecked(mListGameResultTBs);
//			if (ret) {
////				Log.d("INFO", "Ansage-Success: Party won round and ansage was correct");
//			}
//			else Log.d("INFO", "Ansage-Success: Party won round and ansage was correct");
//			return ret;
		}
		// case 3: 120 points for both teams
		else {
			return false; // TODO: Look into it in more detail, maybe it is correct because other party said more or see case 1, that you can say re/kontra to state, that other team will not meet it
		}
	}

    private static int calculateSpecialPointsForParty(GAME_PARTY party) {
		String partyString;
		ArrayList<String> specialXML;
		Map<String, ToggleButton> tbMap;
		SeekBar sbDK;
		SeekBar sbFuchs;

		int points = 0;
		if (party == GAME_PARTY.PARTY_RE) {
			partyString = "Re";
			specialXML = mReSpecialXML;
			tbMap = mMapTBsRe;
			sbDK = mSBReDK;
			sbFuchs = mSBReFuchs;
		}
		else if (party == GAME_PARTY.PARTY_KONTRA) {
			partyString = "Kontra";
			specialXML = mKontraSpecialXML;
			tbMap = mMapTBsKontra;
			sbDK = mSBKontraDK;
			sbFuchs = mSBKontraFuchs;
		}
		else {
			Log.d("ERROR", "Unexpected party provided to calculate special points");
			return 0;
		}

		specialXML.clear();
		for (Map.Entry<String, ToggleButton> entry : tbMap.entrySet()) {
			if (entry.getValue().isChecked()) {
				if (!Objects.equals(entry.getKey(), "Doppelkopf") && !Objects.equals(entry.getKey(), "Fuchs")) {
					points++;
					specialXML.add(entry.getKey());
				} else if (Objects.equals(entry.getKey(), "Doppelkopf")) {
					points += sbDK.getProgress();
					specialXML.add(sbDK.getProgress() + "x " + entry.getKey());
				} else if (Objects.equals(entry.getKey(), "Fuchs")) {
					points += sbFuchs.getProgress();
					specialXML.add(sbFuchs.getProgress() + "x " + entry.getKey());
				}
			}
		}
		Log.d("XML", partyString + " Special (count): " + specialXML.size());
		for (int i = 0; i < specialXML.size(); i++) {
			Log.d("XML", partyString + " Special: " + specialXML.get(i));
		}
		return points;
	}
}
