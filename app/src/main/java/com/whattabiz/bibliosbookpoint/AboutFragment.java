package com.whattabiz.bibliosbookpoint;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * This Fragment Shows ABOUT information of The App / Whattabiz
 */
public class AboutFragment extends Fragment {

    private int count = 0;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getActivity().setTitle("About");

        // easter egg


        ImageView wImageView = (ImageView) view.findViewById(R.id.whattabiz_logo);
        wImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 15;
                count++;
                if (count >= 10) {
                    if (count >= 15) {
                        Toast.makeText(v.getContext(), "Tiger Mode Enabled!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "You're " + (i - count) + " steps away from tiger mode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
