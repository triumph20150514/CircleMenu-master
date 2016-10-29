package com.hitomi.circlemenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitomi.circlemenu.transtion.FabTransform;
import com.hitomi.circlemenu.transtion.MorphTransform;

/**
 * Created by tao on 2016/10/27.
 */

public class Page2Activity extends Activity {
    private android.widget.TextView tv;
    private android.widget.LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_two);
        this.container = (LinearLayout) findViewById(R.id.container);
        this.tv = (TextView) findViewById(R.id.tv);

        if (!FabTransform.setup(this, container)) {
            MorphTransform.setup(this, container,
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    getResources().getDimensionPixelSize(R.dimen.dialog_size));
        }

    }
}
