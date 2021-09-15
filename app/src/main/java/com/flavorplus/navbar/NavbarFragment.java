package com.flavorplus.navbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.NavbarFragmentBinding;
import com.flavorplus.navbar.unithelper.UnitHelperDialogFragment;

import java.util.Objects;

public class NavbarFragment extends PreferenceFragmentCompat implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private NavbarViewModel viewModel;
    private NavigationViewModel navigationViewModel;
    private NavbarFragmentBinding binding;
    private SharedPreferences userData;
    private boolean isLoggedIn;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NavbarFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NavbarViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.btScale.setOnClickListener(this);
        binding.btProfileMenu.setOnClickListener(this);
        binding.btMenu.setOnClickListener(this);
        binding.navbarTitle.setOnClickListener(this);
        userData = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        updateLoginState();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){

    }

    private void btScale(){
        FragmentManager fm = getParentFragmentManager();
        UnitHelperDialogFragment unitHelper = new UnitHelperDialogFragment();
        unitHelper.show(fm, "unit_helper_dialog");
    }

    private void btProfile(View v){
        PopupMenu popup = new PopupMenu(this.requireContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.profile_menu, menu);
        if(isLoggedIn){
            menu.findItem(R.id.mi_profile).setVisible(true);
            menu.findItem(R.id.mi_logout).setVisible(true);
            menu.findItem(R.id.mi_diet).setVisible(true);
            menu.findItem(R.id.mi_favorites).setVisible(true);
            menu.findItem(R.id.mi_new_recipe).setVisible(false);
            menu.findItem(R.id.mi_login).setVisible(false);
        }
        else{
            menu.findItem(R.id.mi_profile).setVisible(false);
            menu.findItem(R.id.mi_logout).setVisible(false);
            menu.findItem(R.id.mi_diet).setVisible(false);
            menu.findItem(R.id.mi_favorites).setVisible(false);
            menu.findItem(R.id.mi_new_recipe).setVisible(false);
            menu.findItem(R.id.mi_login).setVisible(true);
        }
        popup.show();
    }

    private void btMenu(View v){
        PopupMenu popup = new PopupMenu(this.requireContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        Menu menu = popup.getMenu();
menu.findItem(R.id.mi_config).setVisible(false);
        if(isLoggedIn){
            menu.findItem(R.id.mi_support).setVisible(true);
            menu.findItem(R.id.mi_suggestion).setVisible(true);
        }
        else{
            menu.findItem(R.id.mi_support).setVisible(false);
            menu.findItem(R.id.mi_suggestion).setVisible(false);
        }
        popup.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_scale:
                btScale();
                break;
            case R.id.bt_profile_menu:
                btProfile(v);
                break;
            case R.id.bt_menu:
                btMenu(v);
                break;
            case R.id.navbar_title:
                navigationViewModel.setScreen(NavigationViewModel.Screens.HOME);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mi_favorites:
                navigationViewModel.setScreen(NavigationViewModel.Screens.LIBRARY);
                return true;
            case R.id.mi_logout:
                SharedPreferences.Editor editor = userData.edit();
                editor.remove("jwt");
                editor.apply();
                navigationViewModel.setScreen(NavigationViewModel.Screens.HOME);
                return true;
            case R.id.mi_login:
                navigationViewModel.setScreen(NavigationViewModel.Screens.LOGIN);
                return true;
            case R.id.mi_profile:
                navigationViewModel.setScreen(NavigationViewModel.Screens.PROFILE);
                return true;
            case R.id.mi_diet:
                navigationViewModel.setScreen(NavigationViewModel.Screens.DIET);
                return true;
            case R.id.mi_support:
                navigationViewModel.setScreen(NavigationViewModel.Screens.SUPPORT_LANDING);
                return true;
            case R.id.mi_suggestion:
                navigationViewModel.setScreen(NavigationViewModel.Screens.SUGGESTIONS);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("jwt")){
            updateLoginState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userData.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        userData.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void updateLoginState(){
        isLoggedIn = !Objects.equals(userData.getString("jwt", ""), "");
        if(isLoggedIn){
            binding.btProfileMenu.setColorFilter(ContextCompat.getColor(requireContext(), R.color.accent), PorterDuff.Mode.SRC_IN);
        }
        else{
            binding.btProfileMenu.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorTitleText), PorterDuff.Mode.SRC_IN);
        }
    }
}
