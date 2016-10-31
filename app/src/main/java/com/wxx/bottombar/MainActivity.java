package com.wxx.bottombar;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private MainHomePageFragment mMainHomePageFragment;
    private MainChatPageFragment mMainChatPageFragment;
    private MainSquarePageFragment mMainSquarePageFragment;
    private MainDiscoverPageFragment mMainDiscoverPageFragment;
    private MainMinePageFragment mMainMinePageFragment;

    private int mCurrentFragmentIndex = 0;
    private boolean mHomeFragmentAdded = false;
    private boolean mChatFragmentAdded = false;
    private boolean mPlatformFragmentAdded = false;
    private boolean mDiscoverFragmentAdded = false;
    private boolean mMeFragmentAdded = false;

    private static final int BOTTOM_ITEM_TITLE_HOME_INDEX = 0;
    private static final int BOTTOM_ITEM_TITLE_CHAT_INDEX = 1;
    private static final int BOTTOM_ITEM_TITLE_SQUARE_INDEX = 2;
    private static final int BOTTOM_ITEM_TITLE_DISCOVER_INDEX = 3;
    private static final int BOTTOM_ITEM_TITLE_ME_INDEX = 4;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomBar();
    }

    private void initBottomBar() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        List<BottomBarTab> bottomBarItems = new ArrayList<>();

        BottomBarTab homeTab = new BottomBarTab(this, R.drawable.selector_rb_home, R.string.main_home);
        bottomBarItems.add(homeTab);

        BottomBarTab chatTab = new BottomBarTab(this, R.drawable.selector_rb_chat, R.string.main_chat);
        bottomBarItems.add(chatTab);

        BottomBarTab squareTab = new BottomBarTab(this, R.drawable.selector_rb_square, R.string.main_square);
        bottomBarItems.add(squareTab);

        BottomBarTab discoverTab = new BottomBarTab(this, R.drawable.selector_rb_discover, R.string.main_discover);
        bottomBarItems.add(discoverTab);

        BottomBarTab meTab = new BottomBarTab(this, R.drawable.selector_rb_mine, R.string.main_mine);
        bottomBarItems.add(meTab);

        mBottomBar.setItems(bottomBarItems);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        switchToFragment(BOTTOM_ITEM_TITLE_HOME_INDEX);
                        break;
                    case R.id.tab_chat:
                        switchToFragment(BOTTOM_ITEM_TITLE_CHAT_INDEX);
                        break;
                    case R.id.tab_square:
                        switchToFragment(BOTTOM_ITEM_TITLE_SQUARE_INDEX);
                        break;
                    case R.id.tab_discover:
                        switchToFragment(BOTTOM_ITEM_TITLE_DISCOVER_INDEX);
                        break;
                    case R.id.tab_me:
                        switchToFragment(BOTTOM_ITEM_TITLE_ME_INDEX);
                        break;
                }
            }

            @Override
            public void onBarSelected(int position) {
                switch (position) {
                    case BOTTOM_ITEM_TITLE_HOME_INDEX:
                        switchToFragment(BOTTOM_ITEM_TITLE_HOME_INDEX);
                        break;
                    case BOTTOM_ITEM_TITLE_CHAT_INDEX:
                        switchToFragment(BOTTOM_ITEM_TITLE_CHAT_INDEX);
                        break;
                    case BOTTOM_ITEM_TITLE_SQUARE_INDEX:
                        switchToFragment(BOTTOM_ITEM_TITLE_SQUARE_INDEX);
                        break;
                    case BOTTOM_ITEM_TITLE_DISCOVER_INDEX:
                        switchToFragment(BOTTOM_ITEM_TITLE_DISCOVER_INDEX);
                        break;
                    case BOTTOM_ITEM_TITLE_ME_INDEX:
                        switchToFragment(BOTTOM_ITEM_TITLE_ME_INDEX);
                        break;
                }
            }
        });
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int position) {
                switch (position) {
                    case BOTTOM_ITEM_TITLE_HOME_INDEX:
                        break;
                    case BOTTOM_ITEM_TITLE_CHAT_INDEX:
                        break;
                    case BOTTOM_ITEM_TITLE_SQUARE_INDEX:
                        break;
                    case BOTTOM_ITEM_TITLE_DISCOVER_INDEX:
                        break;
                    case BOTTOM_ITEM_TITLE_ME_INDEX:
                        break;
                }
            }
        });
    }

    private void switchToFragment(int i) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (mCurrentFragmentIndex == i) {
            fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
            fragmentTransaction.commitAllowingStateLoss();
            mCurrentFragmentIndex = i;
            if (i == BOTTOM_ITEM_TITLE_HOME_INDEX) {
                mHomeFragmentAdded = true;
            }
            return;
        }

        fragmentTransaction.hide(getFragment(mCurrentFragmentIndex));

        switch (i) {
            case BOTTOM_ITEM_TITLE_HOME_INDEX:
                if (mHomeFragmentAdded) {
                    fragmentTransaction.show(getFragment(i));
                } else {
                    fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
                    mHomeFragmentAdded = true;
                }
                break;
            case BOTTOM_ITEM_TITLE_CHAT_INDEX:
                if (mChatFragmentAdded) {
                    fragmentTransaction.show(getFragment(i));
                } else {
                    fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
                    mChatFragmentAdded = true;
                }
                break;
            case BOTTOM_ITEM_TITLE_SQUARE_INDEX:
                if (mPlatformFragmentAdded) {
                    fragmentTransaction.show(getFragment(i));
                } else {
                    fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
                    mPlatformFragmentAdded = true;
                }
                break;
            case BOTTOM_ITEM_TITLE_DISCOVER_INDEX:
                if (mDiscoverFragmentAdded) {
                    fragmentTransaction.show(getFragment(i));
                } else {
                    fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
                    mDiscoverFragmentAdded = true;
                }
                break;
            case BOTTOM_ITEM_TITLE_ME_INDEX:
                if (mMeFragmentAdded) {
                    fragmentTransaction.show(getFragment(i));
                } else {
                    fragmentTransaction.add(R.id.fragmentContainer, getFragment(i));
                    mMeFragmentAdded = true;
                }
                break;
            default:
                break;
        }

        fragmentTransaction.commitAllowingStateLoss();
        mCurrentFragmentIndex = i;
    }

    private Fragment getFragment(int menuItemId) {
        switch (menuItemId) {
            case BOTTOM_ITEM_TITLE_HOME_INDEX:
                if (mMainHomePageFragment == null) {
                    mMainHomePageFragment = new MainHomePageFragment();
                }
                return mMainHomePageFragment;
            case BOTTOM_ITEM_TITLE_CHAT_INDEX:
                if (mMainChatPageFragment == null) {
                    mMainChatPageFragment = new MainChatPageFragment();
                }
                return mMainChatPageFragment;
            case BOTTOM_ITEM_TITLE_SQUARE_INDEX:
                if (mMainSquarePageFragment == null) {
                    mMainSquarePageFragment = new MainSquarePageFragment();
                }
                return mMainSquarePageFragment;
            case BOTTOM_ITEM_TITLE_DISCOVER_INDEX:
                if (mMainDiscoverPageFragment == null) {
                    mMainDiscoverPageFragment = new MainDiscoverPageFragment();
                }
                return mMainDiscoverPageFragment;
            case BOTTOM_ITEM_TITLE_ME_INDEX:
                if (mMainMinePageFragment == null) {
                    mMainMinePageFragment = new MainMinePageFragment();
                }
                return mMainMinePageFragment;
            default:
                break;
        }
        return null;
    }

}
