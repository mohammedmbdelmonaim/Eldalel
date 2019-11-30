package com.zeidex.eldalel.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zeidex.eldalel.AccountFragment;
import com.zeidex.eldalel.CategoriesFragment;
import com.zeidex.eldalel.OffersFragment;
import com.zeidex.eldalel.hostfragments.AccountHostFragment;
import com.zeidex.eldalel.hostfragments.BasketHostFragment;
import com.zeidex.eldalel.hostfragments.MainHostFragment;

import java.util.ArrayList;
import java.util.List;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragmentList;
    FragmentManager mFragmentManager;
//    FirstPageListener listener = new FirstPageListener();
    private Fragment mFragmentAtPos0;
    MainHostFragment mMainFragment;
    AccountFragment mAccountFragment;
    BasketHostFragment mBasketFragment;
    CategoriesFragment mCategoriesFragment;
    OffersFragment mOffersFragment;
    AccountHostFragment mAccountAfterLoginHostFragment;

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mFragmentList = new ArrayList<>();
        mFragmentManager = fm;
        mMainFragment = new MainHostFragment();

        mAccountFragment = new AccountFragment();
        mBasketFragment = new BasketHostFragment();
        mCategoriesFragment = new CategoriesFragment();
        mOffersFragment = new OffersFragment();
        mAccountAfterLoginHostFragment = new AccountHostFragment();
        addFragment(mMainFragment);
        addFragment(mCategoriesFragment);
        addFragment(mOffersFragment);
        addFragment(mBasketFragment);
        addFragment(mAccountFragment);
        addFragment(mAccountAfterLoginHostFragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
            return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

//    private final class FirstPageListener implements FirstPageFragmentListener {
//        public void onSwitchToNextFragment(Bundle bundle) {
//            mFragmentManager.beginTransaction().remove(mFragmentAtPos0)
//                    .commitNow();
//            if (mFragmentAtPos0 instanceof MainFragment) {
//                mFragmentAtPos0 = new DetailItemFragment();
//            } else { // Instance of NextFragment
//                mFragmentAtPos0 = new MainFragment(listener);
//            }
//            notifyDataSetChanged();
//        }
//    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        if (object instanceof MainFragment &&
//                mFragmentAtPos0 instanceof DetailItemFragment) {
//            return POSITION_NONE;
//        }
//        if (object instanceof DetailItemFragment &&
//                mFragmentAtPos0 instanceof MainFragment) {
//            return POSITION_NONE;
//        }
//        return POSITION_UNCHANGED;
//    }

//    public boolean onBackPressed() {
//        if (mFragmentAtPos0 instanceof DetailItemFragment) {
//            mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commitNow();
//            mFragmentAtPos0 = new MainFragment(listener);
//            notifyDataSetChanged();
//            return true;
//        }
//        return false;
//    }
}
