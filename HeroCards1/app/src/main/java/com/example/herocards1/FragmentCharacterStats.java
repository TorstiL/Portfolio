package com.example.herocards1;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCharacterStats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCharacterStats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CharacterViewModel mCharacterViewModel;
    private TextView characterName;
    private TextView characterRank;
    private TextView characterWounds;
    private TextView characterRange;
    private TextView characterDamage;
    private TextView characterSpeed;
    private TextView characterDefence;
    private TextView characterAttacks;
    private ImageView characterImage;
    private String currentPhotoPath;


    public FragmentCharacterStats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCharacterStats.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCharacterStats newInstance(String param1, String param2) {
        FragmentCharacterStats fragment = new FragmentCharacterStats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_character_stats, container, false);
        mCharacterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        characterName = v.findViewById(R.id.stats_character_name);
        characterRank = v.findViewById(R.id.stats_character_rank);
        characterWounds = v.findViewById(R.id.stats_character_wounds);
        characterRange = v.findViewById(R.id.stats_character_range);
        characterDamage = v.findViewById(R.id.stats_character_damage);
        characterImage = v.findViewById(R.id.stats_character_imageView);
        characterSpeed = v.findViewById(R.id.stats_character_speed);
        characterDefence = v.findViewById(R.id.stats_character_defence);
        characterAttacks = v.findViewById(R.id.stats_character_attacks);

        if(mCharacterViewModel.getSelectedCharacter() != null){
            CharacterModel current = mCharacterViewModel.getSelectedCharacter();
            characterName.setText(current.getCharacterName());
            characterRank.setText(current.getCharacterRank());
            characterWounds.setText(String.valueOf(current.getWounds()));
            characterRange.setText(String.valueOf(current.getAttackRange()));
            characterDamage.setText(String.valueOf(current.getAttackDamage()));
            characterSpeed.setText(String.valueOf(current.getSpeed()));
            characterDefence.setText(String.valueOf(current.getDefence()));
            characterAttacks.setText(String.valueOf(current.getAttacks()));
            if(current.getCharacterImgPath() != null){
                currentPhotoPath = current.getCharacterImgPath();
                File f = new File(currentPhotoPath);
                characterImage.setImageURI(Uri.fromFile(f));
            }
        }
        v.findViewById(R.id.stats_button_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentCharacterStats.this)
                        .navigate(R.id.action_fragmentCharacterStats_to_fragmentEditCharacter);
            }
        });
        v.findViewById(R.id.stats_button_characters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentCharacterStats.this)
                        .navigate(R.id.action_fragmentCharacterStats_to_fragmentCharacterModel);
            }
        });
        return v;
    }

}