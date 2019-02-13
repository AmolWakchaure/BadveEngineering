package engineering.badve.badveengineering.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by snsystem_amol on 28/06/2017.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    public MyTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "FallingSky.otf");
        setTypeface(tf);
    }
}
