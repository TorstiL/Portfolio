package com.example.herocards1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FactionModelListAdapter extends RecyclerView.Adapter<FactionModelListAdapter.FactionViewHolder> {

    public static class FactionViewHolder extends RecyclerView.ViewHolder {
        private final TextView factionNameItemView;

        private FactionViewHolder(View itemView) {
            super(itemView);
            factionNameItemView = itemView.findViewById(R.id.faction_name);

        }

        public TextView getFactionNameItemView() {
            return factionNameItemView;
        }

    }

    private final LayoutInflater mInflater;
    private List<DataArmyWithCharacters> factionsDataSet;
    private CharacterViewModel mCharacterViewModel;
    private FactionModel currentFaction;

    public FactionModelListAdapter(Context context, CharacterViewModel mCharacterViewModel){
        this.mCharacterViewModel = mCharacterViewModel;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FactionModelListAdapter.FactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.faction_model, parent, false);
        return new FactionModelListAdapter.FactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactionModelListAdapter.FactionViewHolder viewHolder, int position) {
        if(factionsDataSet != null) {
            FactionModel current = factionsDataSet.get(position).getFaction();
            viewHolder.getFactionNameItemView().setText(current.getFactionName());
            viewHolder.itemView.setOnClickListener(v -> {
                Log.i("AUTO ", "Faction index: " + position);
                mCharacterViewModel.setFactionIndex(position);
                mCharacterViewModel.setSelectedFaction(current);
                Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_fragmentCharacterModel).onClick(viewHolder.itemView);
            });
            viewHolder.itemView.setOnLongClickListener(v -> {
                Dialog requestInput = createDialog(position);
                requestInput.show();
                return false;
            });
        }else{
            viewHolder.getFactionNameItemView().setText("No character data available");
        }
    }

    private Dialog createDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        FactionModel selected = factionsDataSet.get(position).getFaction();
        builder.setTitle("Delete the entire Faction?");
        TextView characterName = new EditText(mInflater.getContext());
        characterName.setText("     " + selected.getFactionName());
        builder.setView(characterName);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mCharacterViewModel.deleteFaction(selected);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Cancel button
            }
        });
        return builder.create();
    }

    public void setFactionDataSet(List<DataArmyWithCharacters> factionDataSet) {
        this.factionsDataSet = factionDataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(factionsDataSet != null) return factionsDataSet.size();
        else return 0;
    }
}

