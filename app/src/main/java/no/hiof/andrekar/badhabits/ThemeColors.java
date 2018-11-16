package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;

import org.w3c.dom.Attr;

import java.security.KeyStore;
import java.util.jar.Attributes;

public class ThemeColors {

    public static int COLOR_PRIMARY, COLOR_PRIMARY_DARK, COLOR_ACENT, WINDOW_BACKGROUND, PRIMARY_TEXT_COLOR_BLACK;

    private static ThemeColors single_instance = null;
    private static Context mContext;

    private ThemeColors(Context context) {
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
    }

    public static ThemeColors getInstance()
    {
        if (single_instance == null)
            single_instance = new ThemeColors(mContext);

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
    }


}
