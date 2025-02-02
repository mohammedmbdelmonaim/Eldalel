package com.zeidex.eldalel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.zeidex.eldalel.utils.Animatoo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {
    @BindView(R.id.fragment_account_login_linear)
    LinearLayoutCompat fragment_account_login_linear;

    @BindView(R.id.fragment_account_Register_linear)
    LinearLayoutCompat fragment_account_Register_linear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        findViews();
    }

    public void findViews(){
        Fragment fragment = new FragmentAccountHelpers();
        getFragmentManager().beginTransaction().replace(R.id.fragment_account_getHelers , fragment).commit();

        fragment_account_login_linear.setOnClickListener(this);
        fragment_account_Register_linear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fragment_account_login_linear:{
                startActivity(new Intent(getActivity() , LoginActivity.class));
                Animatoo.animateSwipeLeft(getActivity());
                break;
            }
            case R.id.fragment_account_Register_linear:{
                startActivity(new Intent(getActivity() , RegisterActivity.class));
                Animatoo.animateSwipeLeft(getActivity());
                break;
            }
        }
    }
}
