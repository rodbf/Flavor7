package com.flavorplus.diet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.R;
import com.flavorplus.databinding.DietFragmentBinding;
import com.flavorplus.util.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class DietFragment extends Fragment{

    private DietFragmentBinding binding;
    private DietViewModel viewModel;
    private RestrictionItemAdapter restrictionsAdapter;
    private ArrayList<AppCompatRadioButton> radioButtons = new ArrayList<>();
    private Observer<ArrayList<Restriction>> restrictionsListObserver;

    @Override
    public void onDestroy() {
        viewModel.restrictionsList.removeObserver(restrictionsListObserver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.restrictionsList.observe(requireActivity(), restrictionsListObserver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DietFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DietViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstance) {

        radioButtons.add(binding.rbAll);
        radioButtons.add(binding.rbVegan);
        radioButtons.add(binding.rbLactoovo);
        radioButtons.add(binding.rbVegetarian);

        for(AppCompatRadioButton button : radioButtons){
            button.setButtonTintList(ContextCompat.getColorStateList(requireActivity(), R.color.radio_color_list));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(AppCompatRadioButton b : radioButtons){
                        b.setChecked(false);
                    }
                    int diet;
                    switch(view.getId()){
                        case R.id.rb_vegetarian:
                            diet = Diet.VEGETARIAN;
                            break;
                        case R.id.rb_vegan:
                            diet = Diet.VEGAN;
                            break;
                        case R.id.rb_lactoovo:
                            diet = Diet.OVOLACTOVEGETARIAN;
                            break;
                        default:
                            diet = Diet.ALL;
                    }
                    viewModel.updateDiet(diet);
                }
            });
        }

        viewModel.diet.observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                Log.d("debugging", "fragment diet " + integer);
                for(int i = 0; i < radioButtons.size(); i++)
                    radioButtons.get(i).setChecked(false);
                radioButtons.get(integer).setChecked(true);
            }
        });

        RecyclerView restrictionsRecycler = binding.rvRestrictions;
        restrictionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        restrictionsAdapter = new RestrictionItemAdapter(viewModel);
        restrictionsRecycler.setAdapter(restrictionsAdapter);
        restrictionsRecycler.addItemDecoration(new SimpleDividerItemDecoration(requireContext()));

        binding.btAddRestriction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restriction restriction = (Restriction) binding.spRestriction.getSelectedItem();
                if(!viewModel.addRestriction(new Restriction(restriction.getId(), restriction.getName())))
                    Toast.makeText(requireContext(), "Restrição já adicionada", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.restrictions.observe(this.requireActivity(), new Observer<ArrayList<Restriction>>() {
            @Override
            public void onChanged(ArrayList<Restriction> restrictions) {
                restrictionsAdapter.setRestrictionList(restrictions);
            }
        });

        restrictionsListObserver = new Observer<ArrayList<Restriction>>() {
            @Override
            public void onChanged(ArrayList<Restriction> restrictions) {
                ArrayAdapter<Restriction> restrictionSpinnerAdapter = new ArrayAdapter<>(requireActivity(), R.layout.generic_spinner_item, restrictions);
                Spinner restrictionSpinner = binding.spRestriction;
                restrictionSpinner.setAdapter(restrictionSpinnerAdapter);
            }
        };
        viewModel.init();
    }
}

