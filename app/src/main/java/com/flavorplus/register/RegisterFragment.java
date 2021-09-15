package com.flavorplus.register;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.databinding.RegisterFragmentBinding;
import com.flavorplus.util.LoadingDialog;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private RegisterFragmentBinding binding;
    private RegisterViewModel viewModel;
    private NavigationViewModel navigationViewModel;

    private LoadingDialog loading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RegisterFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance){
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     viewModel.register();
                 }
            }
        );

        binding.etPasswordConfirm.setOnEditorActionListener(new TextInputEditText.OnEditorActionListener() {
                 @Override
                 public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                     boolean handled = false;
                     if(i == EditorInfo.IME_ACTION_DONE) {
                         viewModel.register();
                         handled = true;
                     }
                     return handled;
                 }
             }
        );

        loading = new LoadingDialog(requireActivity());
        viewModel.isRegistering.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggingIn) {
                if(isLoggingIn)
                    loading.show();
                else
                    if(viewModel.processResponse())
                        navigationViewModel.setScreen(NavigationViewModel.Screens.HOME);
                    loading.dismiss();
            }
        });
    }
}

