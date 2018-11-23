package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.content.res.TypedArray;

public class GlobalConstants {

    //Settings Keys
    public static final String KEY_PREF_CURRENCY = "key_currency";
    public static final String KEY_PREF_GOOGLE = "key_google";
    public static final String KEY_PREF_THEME = "key_theme";
    public static final String KEY_PREF_ONBOARD = "key_onboard";
    public static final String KEY_PREF_ONBOARDSHOWHABIT = "key_onboard_showhabit";
    public static final String KEY_PREF_NOT_TIME = "key_not_time";
    public static final String KEY_PREF_NOT_ON = "key_not_on";
    public static int COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACENT, EDIT_TEXT_COLOR, PRIMARY_TEXT_COLOR_BLACK;

    public final static String CHANNEL_ID = "habitNotifications";
    public final static String CHANNEL_NAME = "Bad habit notifier";


    private static GlobalConstants single_instance = null;
    private static Context mContext;

    private GlobalConstants(Context context) {
        this.mContext = context;

        {
            int[] attr = {R.attr.colorPrimary};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_PRIMARY = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.colorPrimaryDark};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_PRIMARY_DARK = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.colorAccent};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_ACENT = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.editTextColor};
            TypedArray ta = context.obtainStyledAttributes(attr);
            EDIT_TEXT_COLOR = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
    }

    public static GlobalConstants getInstance()
    {
        if (single_instance == null)
            single_instance = new GlobalConstants(mContext);

        return single_instance;
    }

    public static void update(Context context){
        {
            int[] attr = {R.attr.colorPrimary};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_PRIMARY = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.colorPrimaryDark};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_PRIMARY_DARK = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.colorAccent};
            TypedArray ta = context.obtainStyledAttributes(attr);
            COLOR_ACENT = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
        {
            int[] attr = {R.attr.editTextColor};
            TypedArray ta = context.obtainStyledAttributes(attr);
            EDIT_TEXT_COLOR = ta.getResourceId(0, android.R.color.holo_red_light);
            ta.recycle();
        }
    }


}
