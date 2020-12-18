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

import static android.text.InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE;


public class CharacterModelListAdapter extends RecyclerView.Adapter<CharacterModelListAdapter.CharacterViewHolder> {

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final TextView characterNameItemView;
        private final TextView characterRankItemView;

        private CharacterViewHolder(View itemView) {
            super(itemView);
            characterNameItemView = itemView.findViewById(R.id.character_name);
            characterRankItemView = itemView.findViewById(R.id.character_rank);
        }

        public TextView getCharacterNameItemView() {
            return characterNameItemView;
        }
        public TextView getCharacterRankItemView() {
            return characterRankItemView;
        }
    }

    private final LayoutInflater mInflater;
    private List<CharacterModel> characterDataSet;
    private CharacterViewModel mCharacterViewModel;

    public CharacterModelListAdapter(Context context, CharacterViewModel characterViewModel){
        this.mInflater = LayoutInflater.from(context);
        this.mCharacterViewModel = characterViewModel;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_character_model, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder viewHolder, int position) {
        if(characterDataSet != null) {
            CharacterModel current = characterDataSet.get(position);
            viewHolder.getCharacterNameItemView().setText(current.getCharacterName());
            viewHolder.getCharacterRankItemView().setText(current.getCharacterRank());

            viewHolder.itemView.setOnClickListener(v -> {
                mCharacterViewModel.setSelectedCharacter(current);
                Navigation.createNavigateOnClickListener(R.id.action_fragmentCharacterModel_to_fragmentCharacterStats).onClick(viewHolder.itemView);
            });
            viewHolder.itemView.setOnLongClickListener(v -> {
                Dialog requestInput = createDialog(position);
                requestInput.show();
                return false;
            });
        }else{
            viewHolder.getCharacterNameItemView().setText("No character data available");
        }
    }

    private Dialog createDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        CharacterModel selected = characterDataSet.get(position);
        //set dialog content
        builder.setTitle("Delete Character?");
        TextView characterName = new EditText(mInflater.getContext());
        characterName.setText("     " + selected.getCharacterName());

        builder.setView(characterName);
        //set dialog positive button
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mCharacterViewModel.deleteCharacter(selected);
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

    public void setCharacterDataSet(List<CharacterModel> heroDataSet) {
        this.characterDataSet = heroDataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(characterDataSet != null) return characterDataSet.size();
        else return 0;
    }
}