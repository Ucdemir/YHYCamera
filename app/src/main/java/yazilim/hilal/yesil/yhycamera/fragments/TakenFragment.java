package yazilim.hilal.yesil.yhycamera.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import yazilim.hilal.yesil.yhycamera.R;
import yazilim.hilal.yesil.yhycamera.activity.MainActivity;
import yazilim.hilal.yesil.yhycamera.adapter.DataAdapter;
import yazilim.hilal.yesil.yhycamera.adapter.ViewPagerAdapter;
import yazilim.hilal.yesil.yhycamera.databinding.FragmentTakenBinding;

public class TakenFragment extends Fragment {


    private FragmentTakenBinding binding;

    private Context context;



    private int currentItem;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_taken, container, false);
        View view = binding.getRoot();


        ViewPagerAdapter adapter = new ViewPagerAdapter(getMainActivity());

        binding.viewPager.setAdapter(adapter);

        if(getArguments() != null){
            int currentItem = getArguments().getInt("currentItem");


            binding.viewPager.setCurrentItem(currentItem);

            binding.pageIndicator.setCount(DataAdapter.list.size());
            binding.pageIndicator.setSelection(currentItem);


        }







        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.pageIndicator.setSelection(position);


            }
        });



        binding.btnReturn.setOnClickListener(v->{

            Log.d("TAG","aaa");
        });
        return view;
    }


    public MainActivity getMainActivity(){

        return (MainActivity) context;
    }


}
