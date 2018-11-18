package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.content.res.TypedArray;

public class GlobalConstants {

    public static int COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACENT, EDIT_TEXT_COLOR, PRIMARY_TEXT_COLOR_BLACK;

    public final static String CHANNEL_ID = "CHANNEL_ID";

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
