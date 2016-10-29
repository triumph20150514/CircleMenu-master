package com.hitomi.circlemenu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hitomi.circlemenu.transtion.FabTransform;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private CircleMenu circleMenu;

    private int[] iconResArray = new int[5];
    private CircleMenu circlemenu;
    private com.hitomi.circlemenu.CircleMenu circle;
    private ListView recyclerView;
    private android.widget.ImageButton fab;

    {
        iconResArray[0] = R.mipmap.icon_home;
        iconResArray[1] = R.mipmap.icon_search;
        iconResArray[2] = R.mipmap.icon_notify;
        iconResArray[3] = R.mipmap.icon_setting;
        iconResArray[4] = R.mipmap.icon_gps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fab = (ImageButton) findViewById(R.id.fab);
        this.recyclerView = (ListView) findViewById(R.id.recyclerView);
        this.circle = (com.hitomi.circlemenu.CircleMenu) findViewById(R.id.circle);
        this.circlemenu = (CircleMenu) findViewById(R.id.circle_menu);


        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        // 设置打开/关闭菜单图标
        circleMenu.setMainIconResource(R.mipmap.icon_menu, R.mipmap.icon_cancel);
        // 设置一组 Resource 格式的子菜单项图
        circleMenu.setSubIconResources(iconResArray);
        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int index) {
            }
        });
        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {
            }
        });


        ListView listView = (ListView) findViewById(R.id.recyclerView);

        List<Map<String, String>> mapList = new ArrayList<>();
        String[] strings = new String[]{"英語", "法語", "法語", "法語", "法語",
                "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語",
                "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語",
                "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語",
                "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語",
                "法語", "法語",
                "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語", "法語"};
        for (int i = 0; i < strings.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("The", strings[i]);
            mapList.add(map);
        }
        listView.setAdapter(new SimpleAdapter(this, mapList, R.layout.list_item, new String[]{"The"}, new int[]{R.id.text}));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Page2Activity.class);
                FabTransform.addExtras(intent,
                        ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), R.drawable.ic_cloud_black_24dp);
                intent.putExtra("result", true);
//        registerPostStoryResultListener();
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, fab,
                        getString(R.string.transition_designer_news_login));
                startActivityForResult(intent, 5, options.toBundle());
            }
        });
    }
}
