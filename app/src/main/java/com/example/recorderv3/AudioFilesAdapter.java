package com.example.recorderv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AudioFilesAdapter extends ArrayAdapter<AudioFiles> {
    private LayoutInflater inflater;
    private int layout;
    private List<AudioFiles> filesList;

    public AudioFilesAdapter(@NonNull Context context, int resource, @NonNull List<AudioFiles> filesList) {
        super(context, resource, filesList);
        this.filesList = filesList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }


    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") View view = inflater.inflate(this.layout, parent, false);
        TextView nameFile = view.findViewById(R.id.nameFile);
        TextView pathFile = view.findViewById(R.id.pathFile);
        AudioFiles audioFile = filesList.get(position);
        nameFile.setText(audioFile.getFileName() +"\nДлительность: "+ audioFile.getDuration());
        pathFile.setText(audioFile.getFileName());
        return view;
    }
}
