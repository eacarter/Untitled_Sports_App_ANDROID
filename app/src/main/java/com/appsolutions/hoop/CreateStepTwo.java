package com.appsolutions.hoop;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentCreateGameMainBinding;
import com.appsolutions.databinding.FragmentCreateGameStep2Binding;
import com.appsolutions.manager.UserManager;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class CreateStepTwo extends DaggerFragment implements View.OnClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    private FragmentCreateGameStep2Binding binding;
    private CreateGameViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public CreateStepTwo() {
        //Required empty public constructor
    }

    public static CreateStepTwo getInstance() {
        return new CreateStepTwo();
    }

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

        binding.createGamePpSelector.setOnClickListener(this);
        binding.createGameDateSelector.setOnClickListener(this);
        binding.createGameTimeSelector.setOnClickListener(this);
        binding.createGameLocationSelector.setOnClickListener(this);
        binding.createGameZipSelector.setOnClickListener(this);


        binding.setViewModelCreateGame(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_create_game_step2, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    private void Selections(String[] select, TextView textSetter){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
        adapter.addAll(select);

        AlertDialog.Builder height = new AlertDialog.Builder(getContext());
        height.setTitle("Please Select: ");
        height.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textSetter.setText(adapter.getItem(which));
            }
        });

        height.create().show();
    }

    private void DateSelector(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();
    }

    private void TimeSelector(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timeDialog = new TimePickerDialog(getContext(), timeSetListener, hour, minute, true);
        timeDialog.setTitle("Select Time: ");
        timeDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.createGamePpSelector.getId()){
            Selections(privacy(), binding.createGamePpSelector);
        }
        else if(v.getId() == binding.createGameDateSelector.getId()){
            DateSelector();
        }
        else if(v.getId() == binding.createGameTimeSelector.getId()){
            TimeSelector();
        }
    }

    private String[] privacy(){
        String[] privacy = {"Public", "Private"};
        return privacy;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("MMM dd YYYY").format(c.getTime());
            binding.createGameDateSelector.setText(format);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            binding.createGameTimeSelector.setText(hourOfDay+":"+minute);
        }
    };


}

