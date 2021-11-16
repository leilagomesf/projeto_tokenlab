package com.example.projetotokenlabjava;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//activity da tela inicial com a lista de filmes
public class StartActivity extends AppCompatActivity {

    private ListView listView;
    private List<Movie> movieList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar pgBar = findViewById(R.id.pgBar);

        listView = findViewById(R.id.list);

        //popula a listView com os filmes
        populateListView();

        //adiciona um evento para cada item da lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(StartActivity.this, MovieActivity.class);
                Bundle b = new Bundle();

                //disponibiliza para a nova activity o id do filme selecionado
                b.putInt("movie_id", movieList.get(position).getId());
                intent.putExtras(b);

                //inicia a activity de filme
                startActivity(intent);
            }
        });
    }

    //método para receber a resposta da chamada http e usa-la para popular a lista de filmes
    private void populateListView(){
        Context context = this;
        //cria um listener para esperar uma resposta de sucesso
        Response.Listener<JSONArray> listenerResponse = new Response.Listener<JSONArray>() {
            //define o que será feito quando a resposta for obtida
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //para o progressbar
                    ProgressBar pb = findViewById(R.id.pgBar);
                    pb.setVisibility(View.INVISIBLE);

                    //adiciona cada filme do vetor de resposta em uma lista de Movie
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Movie movie = gson.fromJson(jsonObject.toString(), Movie.class);
                        movieList.add(movie);
                    }

                    //inicializa o adapter e o define na listView para que ela seja populada
                    CustomAdapter adapter = new CustomAdapter(context, movieList);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //cria um listener para esperar uma resposta de erro
        Response.ErrorListener listenerError = new Response.ErrorListener() {
            //define o que será feito em caso de erro na resposta
            @Override
            public void onErrorResponse(VolleyError error) {
                //cria um alertdialog que avisa o usuário que não foi possível obter os dados e que apenas
                //estão disponíveis os dados já acessados(em cache)
                //da as opções de continuar ou sair do app
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Sorry, there was a problem retrieving the data. You'll only be able to access data you have accessed before.")
                        .setCancelable(false)
                        .setPositiveButton("Close app", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ((Activity)context).finishAffinity();
                            }
                        }).setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }

        };

        //efetivamente chama o request implementado na classe RequestMaker
        RequestMaker.requestMoviesData(context, listenerResponse, listenerError);
    }

}