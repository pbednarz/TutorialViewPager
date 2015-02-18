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

import com.viewpagerindicator.CirclePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.solaris.tutorialviewpager.view.SmoothLinearLayout;
import pl.solaris.tutorialviewpager.view.ViewPagerCustomDuration;


public class TutorialActivity extends ActionBarActivity {

    private static final int colorArray[] = {Color.rgb(0, 187, 211),
            Color.rgb(239, 108, 0),
            Color.rgb(52, 172, 113)
    };
    @InjectView(R.id.pager)
    ViewPagerCustomDuration viewPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator circleIndicator;
    @InjectView(R.id.next_btn)
    View nextBtn;
    @InjectView(R.id.skip_btn)
    View skipBtn;
    @InjectView(R.id.done_btn)
    View doneBtn;
    @InjectView(R.id.bg)
    SmoothLinearLayout bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.inject(this);
        bg.setColors(colorArray);
        viewPager.setScrollDurationFactor(1.6);
        viewPager.setOffscreenPageLimit(2);
        final TutorialPagerAdapter defaultPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(defaultPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int mScrollState;

            @Override
            public void onPageSelected(int position) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    bg.onViewPagerPageChanged(position, 0f);
                }
                switchSkipAndNextButton(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int tabStripChildCount = colorArray.length;
                if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                    return;
                }
                bg.onViewPagerPageChanged(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mScrollState = state;
            }

        });

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                if (position > -1 && position < 1) {
                    view.findViewById(R.id.image1).setTranslationX((float) (position * 0.2 * pageWidth));
                    view.findViewById(R.id.image2).setTranslationX((float) (position * 0.3 * pageWidth));
                    view.findViewById(R.id.image3).setTranslationX((float) (position * 0.5 * pageWidth));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @OnClick({R.id.skip_btn, R.id.done_btn})
    public void skipClicked() {
        startActivity(new Intent(this, TutorialActivity.class));
        finish();
    }

    @OnClick(R.id.next_btn)
    public void nextClicked() {
        if (viewPager.getCurrentItem() < colorArray.length) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
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
