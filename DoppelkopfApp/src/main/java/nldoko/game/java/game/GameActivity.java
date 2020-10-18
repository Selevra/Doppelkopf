package nldoko.game.java.game;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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

	private String TAG = "Game";

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
    private int mFocusedPage = 0;
	private static final int mIndexGameMain 		= 0;
	private static final int mIndexGameNewRound 	= 1;
	protected static GameClass mGame;
    private static int mNewRoundPlayerState[] = new int[DokoData.MAX_PLAYER];


    private static CheckBox mCBNewBockRound;
	private static ImageView mBockRoundInfoIcon;
	private static LinearLayout mGameBockDetailContainer;

	// detailed info of round: general items
	private static CheckBox mCBDetailedRoundInfo;
	private static LinearLayout mDetailedRoundInfo;
	private static ImageView mDetailedRoundInfoIcon;
	private static LinearLayout mDetailedRoundInfoContainer;
	private static LinearLayout mDetailedRoundInfoAnsagenContainer;
	private static LinearLayout mDetailedRoundInfoSpecialContainer;
	private static ImageView mDetailedRoundInfoAnsagenArrow;
	private static ImageView mDetailedRoundInfoSpecialArrow;
	// ... round result buttons
	private static RadioButton mRGWinnerRe;
	private static RadioButton mRGWinnerKontra;
	private static Map<GAME_PARTY, RadioButton> mMapRBWinner = new HashMap<GAME_PARTY, RadioButton>();
	private static ToggleButton mTBNo120;
	private static ToggleButton mTBNo90;
	private static ToggleButton mTBNo60;
	private static ToggleButton mTBNo30;
	private static ToggleButton mTBNo0;
	private static ArrayList<ToggleButton> mListResultTBs = new ArrayList<ToggleButton>();
	private static String mRoundResultXML;
	// ... "Ansagen" Re
	private static ToggleButton mTBReRe;
	private static ToggleButton mTBRe90;
	private static ToggleButton mTBRe60;
	private static ToggleButton mTBRe30;
	private static ToggleButton mTBRe0;
    private static ArrayList<ToggleButton> mListReAnsagenTBs = new ArrayList<ToggleButton>();
    private static String mRoundAnsagenReXML;
	// ... "Ansagen" Kontra
	private static ToggleButton mTBKontraKontra;
	private static ToggleButton mTBKontra90;
	private static ToggleButton mTBKontra60;
	private static ToggleButton mTBKontra30;
	private static ToggleButton mTBKontra0;
    private static ArrayList<ToggleButton> mListKontraAnsagenTBs = new ArrayList<ToggleButton>();
    private static String mRoundAnsagenKontraXML;
	// "Sonderpunkte" Re
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
	private static Map<String, ToggleButton> mMapTBsRe = new HashMap<String, ToggleButton>();
	private static Map<String, ToggleButton> mMapTBsKontra = new HashMap<String, ToggleButton>();
	private static ArrayList<String> mReSpecialXML = new ArrayList<String>();
	private static ArrayList<String> mKontraSpecialXML = new ArrayList<String>();

    private static GameAddRoundPlayernameClickListener mAddRoundPlayernameClickListener;
    private static GameAddRoundPlayernameLongClickListener mAddRoundPlayernameLongClickListener;
    private static btnAddRoundClickListener mBtnAddRoundClickListener;

    private ProgressDialog progressDialog;

	private static Drawable winnerDraw;
	private static Drawable loserDraw;
	private static Drawable suspendDraw;

	public enum GAME_PARTY {
		PARTY_RE,
		PARTY_CONTRA,
	}


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

        loadDefaultPlayerStates(mNewRoundPlayerState);

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

    private static void loadDefaultPlayerStates(int[] states) {
		//default
		for (int i = 0; i < states.length; i++) {
            states[i] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
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
            mFocusedPage = position;
            String mStr = "";
            switch (mFocusedPage) {
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
		DokoData.PUNKTEINGABE mPunkteeingabe;
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
			mPunkteeingabe	= (DokoData.PUNKTEINGABE)intent.getSerializableExtra(DokoData.PUNKTEEINGABE_KEY);
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER
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
        	mGame = new GameClass(5, 4, 1, GAME_CNT_VARIANT.CNT_VARIANT_NORMAL, DokoData.PUNKTEINGABE.PUNKTEEINGABE_MANUAL);
	    	
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
		String mBockPreviewStr = "";
		if(mGame != null && mGame.getBockRoundLimit() > 0){
			for(int i=0;i<mGame.getPreRoundList().size();i++){
				mTmp = mGame.getPreRoundList().get(i).getBockCount();
				if(mTmp > 0){
					mBockRoundCnt++;
					mBockPreviewStr += Functions.getBockCountAsString(mTmp)+"  ";
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

		mBottomInfoBockRoundPreview.setText(mBockPreviewStr);
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
                dokoActivity.showAlertDialog(R.string.str_help, R.string.str_game_points_choose_bock_info, dokoActivity);
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

        // round result: who is the winner?
		mRGWinnerRe = (RadioButton)rootView.findViewById(R.id.rb_re_won);
		mRGWinnerRe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mRGWinnerRe != null) {
					updatePointsTextView();
				}
			}
		});
		mRGWinnerKontra = (RadioButton)rootView.findViewById(R.id.rb_kontra_won);
		mRGWinnerKontra.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mRGWinnerKontra != null) {
					updatePointsTextView();
				}
			}
		});
		mMapRBWinner.put(GAME_PARTY.PARTY_RE, mRGWinnerRe);
		mMapRBWinner.put(GAME_PARTY.PARTY_CONTRA, mRGWinnerKontra);
		mTBNo120 = (ToggleButton)rootView.findViewById(R.id.tb_no120);
		mTBNo120.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBNo120 != null) {
					setAllWinnerButtonsUntil(120);
					updatePointsTextView();
				}
			}
		});
		mTBNo90 = (ToggleButton)rootView.findViewById(R.id.tb_no90);
		mTBNo90.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBNo90 != null) {
					setAllWinnerButtonsUntil(90);
					updatePointsTextView();
				}
			}
		});
		mTBNo60 = (ToggleButton)rootView.findViewById(R.id.tb_no60);
		mTBNo60.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBNo60 != null) {
					setAllWinnerButtonsUntil(60);
					updatePointsTextView();
				}
			}
		});
		mTBNo30 = (ToggleButton)rootView.findViewById(R.id.tb_no30);
		mTBNo30.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBNo30 != null) {
					setAllWinnerButtonsUntil(30);
					updatePointsTextView();
				}
			}
		});
		mTBNo0 = (ToggleButton)rootView.findViewById(R.id.tb_no0);
		mTBNo0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBNo0 != null) {
					setAllWinnerButtonsUntil(0);
					updatePointsTextView();
				}
			}
		});
		mListResultTBs.add(mTBNo120);
        mListResultTBs.add(mTBNo90);
        mListResultTBs.add(mTBNo60);
        mListResultTBs.add(mTBNo30);
        mListResultTBs.add(mTBNo0);

        // Toggle buttons for "ansagen"
        mTBReRe = (ToggleButton)rootView.findViewById(R.id.tb_re_re);
        mTBReRe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBReRe != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE, "Re");
					updatePointsTextView();
                }
            }
        });
        mTBRe90 = (ToggleButton)rootView.findViewById(R.id.tb_re_90);
        mTBRe90.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe90 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"90");
					updatePointsTextView();
                }
            }
        });
        mTBRe60 = (ToggleButton)rootView.findViewById(R.id.tb_re_60);
        mTBRe60.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe60 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE, "60");
					updatePointsTextView();
                }
            }
        });
        mTBRe30 = (ToggleButton)rootView.findViewById(R.id.tb_re_30);
        mTBRe30.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe30 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"30");
					updatePointsTextView();
                }
            }
        });
        mTBRe0 = (ToggleButton)rootView.findViewById(R.id.tb_re_0);
        mTBRe0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTBRe0 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_RE,"0");
					updatePointsTextView();
                }
            }
        });
        mListReAnsagenTBs.add(mTBReRe);
        mListReAnsagenTBs.add(mTBRe90);
        mListReAnsagenTBs.add(mTBRe60);
        mListReAnsagenTBs.add(mTBRe30);
        mListReAnsagenTBs.add(mTBRe0);

		mTBKontraKontra = (ToggleButton)rootView.findViewById(R.id.tb_kontra_kontra);
		mTBKontraKontra.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBKontraKontra != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_CONTRA, "Kontra");
					updatePointsTextView();
				}
			}
		});
		mTBKontra90 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_90);
		mTBKontra90.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBKontra90 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_CONTRA,"90");
					updatePointsTextView();
				}
			}
		});
		mTBKontra60 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_60);
		mTBKontra60.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBKontra60 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_CONTRA, "60");
					updatePointsTextView();
				}
			}
		});
		mTBKontra30 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_30);
		mTBKontra30.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBKontra30 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_CONTRA,"30");
					updatePointsTextView();
				}
			}
		});
		mTBKontra0 = (ToggleButton)rootView.findViewById(R.id.tb_kontra_0);
		mTBKontra0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBKontra0 != null) {
					setAllAnsagenButtonsForPartyUntil(GAME_PARTY.PARTY_CONTRA,"0");
					updatePointsTextView();
				}
			}
		});
		mListKontraAnsagenTBs.add(mTBKontraKontra);
		mListKontraAnsagenTBs.add(mTBKontra90);
		mListKontraAnsagenTBs.add(mTBKontra60);
		mListKontraAnsagenTBs.add(mTBKontra30);
		mListKontraAnsagenTBs.add(mTBKontra0);

        // detailed round info general layouts, buttons and co.
		mDetailedRoundInfo = (LinearLayout)rootView.findViewById(R.id.game_detailed_round_info_container);
		if(mGame.getPunkteeingabe() != DokoData.PUNKTEINGABE.PUNKTEINGABE_AUTOMATIC) {
			mDetailedRoundInfo.setVisibility(View.GONE);
		}
		else {
			mDetailedRoundInfo.setVisibility(View.VISIBLE);
		}
		mDetailedRoundInfoAnsagenContainer = (LinearLayout)rootView.findViewById(R.id.ll_ansagen);
		mDetailedRoundInfoSpecialContainer = (LinearLayout)rootView.findViewById(R.id.ll_special_points);
		mDetailedRoundInfoAnsagenArrow = (ImageView) rootView.findViewById(R.id.im_ansagen);
		mDetailedRoundInfoAnsagenArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mDetailedRoundInfoAnsagenArrow != null) {
					if (mDetailedRoundInfoAnsagenContainer.getVisibility() == View.VISIBLE) {
						mDetailedRoundInfoAnsagenContainer.setVisibility(View.GONE);
					}
					else {
						mDetailedRoundInfoAnsagenContainer.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		mDetailedRoundInfoSpecialArrow = (ImageView) rootView.findViewById(R.id.im_special);
		mDetailedRoundInfoSpecialArrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mDetailedRoundInfoSpecialArrow != null) {
					if (mDetailedRoundInfoSpecialContainer.getVisibility() == View.VISIBLE) {
						mDetailedRoundInfoSpecialContainer.setVisibility(View.GONE);
					}
					else {
						mDetailedRoundInfoSpecialContainer.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		// text views for projected points
		mTVProjectedPoints = (TextView)rootView.findViewById(R.id.text_projected_points);
		mTVProjectedPointsWinner = (TextView)rootView.findViewById(R.id.text_winner);
		// detailed round info buttons for winner
		// detailed round info buttons for "Ansagen"
		// detailed round info buttons for "Sonderpunkte"
		mSBReDK = (SeekBar)rootView.findViewById(R.id.re_seekBar_DK);
		mSBReDK.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsTextView();
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
					updatePointsTextView();
				}
				if (mSBReDK != null) {
					mSBReDK.setVisibility(mTBReDK.isChecked() ? View.VISIBLE : View.GONE);
				}
			}
		});

		mSBReFuchs = (SeekBar)rootView.findViewById(R.id.re_seekBar_Fuchs);
		mSBReFuchs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				updatePointsTextView();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mTBReFuchs = (ToggleButton)rootView.findViewById(R.id.btn_re_fuchs);
		mMapTBsRe.put("Fuchs", mTBReFuchs);
		mTBReFuchs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBReFuchs != null) {
					updatePointsTextView();
				}
				if (mSBReFuchs != null) {
					mSBReFuchs.setVisibility(mTBReFuchs.isChecked() ? View.VISIBLE : View.GONE);
				}
			}
		});
		mTBReKarlchen = (ToggleButton)rootView.findViewById(R.id.btn_re_karl);
		mMapTBsRe.put("Karlchen", mTBReKarlchen);
		mTBReKarlchen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBReKarlchen != null) {
					updatePointsTextView();
				}
			}
		});
		mTBReHeart = (ToggleButton)rootView.findViewById(R.id.btn_re_heart);
		mMapTBsRe.put("Herz", mTBReHeart);
		mTBReHeart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mTBReHeart != null) {
					updatePointsTextView();
				}
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
        if (mGame.getPunkteeingabe() == DokoData.PUNKTEINGABE.PUNKTEINGABE_AUTOMATIC) {
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
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                valueField.setText("");
            }
        });
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

			mGame.addNewRound(getNewRoundPoints(),mGameBockRoundsCount, mGameBockRoundsGameCount,mNewRoundPlayerState, mRoundResultXML, mRoundAnsagenReXML, mRoundAnsagenKontraXML, mReSpecialXML, mKontraSpecialXML);
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
	
	private void  notifyDataSetChanged() {
		if(mLvRounds.getAdapter() instanceof ArrayAdapter<?>)((ArrayAdapter<?>) mLvRoundAdapter).notifyDataSetChanged();
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
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			for(int i=0;i<mGameAddRoundPlayerState.size();i++){
                TextView mTvState = mGameAddRoundPlayerState.get(i);
                // maybe right or left
                TextView mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_left_state);
                if (mTvStateOfView == null) {
                    mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_right_state);
                }

                PLAYER_ROUND_RESULT_STATE stateForPosition  =  PLAYER_ROUND_RESULT_STATE.valueOf(mNewRoundPlayerState[i]);

				if(mTvState != null && mTvStateOfView == mTvState && stateForPosition != PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE){
					if(stateForPosition == PLAYER_ROUND_RESULT_STATE.LOSE_STATE && getWinnerCnt() < mGame.getActivePlayerCount()-1){

                        changePlayerViewState(mTvState, winnerDraw, R.string.str_game_points_winner_select_text, YES);
                        mNewRoundPlayerState[i] = PLAYER_ROUND_RESULT_STATE.WIN_STATE.ordinal();
					}
					else{
                        mNewRoundPlayerState[i] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
                        changePlayerViewState(mTvState, loserDraw, R.string.str_game_points_lose_select_text, YES);
					}
				}
			}
		}
    }
	
	public class GameAddRoundPlayernameLongClickListener implements OnLongClickListener{
		@SuppressWarnings("deprecation")
		@Override
		public boolean onLongClick(View v) {
			for(int i=0;i<mGameAddRoundPlayerState.size();i++){
                TextView mTvState = mGameAddRoundPlayerState.get(i);

                // maybe right or left
                TextView mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_left_state);
                if (mTvStateOfView == null) {
                    mTvStateOfView = (TextView)v.findViewById(R.id.game_add_round_player_right_state);
                }

                PLAYER_ROUND_RESULT_STATE stateForPosition  =  PLAYER_ROUND_RESULT_STATE.valueOf(mNewRoundPlayerState[i]);

                if(mTvState != null && mTvStateOfView == mTvState){
					if(stateForPosition != PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE && getSuspendCnt() < mGame.getPlayerCount()-mGame.getActivePlayerCount()){
                        changePlayerViewState(mTvState, suspendDraw, R.string.str_game_points_suspend_select_text, YES);
                        mNewRoundPlayerState[i] = PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE.ordinal();
                    }
					else if(stateForPosition == PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE){
						changePlayerViewState(mTvState, loserDraw, R.string.str_game_points_lose_select_text, YES);
						mNewRoundPlayerState[i] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
					}
				}
			}
			return true;	
		}
    }

	private void changePlayerViewState(TextView mTvStateView, Drawable newDrawable, int stringID, boolean animate) {
		changePlayerViewState(mTvStateView, newDrawable, stringID, animate, mContext);
	}


    public static void changePlayerViewState(TextView mTvStateView, Drawable newDrawable, int stringID, boolean animate, Context context) {
        if (animate) {
            Drawable backgrounds[] = new Drawable[2];
            Resources res = context.getResources();
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
		if(getNewRoundPoints() == -1) {
            return false;
        }
		if(getNewRoundPoints() != 0 && (!isWinnerCntOK() || !isSuspendCntOK())) {
            return false;
        }
		return true;
	}
	
	private static  void resetNewRoundFields(Context context) {
		TextView mTv = null;
		mEtNewRoundPoints.setText("");
        loadDefaultPlayerStates(mNewRoundPlayerState);
		
		for(int i=0;i<mGameAddRoundPlayerState.size();i++){
			mTv = mGameAddRoundPlayerState.get(i);
            changePlayerViewState(mTv, loserDraw, R.string.str_game_points_lose_select_text, NO, context);
		}

        mCBNewBockRound.setChecked(false);

        mGameBockRoundsCnt.setSelection(0);
        mGameBockRoundsGameCnt.setSelection(mGame.getPlayerCount() - 1);
        mGameBockDetailContainer.setVisibility(View.GONE);


		if (mBtnAddRound != null && mBtnAddRound.getParent() != null && mBtnAddRound.getParent().getParent() != null && (mBtnAddRound.getParent().getParent() instanceof ScrollView)) {
			ScrollView sv = (ScrollView)mBtnAddRound.getParent().getParent();
			sv.fullScroll(ScrollView.FOCUS_UP);
		}
	}


	private boolean isNewBockRoundSet(){
        return mCBNewBockRound.isChecked();
	}

    private int getNewRoundPoints(){
        int mPoints;
        try{
            mPoints = Integer.valueOf(mEtNewRoundPoints.getText().toString());
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
	

	
	private boolean isSuspendCntOK(){
		if(mGame.getPlayerCount()-mGame.getActivePlayerCount() == 0){
            return true;
        }
		if(getSuspendCnt() == (mGame.getPlayerCount()-mGame.getActivePlayerCount())) {
            return true;
        }
		return false;
	}
	
	private boolean isWinnerCntOK(){
		int mWinnerCnt = getWinnerCnt();
		if(mWinnerCnt >= mGame.getActivePlayerCount() || mWinnerCnt == 0) {
            return false;
        }
		return true;
	}
	
	private int getSuspendCnt(){
		int m = 0;
		for(int i=0;i<mNewRoundPlayerState.length;i++){
            PLAYER_ROUND_RESULT_STATE stateForPosition  =  PLAYER_ROUND_RESULT_STATE.valueOf(mNewRoundPlayerState[i]);
            if(stateForPosition == PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE) {
                m++;
            }
		}
		return m;
	}
	
	private int getWinnerCnt(){
        int m = 0;
        for(int i=0;i<mNewRoundPlayerState.length;i++){
            PLAYER_ROUND_RESULT_STATE stateForPosition  =  PLAYER_ROUND_RESULT_STATE.valueOf(mNewRoundPlayerState[i]);
            if(stateForPosition == PLAYER_ROUND_RESULT_STATE.WIN_STATE) {
                m++;
            }
        }
        return m;
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
    

    
    private void saveStateData(Bundle outState){
    	if(mGame != null){
	    	ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
	    	try {
	    		ObjectOutput out1 = new ObjectOutputStream(bos1);
				out1.writeObject(mGame);
		    	out1.flush();
		    	out1.close();
		    	outState.putByteArray("GAME_KEY", bos1.toByteArray());
			} 
	    	catch (IOException e) {    	
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
		DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		};

		DialogInterface.OnClickListener abortListerner = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
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
        int mEditRoundRoundPlayerStates[] = new int[DokoData.MAX_PLAYER];
        loadDefaultPlayerStates(mEditRoundRoundPlayerStates);

		
    	if(data != null) {
            extras = data.getExtras();
        }

    	if(extras != null && extras.getBoolean(DokoData.CHANGE_ROUND_KEY,false)){

    		mNewRoundPoints = extras.getInt(DokoData.ROUND_POINTS_KEY,0);

        	for(int k=0; k<mGame.getPlayerCount(); k++){
        		int mTmpState = extras.getInt(DokoData.PLAYERS_KEY[k]+"_STATE",-1);

        		if (mTmpState == -1 || PLAYER_ROUND_RESULT_STATE.valueOf(mTmpState) == null) {
        			Toast.makeText(mContext, getResources().getString(R.string.str_edit_round_error), Toast.LENGTH_LONG).show();
        			return;
        		} else {
                    PLAYER_ROUND_RESULT_STATE mPlayerRoundState = PLAYER_ROUND_RESULT_STATE.valueOf(mTmpState);

        			switch (mPlayerRoundState) {
						case WIN_STATE:
                            mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.WIN_STATE.ordinal();
                            break;

						case SUSPEND_STATE:
                            mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.SUSPEND_STATE.ordinal();
                            break;
                        case LOSE_STATE:
                            mEditRoundRoundPlayerStates[k] = PLAYER_ROUND_RESULT_STATE.LOSE_STATE.ordinal();
                            break;
						default:
							break;
					}
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
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER
        			|| (mPlayerCnt == 0 || mActivePlayers == 0))
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

    // TODO: merge both "set all until" functions
	private static void setAllWinnerButtonsUntil(Integer lastButtonToSet) {
    	ArrayList<Integer> buttons = new ArrayList<Integer>(Arrays.asList(120, 90, 60, 30, 0));
    	// check if size of both array lists are equal
        if (buttons.size() != mListResultTBs.size())
        {
            Log.d("ERROR", "GameActivity: unexpected amount of buttons to set game result (no 120, no 90, ...)");
            return;
        }

        mRoundResultXML = mRGWinnerRe.isChecked() ? "Re" : "Kontra"; // no 120, 90, etc. will be added below

    	boolean buttonStateToSet = true;
		// in case the button to activate until to was already the last activated button (check against false, because button state changed before call to this function), deactivate all buttons
		if (!(mListResultTBs.get(buttons.indexOf(lastButtonToSet)).isChecked())) {
			if ((buttons.indexOf(lastButtonToSet) == buttons.size() -1) || !(mListResultTBs.get(buttons.indexOf(lastButtonToSet) + 1).isChecked())) {
				Log.d("INFO", "Trying to activate button that was the last activated => deactivate all");
				buttonStateToSet = false;
			}
		}
    	for (int i=0; i<buttons.size(); i++) {
			mListResultTBs.get(i).setChecked(buttonStateToSet);
			if (buttonStateToSet) {
                mRoundResultXML += ", No " + Integer.toString(buttons.get(i));
            }
    		if (buttons.get(i) == lastButtonToSet) {
				buttonStateToSet = false;
			}
		}
        Log.d("XML", ": Round Result: "  + mRoundResultXML);
	}

    private static void setAllAnsagenButtonsForPartyUntil(GAME_PARTY party, String lastButtonToSet) {
		// get relevant button-list and XML member variables for party
		ArrayList<ToggleButton> list;
		String xmlAnsagen;
		String partyString;

		// first element "Re" respecitively "Kontra" is missing and has to be added below
		ArrayList<String> buttons = new ArrayList<String>(Arrays.asList("90", "60", "30", "0"));

		if (party == GAME_PARTY.PARTY_RE)
		{
			partyString = "Re";
			list = mListReAnsagenTBs;
			xmlAnsagen = mRoundAnsagenReXML;
		}
		else {
			partyString = "Kontra";
			list = mListKontraAnsagenTBs;
			xmlAnsagen = mRoundAnsagenKontraXML;
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
            if (buttonStateToSet) {
                if (i != 0) {
                    xmlAnsagen += ", No " + buttons.get(i);
                }
                else {
                    xmlAnsagen = buttons.get(i);
                }
            }
            if (buttonStateToSet && buttons.get(i) == lastButtonToSet) {
                buttonStateToSet = false;
            }
        }
        Log.d("XML", ": " + partyString + " Ansagen: " + xmlAnsagen);
    }

    private static void updatePointsTextView() {
		Log.d("INFO", "\nUpdating projected points...");
		int points = calculatePointsForRe();
		mTVProjectedPoints.setText(Math.abs(points) + " P.");
		if (!mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked() && !mMapRBWinner.get(GAME_PARTY.PARTY_CONTRA).isChecked()) {
			mTVProjectedPointsWinner.setText("Pls. select winner");
		}
		else {
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
	}

	private static int calculatePointsForRe() {
    	int rePoints = 0;
    	int ansagenValueRe = 0;
    	int ansagenValueKontra = 0;
    	int newPoints = 0; // used for debugging

		// get value of Ansagen for both parties
		ansagenValueRe = getAnsagenPointsValue(GAME_PARTY.PARTY_RE);
		ansagenValueKontra = getAnsagenPointsValue(GAME_PARTY.PARTY_CONTRA);

    	// calculate points depending on if "Ansagen" were correct
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
			newPoints = ansagenValueRe;
			rePoints += ansagenValueRe;
		}
		// Ansage points are only counted if the other party was successful with their ansage
		else if (wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
			newPoints = -ansagenValueRe;
			rePoints -= ansagenValueRe;
		}
		Log.d("POINTS", "Points after calculateAnsagenPoints for Re: " + Integer.toString(rePoints) + ", new Points: " + Integer.toString(newPoints));
		// calculate points for Kontra Ansage
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
			newPoints = -ansagenValueKontra;
			rePoints -= ansagenValueKontra;
		}
		// Ansage points are only counted if the other party was successful with their ansage
		else if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
			newPoints = ansagenValueKontra;
			rePoints += ansagenValueKontra;
		}
		Log.d("POINTS", "Points after calculateAnsagenPoints for Kontra: " + Integer.toString(rePoints) + ", new Points: " + Integer.toString(newPoints));

		newPoints = calculateNormalPointsRe();
		rePoints += calculateNormalPointsRe();
		Log.d("POINTS", "Points after calculateNormalPointsForRe: " + Integer.toString(rePoints) + ", new Points: " + Integer.toString(newPoints));

		newPoints = calculateSpecialPointsForParty(GAME_PARTY.PARTY_RE);
		rePoints += calculateSpecialPointsForParty(GAME_PARTY.PARTY_RE);
		Log.d("POINTS", "Points after special points for Re: " + Integer.toString(rePoints) + ", new Points: " + Integer.toString(newPoints));

		newPoints = calculateSpecialPointsForParty(GAME_PARTY.PARTY_CONTRA);
		rePoints -= calculateSpecialPointsForParty(GAME_PARTY.PARTY_CONTRA);
		Log.d("POINTS", "Points after special points for Kontra: " + Integer.toString(rePoints) + ", new Points: " + Integer.toString(newPoints));
    	return rePoints;
	}

	private static int calculateNormalPointsRe() {
		int points = 0;
		int gameValue = getConsectuiveChecked(mListResultTBs);
		boolean reWonRound = mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked();
		boolean kontraWonRound = mMapRBWinner.get(GAME_PARTY.PARTY_CONTRA).isChecked();

		//int reAnsageValue = getAnsagenPointsValue(GAME_PARTY.PARTY_RE);
		//int kontraAnsageValue = getAnsagenPointsValue(GAME_PARTY.PARTY_CONTRA);
//		boolean kontraAnsageSuccess = wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA);

		boolean takeReAnsageIntoAccount = getAnsagenPointsValue(GAME_PARTY.PARTY_RE) != 0;
		boolean takeKontraAnsageIntoAccount = getAnsagenPointsValue(GAME_PARTY.PARTY_CONTRA) != 0;

		// catch error case
		if (takeReAnsageIntoAccount && takeKontraAnsageIntoAccount && wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
			Log.d("ERROR", "GameActivity: Both parties had success with their Ansagen (value > 0) which is not possible!");
			return 0;
		}

		// points for having more points (no 120, ..) are only awarded to any party if the Ansage of at least one party was correct
		if (wasAnsageSuccessful(GAME_PARTY.PARTY_RE) || wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
			// check who won the round
			if (reWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points += gameValue;
			}
			else if (kontraWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
				points -= gameValue;
			}
			// make sure that "round won" points is taken into account (2 possible cases)
			else if (kontraWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points += 1;
			}
			else if (reWonRound && wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
				points -= 1;
			}
			else return 0; // leave function since we do not know who has more points scored (button is not set by user)
		}
		Log.d("POINTS", "Points after checking for at least 1 successful Ansage: " + Integer.toString(points));
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
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
				points += getConsectuiveChecked(mListKontraAnsagenTBs) - 1; // all Absagen of other team were beaten (only take no 90 and lower into account => reason for "-1")
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (1): " + Integer.toString(points));
			// check for points due to failed Ansagen of Re
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points -= (getConsectuiveChecked(mListReAnsagenTBs) - 1 - gameValue);
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (2): " + Integer.toString(points));
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && !wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA) && gameValue != 0) {
				// check for correct Ansagen "no 90" or lower
				points += gameValue - 1; // only no 90 or lower
			}
			Log.d("POINTS", "Points after checking for success of Re Ansage (3): " + Integer.toString(points));
		}
		else if (kontraWonRound) {
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE)) {
				points -= getConsectuiveChecked(mListReAnsagenTBs) - 1;  // all Absagen of other team were beaten (only take no 90 and lower into account => reason for "-1")
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (1): " + Integer.toString(points));
			// check for points due to failed Ansagen of Kontra
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA)) {
				points += (getConsectuiveChecked(mListKontraAnsagenTBs) - 1 - gameValue);
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (2): " + Integer.toString(points));
			if (!wasAnsageSuccessful(GAME_PARTY.PARTY_RE) && !wasAnsageSuccessful(GAME_PARTY.PARTY_CONTRA) && gameValue != 0) {
				// check for correct Ansagen "no 90" or lower
				points -= gameValue - 1; // only no 90 or lower
			}
			Log.d("POINTS", "Points after checking for success of Kontra Ansage (3): " + Integer.toString(points));
		}
		else return 0; // leave function since we do not know who has more points scored (button is not set by user)

		return points;
	}

	private static int getConsectuiveChecked(@NonNull ArrayList<ToggleButton> list) {
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
		else if (party == GAME_PARTY.PARTY_CONTRA) tmpList = mListKontraAnsagenTBs;
		else {
			// TODO: How to handle this case?
			Log.d("ERROR", "Unexpected party provided to determine if Ansage was successful");
			return 0;
		}

		int ansageValue = getConsectuiveChecked(tmpList);
		if (ansageValue > 0) {
			return ansageValue + 1;
		}
		else return 0;
	}

	private static boolean wasAnsageSuccessful(GAME_PARTY party) {
		ArrayList<ToggleButton> ansagenList;
		GAME_PARTY otherParty;
//		ArrayList<ToggleButton> otherPartyAnsagenList;

		// it has to be selected which team has more points
		if (!mMapRBWinner.get(GAME_PARTY.PARTY_RE).isChecked() && !mMapRBWinner.get(GAME_PARTY.PARTY_CONTRA).isChecked())
		{
			Log.d("INFO", "It wasn't selected which team has more points => cannot say if Ansage was correct");
			return false;
		}

    	if (party == GAME_PARTY.PARTY_RE) {
    		ansagenList = mListReAnsagenTBs;
    		otherParty = GAME_PARTY.PARTY_CONTRA;
//    		otherPartyAnsagenList = mListKontraAnsagenTBs;
		}
    	else if (party == GAME_PARTY.PARTY_CONTRA) {
    		ansagenList = mListKontraAnsagenTBs;
    		otherParty = GAME_PARTY.PARTY_RE;
//			otherPartyAnsagenList = mListReAnsagenTBs;
		}
    	else {
    		Log.d("ERROR", "Unexpected party provided to determine if Ansage was successful");
    		return false;
		}

    	// compare "highest" Ansage with game result to see if it was successful
		int consecutiveAnsagen = getConsectuiveChecked(ansagenList);
		if (consecutiveAnsagen == 0) {
//			Log.d("INFO", "Ansage-Success: No ansage done for party");
			return true;
		}
		// case 1: other party has more points
		if (!mMapRBWinner.get(party).isChecked()) {
			if (!wasAnsageSuccessful(otherParty) && consecutiveAnsagen == 1) { //other parties ansage failed but party correctly thought that it will happen saying re/kontra
				return consecutiveAnsagen < getConsectuiveChecked(mListResultTBs);
			}
			else return false;
//			Log.d("INFO", "Ansage-Success: Other party has won and therefore Ansage is false");
//			return false;
		}
		// case 2: party won the round, but was Ansage correct?
		else {
			boolean ret = consecutiveAnsagen <= getConsectuiveChecked(mListResultTBs);
//			if (ret) {
////				Log.d("INFO", "Ansage-Success: Party won round and ansage was correct");
//			}
//			else Log.d("INFO", "Ansage-Success: Party won round and ansage was correct");
			return ret;
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
		else if (party == GAME_PARTY.PARTY_CONTRA) {
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
				if (entry.getKey() != "Doppelkopf" && entry.getKey() != "Fuchs") {
					points++;
					specialXML.add(entry.getKey());
				} else if (entry.getKey() == "Doppelkopf") {
					points += sbDK.getProgress();
					specialXML.add(sbDK.getProgress() + "x " + entry.getKey());
				} else if (entry.getKey() == "Fuchs") {
					points += sbFuchs.getProgress();
					specialXML.add(sbFuchs.getProgress() + "x " + entry.getKey());
				}
			}
		}
		Log.d("XML", partyString + " Special (count): " + Integer.toString(specialXML.size()));
		for (int i = 0; i < specialXML.size(); i++) {
			Log.d("XML", partyString + " Special: " + specialXML.get(i));
		}
		return points;
	}
}
