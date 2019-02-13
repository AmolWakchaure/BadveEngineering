package engineering.badve.badveengineering.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by snsystem_amol on 06/07/2017.
 */

public class MyAutocompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public MyAutocompleteTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyAutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyAutocompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "FallingSky.otf");
        setTypeface(tf);
    }
}