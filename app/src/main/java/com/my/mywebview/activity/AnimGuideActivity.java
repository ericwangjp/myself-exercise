package com.my.mywebview.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.my.mywebview.R;
import com.my.mywebview.adapter.BaseFragmentAdapter;
import com.my.mywebview.base.CommonBaseActivity;
import com.my.mywebview.fragment.LauncherBaseFragment;
import com.my.mywebview.fragment.PrivateMessageLauncherFragment;
import com.my.mywebview.fragment.RewardLauncherFragment;
import com.my.mywebview.fragment.StereoscopicLauncherFragment;
import com.my.mywebview.view.GuideViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现引导页动画
 * 经测试此种写法会有OOM导致崩溃的风险需优化
 */
public class AnimGuideActivity extends CommonBaseActivity {
    private GuideViewPager vPager;
    private List<LauncherBaseFragment> list = new ArrayList<LauncherBaseFragment>();
    private BaseFragmentAdapter adapter;
    private ImageView[] tips;
    private int currentSelect;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        super.initContentView(savedInstanceState);
        setContentView(R.layout.activity_anim_guide);
    }

    @Override
    protected void initData() {
        super.initData();
        //初始化点点点控件
        ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
        tips = new ImageView[3];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            tips[i]=imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            group.addView(imageView,layoutParams);
        }
        //获取自定义viewpager 然后设置背景图片
        vPager = (GuideViewPager) findViewById(R.id.viewpager_launcher);
        vPager.setBackGroud(BitmapFactory.decodeResource(getResources(),R.drawable.bg_kaka_launcher));
        /**
         * 初始化三个fragment  并且添加到list中
         */
        RewardLauncherFragment rewardFragment = new RewardLauncherFragment();
        PrivateMessageLauncherFragment privateFragment = new PrivateMessageLauncherFragment();
        StereoscopicLauncherFragment stereoscopicFragment = new StereoscopicLauncherFragment();
        list.add(rewardFragment);
        list.add(privateFragment);
        list.add(stereoscopicFragment);

        adapter = new BaseFragmentAdapter(getSupportFragmentManager(),list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);//此处设置viewpager的预加载页面为2页，默认预加载为1页
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(changeListener);
    }
    /**
     * 监听viewpager的移动
     */
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            setImageBackground(index);//改变点点点的切换效果
            LauncherBaseFragment fragment=list.get(index);

            list.get(currentSelect).stopAnimation();//停止前一个页面的动画
            fragment.startAnimation();//开启当前页面的动画

            currentSelect=index;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };
    /**
     * 改变点点点的切换效果
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
}
