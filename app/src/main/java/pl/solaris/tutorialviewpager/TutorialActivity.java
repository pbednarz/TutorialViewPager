package pl.solaris.tutorialviewpager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.solaris.tutorialviewpager.view.CircleIndicator;


public class TutorialActivity extends ActionBarActivity {

    private static final int colorArray[] = {Color.rgb(0, 187, 211),
            Color.rgb(255, 167, 37),
            Color.rgb(52, 172, 113)
    };
    @InjectView(R.id.pager)
    ViewPager viewPager;
    @InjectView(R.id.root)
    View rlRoot;
    @InjectView(R.id.indicator)
    CircleIndicator circleIndicator;
    @InjectView(R.id.next_btn)
    View nextBtn;
    @InjectView(R.id.skip_btn)
    View skipBtn;
    @InjectView(R.id.done_btn)
    View doneBtn;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.inject(this);
        rlRoot.setBackgroundColor(colorArray[0]);
        viewPager.setOffscreenPageLimit(2);
        final TutorialPagerAdapter defaultPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(defaultPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switchSkipAndNextButton(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                time++;
                if (positionOffset == 0) {
                    rlRoot.setBackgroundColor(colorArray[position]);
                } else if (time % 3 == 0) {
                    rlRoot.setBackgroundColor(blendColors(colorArray[position + 1], colorArray[position], positionOffset));
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                if (position > -1 && position < 1) {
                    view.findViewById(R.id.image1).setTranslationX((float) (position * 0.1 * pageWidth));
                    view.findViewById(R.id.image2).setTranslationX((float) (position * 0.2 * pageWidth));
                    view.findViewById(R.id.image3).setTranslationX((float) (position * 0.5 * pageWidth));
                }
            }
        });
    }

    @OnClick({R.id.skip_btn, R.id.done_btn})
    public void skipClicked() {
        startActivity(new Intent(this, this.getClass()));
        finish();
    }

    @OnClick(R.id.next_btn)
    public void nextClicked() {
        if (viewPager.getCurrentItem() < colorArray.length) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    private int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        final int r = (int) ((((color1 >> 16) & 0xFF) * ratio) + (((color2 >> 16) & 0xFF) * inverseRation));
        final int g = (int) ((((color1 >> 8) & 0xFF) * ratio) + (((color2 >> 8) & 0xFF) * inverseRation));
        final int b = (int) (((color1 & 0xFF) * ratio) + ((color2 & 0xFF) * inverseRation));
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public void switchSkipAndNextButton(int position) {
        if (skipBtn.getVisibility() == View.VISIBLE && position == colorArray.length - 1) {
            doneBtn.setVisibility(View.VISIBLE);
            skipBtn.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
        } else if (position <= colorArray.length && skipBtn.getVisibility() == View.INVISIBLE) {
            skipBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
        }
    }

    public class TutorialPagerAdapter extends FragmentPagerAdapter {

        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return TutorialFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return colorArray.length;
        }
    }
}
