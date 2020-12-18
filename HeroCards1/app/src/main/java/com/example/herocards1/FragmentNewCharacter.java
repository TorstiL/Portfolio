package com.example.herocards1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentNewCharacter extends Fragment {

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;


    private String currentPhotoPath;
    private CharacterViewModel mCharacterViewModel;
    private EditText characterName;
    private EditText characterRank;
    private EditText characterWounds;
    private EditText characterRange;
    private EditText characterDamage;
    private EditText characterSpeed;
    private EditText characterDefence;
    private EditText characterAttacks;
    private ImageView characterImage;
    private Button cameraBtn;

    private DataArmyWithCharacters currentFaction;

    public FragmentNewCharacter() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_character, container, false);
        // Inflate the layout for this fragment
        characterImage = v.findViewById(R.id.new_character_imageView);
        cameraBtn = v.findViewById(R.id.new_character_button_camera);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        mCharacterViewModel = new ViewModelProvider(requireActivity()).get(CharacterViewModel.class);
        characterName = v.findViewById(R.id.new_character_input_character_name);
        characterRank = v.findViewById(R.id.new_character_input_character_rank);
        characterWounds = v.findViewById(R.id.new_character_input_character_wounds);
        characterRange = v.findViewById(R.id.new_character_input_character_range);
        characterDamage = v.findViewById(R.id.new_character_input_character_damage);
        characterSpeed = v.findViewById(R.id.new_character_input_character_speed);
        characterDefence = v.findViewById(R.id.new_character_input_character_defence);
        characterAttacks = v.findViewById(R.id.new_character_input_character_attacks);
        //Set current faction
        mCharacterViewModel.getAllFactionsWithCharacters().observe(getViewLifecycleOwner(), allFactionsWithAllCharacters -> {
            setCurrentFaction(allFactionsWithAllCharacters.get(mCharacterViewModel.getFactionIndex()));
        });
        //Set on click events for UI buttons
        v.findViewById(R.id.new_character_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentNewCharacter.this)
                        .navigate(R.id.action_fragmentNewCharacter_to_fragmentCharacterModel);
            }
        });

        v.findViewById(R.id.new_character_button_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if all the input fields are set
                if(allDataFieldsAreSet()){
                    //create a CharacterModel object
                    CharacterModel insertable = new CharacterModel(
                            currentFaction.getFaction().getFactionId(),
                            getFieldDataAsString(characterName),
                            getFieldDataAsString(characterRank),
                            Integer.parseInt(getFieldDataAsString(characterWounds)),
                            Integer.parseInt(getFieldDataAsString(characterRange)),
                            Integer.parseInt(getFieldDataAsString(characterDamage)),
                            Integer.parseInt(getFieldDataAsString(characterSpeed)),
                            Integer.parseInt(getFieldDataAsString(characterDefence)),
                            Integer.parseInt(getFieldDataAsString(characterAttacks))
                    );
                    //if a photo has been taken add its path to CharacterModel
                    if(currentPhotoPath != null){
                        insertable.setCharacterImgPath(currentPhotoPath);
                    }
                    //take the new character to database via CharacterViewModel
                    mCharacterViewModel.insertCharacter(insertable);
                    //set the currently selected character to point to the newly created one
                    mCharacterViewModel.setSelectedCharacter(insertable);
                    //move to FragmentCharacterStats to view the newly created character
                    NavHostFragment.findNavController(FragmentNewCharacter.this)
                            .navigate(R.id.action_fragmentNewCharacter_to_fragmentCharacterStats);
                }else{
                    Toast.makeText(getActivity(), "Make sure all the fields are filled", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return v;
    }

    public void setCurrentFaction(DataArmyWithCharacters currentFaction) {
        this.currentFaction = currentFaction;
    }

    private boolean allDataFieldsAreSet(){
        if(fieldIsEmpty(characterName) || fieldIsEmpty(characterRank) || fieldIsEmpty(characterWounds) || fieldIsEmpty(characterRange) || fieldIsEmpty(characterDamage)|| fieldIsEmpty(characterSpeed)|| fieldIsEmpty(characterDefence)|| fieldIsEmpty(characterAttacks)){
            return false;
        }
        return true;
    }

    private boolean fieldIsEmpty(EditText field){
        if(getFieldDataAsString(field).matches("")){
            return true;
        }
        return false;
    }

    private String getFieldDataAsString(EditText field){
        return field.getText().toString();
    }

    private void requestCameraPermission() {
        //saattaa tarvita this, ei getActivity
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //Log.i("AUTO", "ERROR");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Log.i("AUTO", "TOIMII");
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
            Log.i("AUTO", "RC: " + requestCode);
            if (resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                characterImage.setImageURI(Uri.fromFile(f));
            }
        }
    }
}