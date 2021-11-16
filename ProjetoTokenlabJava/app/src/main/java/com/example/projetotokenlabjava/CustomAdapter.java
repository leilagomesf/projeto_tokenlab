package com.example.projetotokenlabjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//classe usada para a apresentação dos filmes na lista da tela inicial
//implementa um Adapter para ser pela listview na StartActivity
public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List movieList;

    public CustomAdapter(Context context, List movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int location) {
        return movieList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //infla o layout baseado no layout movie_row criado para definir a apresentação de cada filme
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.movie_row, null);


        TextView title = (TextView) rowView.findViewById(R.id.title);

        ImageView poster = (ImageView) rowView.findViewById(R.id.poster);

        // acessa o filme da posição atual
        Movie m = (Movie) movieList.get(position);

        // define título
        title.setText("Title: " + String.valueOf(m.getTitle()));

        // define poster
        ImageLoader.loadImage(poster,m.getPoster_url(), context);

        return rowView;
    }
}


