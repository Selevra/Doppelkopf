package nldoko.game.java;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import nldoko.game.R;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Map;

import nldoko.game.java.data.DokoData;
import nldoko.game.java.game.NewGameActivity;
import nldoko.game.java.game.SavedGameListActivity;
import nldoko.game.java.util.CustomTypefaceSpan;
import nldoko.game.java.util.TypefaceUtil;


public class DokoActivity extends AppCompatActivity {

    public final static boolean YES = true;
    public final static boolean NO = false;

    protected Drawer drawer;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Toolbar mToolbar;

    protected SecondaryDrawerItem drawerAbout;
    protected SecondaryDrawerItem drawerSavedGames;
    protected SecondaryDrawerItem drawerStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isDebug()) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override

                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    Log.e("EX", paramThrowable.getStackTrace().toString());
                    paramThrowable.printStackTrace();
                    handleUncaughtException (paramThread, paramThrowable);
                }
            });
        }

        mContext = this;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Intent intent = new Intent ();
        intent.setAction ("nldoko.game.java.SEND_LOG"); // see step 5.
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity (intent);

        System.exit(1); // kill off the crashed app
    }

    public boolean isDebug() {
        return ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
    }

    public void setupDrawerAndToolbar(String toolbarTitle) {
        try {
            mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(mToolbar);
            changeToolbarTitle(toolbarTitle);

            drawerStart = new SecondaryDrawerItem().withIdentifier(0).withName(R.string.str_start);
            drawerSavedGames = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.str_saved_game);
            drawerAbout = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.start_action_about);

            drawerStart.withTextColor(mContext.getResources().getColor(R.color.black));
            drawerSavedGames.withTextColor(mContext.getResources().getColor(R.color.black));
            drawerAbout.withTextColor(mContext.getResources().getColor(R.color.gray_dark));
            drawerStart.withTypeface(TypefaceUtil.getTypefaceLight(this));
            drawerSavedGames.withTypeface(TypefaceUtil.getTypefaceLight(this));
            drawerAbout.withTypeface(TypefaceUtil.getTypefaceLight(this));


            AccountHeader header = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.doppelkopf_splash)
                    .build();
            header.removeProfile(0);
            header.setSelectionFirstLineShown(NO);
            header.getHeaderBackgroundView().setScaleType(ImageView.ScaleType.CENTER_CROP);

            drawer = new DrawerBuilder()
                    .withActivity(this)
                    .withTranslucentStatusBar(true)
                    .withActionBarDrawerToggle(YES)
                    .withAccountHeader(header)
                    .withToolbar(mToolbar)
                    .withCloseOnClick(YES)
                    .addDrawerItems(
                            drawerStart,
                            drawerSavedGames,
                             new DividerDrawerItem(),
                            drawerAbout
                    )
                    .withStickyFooterDivider(YES)
                    .withStickyFooter(R.layout.drawer_footer)
                    .withFooterClickable(YES)
                    .withStickyFooterShadow(NO)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem == drawerAbout) {
                                showAbout();
                                return YES;
                            }

                            if (drawerItem == drawerSavedGames ){
                                showSavedGames();
                                return YES;
                            }

                            if (drawerItem == drawerStart) {
                                showStart();
                                return YES;
                            }

                            return NO;
                        }
                    })
                    .build();

            drawer.setSelectionAtPosition(71);
            View mV = drawer.getStickyFooter();
            if (mV != null) {
                mV.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle(mContext.getResources().getString(R.string.str_dev_mode_title));
                        alertDialog.setMessage(mContext.getResources().getString(R.string.str_dev_mode_text));

                        final EditText input = new EditText(mContext);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);


                        Typeface face=TypefaceUtil.getTypefaceLight(mContext);
                        if (face != null) {
                            input.setTypeface(face);
                        }


                        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.str_accept),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String password = input.getText().toString();
                                        if (password.trim().equalsIgnoreCase("devdoko")) {
                                                Toast.makeText(getApplicationContext(), "Password Matched", Toast.LENGTH_SHORT).show();
                                                DokoData.DEV_MODE = YES;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        alertDialog.setNegativeButton(mContext.getResources().getString(R.string.str_abort),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                        return true;
                    }
                });
            }


            setHamburgerIconActionBar();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBackArrowInToolbar() {
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void changeToolbarTitle(String title) {
        SpannableStringBuilder SS = new SpannableStringBuilder(title);
        SS.setSpan (new CustomTypefaceSpan("", TypefaceUtil.getTypefaceLight(this)), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);
    }


    protected void  showBackArrorActionBar () {
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setHamburgerIconActionBar () {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    protected static void showAlertDialog(int titleID, int msgID, DokoActivity activity) {
        activity.showAlertDialog(activity.getResources().getString(titleID), activity.getResources().getString(msgID));
    }

    protected void showAlertDialog(int titleID, int msgID) {
        showAlertDialog(getResources().getString(titleID), getResources().getString(msgID));
    }

    protected void showAlertDialog(String title, String msg) {
        showAlertDialog(title, msg, R.string.str_accept, null, 0, null);
    }

    protected void showAlertDialog(int titleID, int msgID,
                                   int okbuttonTextID, DialogInterface.OnClickListener okButtonClickListener,
                                   int negativeButtonTextID,
                                   DialogInterface.OnClickListener negativeButtonClickListener) {

        showAlertDialog(this.getResources().getString(titleID),
                this.getResources().getString(msgID),
                okbuttonTextID, okButtonClickListener,
                negativeButtonTextID, negativeButtonClickListener);


    }

    protected void showAlertDialog(String title, String msg,
                                          int okbuttonTextID, DialogInterface.OnClickListener okButtonClickListener,
                                          int negativeButtonTextID,
                                          DialogInterface.OnClickListener negativeButtonClickListener) {
        showAlertDialog(title, msg, okbuttonTextID, okButtonClickListener, negativeButtonTextID, negativeButtonClickListener, this);
    }

    public static void showAlertDialog(String title, String msg,
                                   int okbuttonTextID, DialogInterface.OnClickListener okButtonClickListener,
                                   int negativeButtonTextID,
                                   DialogInterface.OnClickListener negativeButtonClickListener, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(okbuttonTextID, okButtonClickListener);

        if (negativeButtonClickListener != null) {
            builder.setNegativeButton(negativeButtonTextID, negativeButtonClickListener);
        }

        AlertDialog dialog = builder.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.accent));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.accent));

        // Must call show() prior to fetching text view
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.LEFT);
        Typeface face=TypefaceUtil.getTypefaceLight(context);
        if (face != null) {
            messageView.setTypeface(face);
        }
    }


    private void showAbout() {
        showAlertDialog(R.string.start_action_about, R.string.str_disclaimer);
    }

    private void showSavedGames () {
        Intent i = new Intent(mContext,SavedGameListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }

    private void showStart () {
        Intent i = new Intent(mContext,NewGameActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }
}
