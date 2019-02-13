package engineering.badve.badveengineering.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by snsystem_amol on 28/06/2017.
 */

public class MyEditext extends android.support.v7.widget.AppCompatEditText {

    public MyEditext(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyEditext(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "FallingSky.otf");
        setTypeface(tf);
    }
}
