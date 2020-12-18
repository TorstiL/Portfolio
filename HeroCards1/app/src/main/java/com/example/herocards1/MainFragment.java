package com.example.herocards1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.text.InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE;

public class MainFragment extends Fragment {

    private RecyclerView factionRecyclerView;
    private FactionModelListAdapter mfactionModelListAdapter;
    private CharacterViewModel mCharacterViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mCharacterViewModel = new ViewModelProvider(getActivity()).get(CharacterViewModel.class);
        mfactionModelListAdapter = new FactionModelListAdapter(inflater.getContext(), mCharacterViewModel);
        mCharacterViewModel.getAllFactionsWithCharacters().observe(getViewLifecycleOwner(), factionsWithCharacters -> {
            mfactionModelListAdapter.setFactionDataSet(factionsWithCharacters);
        });

        factionRecyclerView = v.findViewById(R.id.faction_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        factionRecyclerView.setLayoutManager(llm);
        factionRecyclerView.setAdapter(mfactionModelListAdapter);

        v.findViewById(R.id.fragment_main_button_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog requestInput = createDialog();
                requestInput.show();
            }
        });
        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Dialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set dialog content
        builder.setTitle("New Faction Name");
        EditText inputFactionName = new EditText(getActivity());
        inputFactionName.setInputType(TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        builder.setView(inputFactionName);
        //set dialog positive button
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mCharacterViewModel.insertFaction(new FactionModel(getFieldDataAsString(inputFactionName)));
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