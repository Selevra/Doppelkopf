package nldoko.game.java.game;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EnumSet;

import nldoko.game.java.DokoActivity;
import nldoko.game.R;
import nldoko.game.java.XML.DokoXMLClass;
import nldoko.game.java.data.DokoData;
import nldoko.game.java.data.DokoData.GAME_CNT_VARIANT;
import nldoko.game.java.data.DokoData.GAME_VIEW_TYPE;
import nldoko.game.java.data.DokoData.PLAYER_ROUND_RESULT_STATE;
import nldoko.game.java.data.GameClass;
import nldoko.game.java.util.Functions;
import nldoko.game.java.util.TypefaceUtil;

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

    private static GameAddRoundPlayernameClickListener mAddRoundPlayernameClickListener;
    private static GameAddRoundPlayernameLongClickListener mAddRoundPlayernameLongClickListener;
    private static btnAddRoundClickListener mBtnAddRoundClickListener;

    private ProgressDialog progressDialog;

	private static Drawable winnerDraw;
	private static Drawable loserDraw;
	private static Drawable suspendDraw;


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
        	
        	if(mPlayerCnt < DokoData.MIN_PLAYER || mPlayerCnt > DokoData.MAX_PLAYER 
        			|| mActivePlayers > mPlayerCnt || mActivePlayers < DokoData.MIN_PLAYER
        			|| (mPlayerCnt == 0 || mActivePlayers == 0))
        		return null;

        	mGame = new GameClass(mPlayerCnt, mActivePlayers, mBockLimit, mGameCntVaraint);

        	for(int k=0;k<mPlayerCnt;k++){
        		//Log.d(TAG,mTmp+"k:"+k);
        		mTmp = extras.getString(DokoData.PLAYERS_KEY[k],"");
        		if(mTmp == null || mTmp.length() == 0) return null;
        		//Log.d(TAG,mTmp);
        		mGame.getPlayer(k).setName(mTmp);
        	}
        }
        else{
        	mGame = new GameClass(5, 4, 1, GAME_CNT_VARIANT.CNT_VARIANT_NORMAL);
	    	
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

			mGame.addNewRound(getNewRoundPoints(),mGameBockRoundsCount, mGameBockRoundsGameCount,mNewRoundPlayerState);
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

    
}
