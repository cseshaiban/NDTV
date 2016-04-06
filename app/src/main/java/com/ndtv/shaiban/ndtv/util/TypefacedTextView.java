package com.ndtv.shaiban.ndtv.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ndtv.shaiban.ndtv.R;


/**
 * Created by shaiban on 6/7/16.
 */
public class TypefacedTextView extends TextView {

    private CharSequence origText = "";

    public TypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            setTypeface(typeface);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        origText = text;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        CharSequence text = origText;
        //onPreDraw();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            while (getLineCount() > getMaxLines()) {
                if (text.length() > 5) {
                    text = text.subSequence(0, text.length() - 5);
                }
                super.setText(text + "...");
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                onPreDraw();
            }
        } else {
            onPreDraw();
        }

    }

}

