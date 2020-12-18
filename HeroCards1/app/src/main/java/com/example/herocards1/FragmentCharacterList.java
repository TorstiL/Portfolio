package com.example.herocards1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static android.text.InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE;

/**
 * A fragment representing a list of Items.
 */
public class FragmentCharacterList extends Fragment {

    private RecyclerView recyclerView;
    private CharacterModelListAdapter mCharacterModelListAdapter;
    private CharacterViewModel characterViewModel;
    private TextView factionName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentCharacterList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);
        view.findViewById(R.id.character_list_button_factions).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(FragmentCharacterList.this)
                        .navigate(R.id.action_fragmentCharacterModel_to_mainFragment);
            }
        });
        view.findViewById(R.id.character_list_button_create).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(FragmentCharacterList.this)
                        .navigate(R.id.action_fragmentCharacterModel_to_fragmentNewCharacter);
            }
        });
        view.findViewById(R.id.character_list_button_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog requestInput = createDialog();
                requestInput.show();
            }
        });

        characterViewModel = new ViewModelProvider(getActivity()).get(CharacterViewModel.class);
        //Log.i("AUTO ", "Character List faction index: " + characterViewModel.getFactionIndex());
        mCharacterModelListAdapter = new CharacterModelListAdapter(inflater.getContext(), characterViewModel);
        characterViewModel.getAllFactionsWithCharacters().observe(getViewLifecycleOwner(), allFactionsWithAllCharacters -> {
            mCharacterModelListAdapter.setCharacterDataSet(allFactionsWithAllCharacters.get(characterViewModel.getFactionIndex()).getFactionCharacters());
        });
        factionName = view.findViewById(R.id.character_list_faction_name);
        factionName.setText(characterViewModel.getSelectedFaction().getFactionName());

        recyclerView = view.findViewById(R.id.character_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mCharacterModelListAdapter);

        return view;
    }

    private Dialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        FactionModel selectedFaction = characterViewModel.getSelectedFaction();
        //set dialog content
        builder.setTitle("Change Faction Name");
        EditText inputFactionName = new EditText(getActivity());
        inputFactionName.setInputType(TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        inputFactionName.setText(characterViewModel.getSelectedFaction().getFactionName());

        builder.setView(inputFactionName);
        //set dialog positive button
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                selectedFaction.setFactionName(getFieldDataAsString(inputFactionName));
                factionName.setText(getFieldDataAsString(inputFactionName));
                characterViewModel.updateFaction(selectedFaction);
            }
        });
        //set dialog negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Cancel button
            }
        });
        return builder.create();
    }
    private String getFieldDataAsString(EditText field){
        return field.getText().toString();
    }

}