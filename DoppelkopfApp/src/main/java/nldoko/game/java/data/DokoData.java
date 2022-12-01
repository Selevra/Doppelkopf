package nldoko.game.java.data;

import nldoko.game.R;

import java.util.ArrayList;

public class DokoData {

	public static boolean DEV_MODE = false;
	
	public static final int MAX_PLAYER = 8;
	public static final int MAX_ACTIVE_PLAYER = 6;
	public static final int MIN_PLAYER = 4;
	
	public static ArrayList<String> PLAYER_NAMES = new ArrayList<String>();
	public static final String PLAYER_NAMES_XML = "player_names.xml";

	
	public enum GAME_ROUND_RESULT_TYPE {
		NORMAL, 
		WIN_SOLO, 
		LOSE_SOLO, 
		FIVEPLAYER_3WIN, 
		FIVEPLAYER_2WIN,
        SIXPLAYER_3WIN,
        SIXPLAYER_2WIN,
        SIXPLAYER_4WIN,
        RESTORE_ROUND;

        public static GAME_ROUND_RESULT_TYPE valueForString(String type) {
            if (type == null ){
                return RESTORE_ROUND;
            }

            if (type.equalsIgnoreCase("normal")){
                return NORMAL;
            }
            else if (type.equalsIgnoreCase("win_solo")){
                return WIN_SOLO;
            }
            else if (type.equalsIgnoreCase("lose_solo")){
                return LOSE_SOLO;
            }
            else if (type.equalsIgnoreCase("5Player_3Win")){
                return FIVEPLAYER_3WIN;
            }
            else if (type.equalsIgnoreCase("5Player_2Win")){
                return FIVEPLAYER_2WIN;
            }
            else if (type.equalsIgnoreCase("6Player_2Win")){
                return SIXPLAYER_2WIN;
            }
            else if (type.equalsIgnoreCase("6Player_4Win")){
                return SIXPLAYER_4WIN;
            }
            else if (type.equalsIgnoreCase("6Player_3Win")){
                return SIXPLAYER_3WIN;
            }

            return RESTORE_ROUND;
        }

        public static String stringValueOf(GAME_ROUND_RESULT_TYPE type) {
            if (type == null) {
                return "restore";
            }

            switch (type) {
                case NORMAL: return "normal";
                case WIN_SOLO: return "win_solo";
                case LOSE_SOLO: return "lose_solo";
                case FIVEPLAYER_3WIN: return "5Player_3Win";
                case FIVEPLAYER_2WIN: return "5Player_2Win";
                case SIXPLAYER_2WIN: return "6Player_2Win";
                case SIXPLAYER_4WIN: return "6Player_4Win";
                case SIXPLAYER_3WIN: return "6Player_3Win";
                case RESTORE_ROUND: return  "restore";
                default:
                    break;
            }
            return "restore";
        }
	}

	// must be aligned with Enum "USER_SELECTED_PLAYER_STATE"
	public enum PLAYER_ROUND_RESULT_STATE {
		WIN_STATE,
		LOSE_STATE,
		SUSPEND_STATE,
		TIE_STATE,

//		public static PLAYER_ROUND_RESULT_STATE valueOf(int state) {
//			switch (state) {
//				case 0:	return LOSE_STATE;
//				case 1:	return WIN_STATE;
//				case 2:	return SUSPEND_STATE;
//				default:
//					break;
//			}
//			return null;
//		}
	}

	public enum PLAYER_ROUND_PARTY {
		PARTY_RE,
		PARTY_KONTRA,
		PARTY_NOT_SET
	}
	
	public enum GAME_CNT_VARIANT {
		CNT_VARIANT_NORMAL,
		CNT_VARIANT_LOSE,
		CNT_VARIANT_WIN
	}

	public enum POINTS_CALCULATION {
		POINTS_CALCULATION_MANUAL,
		POINTS_CALCULATION_DETAILED
	}

	public enum GAME_VIEW_TYPE {
		ROUND_VIEW_DETAIL,
		ROUND_VIEW_TABLE
	}
	
	public static final Integer[] mTvTablePlayerName = {
			R.id.fragment_game_round_view_table_player_1,
			R.id.fragment_game_round_view_table_player_2,
			R.id.fragment_game_round_view_table_player_3,
			R.id.fragment_game_round_view_table_player_4,
			R.id.fragment_game_round_view_table_player_5,
			R.id.fragment_game_round_view_table_player_6,
			R.id.fragment_game_round_view_table_player_7,
			R.id.fragment_game_round_view_table_player_8,
	};

	public static final Integer[] mTvTablePlayerNameColor = {
			R.id.fragment_game_round_view_table_player_1_color,
			R.id.fragment_game_round_view_table_player_2_color,
			R.id.fragment_game_round_view_table_player_3_color,
			R.id.fragment_game_round_view_table_player_4_color,
			R.id.fragment_game_round_view_table_player_5_color,
			R.id.fragment_game_round_view_table_player_6_color,
			R.id.fragment_game_round_view_table_player_7_color,
			R.id.fragment_game_round_view_table_player_8_color,
	};


	// name - description
	public static final Integer[][] GAME_CNT_VARAINT_ARRAY  = {
		{R.string.str_info_cnt_cnt_variant_std_name, R.string.str_info_cnt_cnt_variant_standard},
		{R.string.str_info_cnt_cnt_variant_win_name, R.string.str_info_cnt_cnt_variant_win},
		{R.string.str_info_cnt_cnt_variant_lose_name, R.string.str_info_cnt_cnt_variant_lose} };
	
	
	public static final String CHANGE_GAME_SETTINGS_KEY = "CHANGE_GAME_SETTINGS";
	public static final String CHANGE_ROUND_KEY			= "CHANGE_ROUND";
	public static final String PLAYER_CNT_KEY 			= "PLAYER_CNT";
	public static final String BOCKLIMIT_KEY 			= "BOCKLIMIT";
	public static final String BOCKROUND_KEY 			= "BOCKROUND";
	public static final String ACTIVE_PLAYER_KEY 		= "ACTIVE_PLAYERS";
	public static final String GAME_CNT_VARIANT_KEY 	= "GAME_CNT_VARIANT";
	public static final String PUNKTEEINGABE_KEY		= "PUNKTEINGABE";
	public static final String ROUND_POINTS_KEY 		= "ROUND_POINTS";
	public static final String ROUND_ID 				= "ROUND_ID";

	
	public static final String[] PLAYERS_KEY  = {"PLAYER_1","PLAYER_2","PLAYER_3","PLAYER_4",
												 "PLAYER_5","PLAYER_6","PLAYER_7","PLAYER_8"};

    public static final String[] PLAYERS_POINTS_KEY = {"PLAYER_1_POINTS","PLAYER_2_POINTS","PLAYER_3_POINTS","PLAYER_4_POINTS",
            "PLAYER_5_POINTS","PLAYER_6_POINTS","PLAYER_7_POINTS","PLAYER_8_POINTS"};

    public static final Integer[] PLAYERS_COLORS_KEY = {R.color.player_1_color, R.color.player_2_color, R.color.player_3_color, R.color.player_4_color,
            R.color.player_5_color,R.color.player_6_color, R.color.player_7_color, R.color.player_8_color};

	public static final int CHANGE_GAME_SETTINGS_ACTIVITY_CODE = 1122;
	public static final int EDIT_ROUND_ACTIVITY_CODE = 1133;
    public static final int GAME_RESULT_ACTIVITY = 1144;
}
