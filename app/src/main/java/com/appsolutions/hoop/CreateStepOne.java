package com.appsolutions.hoop;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentCreateGameMainBinding;
import com.appsolutions.databinding.FragmentCreateGameStep1Binding;
import com.appsolutions.interfaces.CreateGameInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class CreateStepOne extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentCreateGameStep1Binding binding;
    private CreateGameViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private CreateGameInterface step;
    private Map<String, Object> map;

    @Inject
    public CreateStepOne() {
        //Required empty public constructor
    }

    public static CreateStepOne getInstance() {
        return new CreateStepOne();
    }
//
//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//        try{
//            CreateGameInterface step = (CreateGameInterface) getActivity();
//        }
//        catch(ClassCastException e){
//            throw e;
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CreateGameViewModel.class);
        lifecycleOwner = this;

        ArrayAdapter<String> gameAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, gameModes());
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, genderSelects());

        binding.createGameGameMode.setAdapter(gameAdapter);
        binding.createGameGender.setAdapter(genderAdapter);
        binding.createGamePointSelector.setMax(30);
        binding.createGamePointSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                binding.createGamePointText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.createGameMinAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(makeWeight());

                AlertDialog.Builder height = new AlertDialog.Builder(getContext());
                height.setTitle("Enter min Age: ");
                height.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.createGameMinAge.setText(adapter.getItem(which));
                    }
                });

                height.create().show();
            }
        });

        binding.createGameMaxAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(makeWeight());

                AlertDialog.Builder height = new AlertDialog.Builder(getContext());
                height.setTitle("Enter max Age: ");
                height.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.createGameMaxAge.setText(adapter.getItem(which));
                    }
                });

                height.create().show();
            }
        });

        binding.setViewModelCreateGame(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_create_game_step1, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    private String[] gameModes(){
        String[] games = {"21", "7", "1 V 1", "2 V 2", "3 V 3", "Competitive 1 V 1",
                "Competitive 2 V 2", "Competitive 3 V 3", "Competitive 4 V 4", "Competitive 5 V 5"};

        return games;
    }

    private String[] genderSelects(){
        String[] genders = {"Male", "Female", "Both"};

        return genders;
    }

    private ArrayList<String> makeWeight(){
        ArrayList<String> weight = new ArrayList<>();

        for(int i = 16; i <= 99; i++){
            weight.add(String.valueOf(i));
        }

        return weight;
    }

//    void getData(){
//        step.stepOne(map);
//    }
}

