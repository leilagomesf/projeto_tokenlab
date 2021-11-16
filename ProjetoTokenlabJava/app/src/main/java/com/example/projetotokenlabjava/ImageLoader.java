package com.example.projetotokenlabjava;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

//classe que usa a biblioteca picasso para carregar uma imagem de uma url em uma imageview
public class ImageLoader {
    public static void loadImage(ImageView imgView, String url, Context context) {
        Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        }).build();

        //carrega a image da url ou, se não encontrar imagem, carrega uma imagem avisando que está indisponível
        picasso.with(context).load(url)
                .placeholder(R.drawable.unavailable_poster)
                .into(imgView);
    }
}
