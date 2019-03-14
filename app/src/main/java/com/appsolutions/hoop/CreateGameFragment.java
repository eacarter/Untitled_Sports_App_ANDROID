package com.appsolutions.hoop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentCreateGameMainBinding;
import com.appsolutions.interfaces.CreateGameInterface;
import com.appsolutions.manager.UserManager;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import dagger.android.support.DaggerFragment;

public class CreateGameFragment extends DaggerFragment implements ViewPager.OnPageChangeListener,
View.OnClickListener{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    private FragmentCreateGameMainBinding binding;
    private CreateGameViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private FragmentStatePagerAdapter adapter;
    private Map<String, Object> maps;

    @Inject
    public CreateGameFragment() {
        //Required empty public constructor
    }

    public static CreateGameFragment getInstance() {
        return new CreateGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maps = new HashMap<>();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CreateGameViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelCreateGame(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_create_game_main, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        adapter = new CreateGameAdapter(getChildFragmentManager());
        binding.createViewPager.setAdapter(adapter);
        binding.createViewPager.addOnPageChangeListener(this);
        binding.createMainNext.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//        View view = binding.createViewPager.getChildAt(position);

        if(position == 0) {
            View view = binding.createViewPager.getChildAt(0);
            EditText title = (EditText) view.findViewById(R.id.create_game_title);
            Spinner spinner = (Spinner) view.findViewById(R.id.create_game_game_mode);
            TextView points = (TextView) view.findViewById(R.id.create_game_point_text);
            EditText minAge = (EditText) view.findViewById(R.id.create_game_min_age);
            EditText maxAge = (EditText) view.findViewById(R.id.create_game_max_age);
            Spinner gender = (Spinner) view.findViewById(R.id.create_game_gender);

            maps.put("id", userManager.getUser().getValue().getUid()+""+randomGenerator());
            maps.put("title", title.getText().toString());
            maps.put("game_mode", spinner.getSelectedItem().toString());
            maps.put("points", points.getText().toString());
            maps.put("min_age", minAge.getText().toString());
            maps.put("max_age", maxAge.getText().toString());
            maps.put("gender", gender.getSelectedItem().toString());
        }
        if(position == 1){
            View view = binding.createViewPager.getChildAt(1);
            TextView privacy = (TextView) view.findViewById(R.id.create_game_pp_selector);
            TextView date =  (TextView) view.findViewById(R.id.create_game_date_selector);
            TextView time =  (TextView) view.findViewById(R.id.create_game_time_selector);
            TextView zip =  (TextView) view.findViewById(R.id.create_game_zip_selector);
            TextView location =  (TextView) view.findViewById(R.id.create_game_location_selector);

            maps.put("privacy", privacy.getText().toString());
            maps.put("game_date", date.getText().toString());
            maps.put("game_time", time.getText().toString());
            maps.put("game_zip", zip.getText().toString());
            maps.put("game_location", location.getText().toString());
        }
    }

    @Override
    public void onPageSelected(int position) {
//        View view = binding.createViewPager.getChildAt(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.createMainNext.getId()){
            binding.createViewPager.setCurrentItem(binding.createViewPager.getCurrentItem() + 1, true);
        }
    }

    private String randomGenerator(){
        String[] latter = {"a","b","c","d","e","f","g","h","i","j","k","l","m",
                "n","o","p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7"
                ,"8","9","0"};

        StringBuilder cat = new StringBuilder();

        for(int i = 0; i < 6; i++){
            cat.append(latter[new Random().nextInt(latter.length)]);
        }
        return cat.toString();
    }

//    @Override
//    public void stepOne(Map<String, Object> map) {
//
//    }
//
//    @Override
//    public void stepTwo(Map<String, Object> map) {
//
//    }
//
//    @Override
//    public void stepThree(Map<String, Object> map) {
//
//    }
}
