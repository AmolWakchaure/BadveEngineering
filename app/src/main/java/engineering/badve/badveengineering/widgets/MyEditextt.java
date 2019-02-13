package engineering.badve.badveengineering.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

/**
 * Created by snsystem_amol on 28/06/2017.
 */

public class MyEditextt extends ShowHidePasswordEditText {

    public MyEditextt(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyEditextt(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyEditextt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "FallingSky.otf");
        setTypeface(tf);
    }
}