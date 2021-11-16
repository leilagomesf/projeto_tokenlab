package com.example.projetotokenlabjava;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

//activity da tela de detalhes de filme
public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //recebe o parâmetro enviado pela activity anterior no onclicklistview
        Bundle b = getIntent().getExtras();
        int movie_id = -1; // or other values
        if(b != null)
            movie_id = b.getInt("movie_id");

        //chama o método para popular a tela
        populateMovieView(movie_id);
    }

    //método para receber a resposta da chamada http e usa-la para popular a tela de detalhes do filme
    public void populateMovieView(int movie_id){
        Context context = this;
        //cria um listener para esperar uma resposta de sucesso
        Response.Listener<JSONObject> listenerResponse = new Response.Listener<JSONObject>() {
            //define o que será feito quando a resposta for obtida
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //cria variáveis para todos os componentes que serão modificados
                    ProgressBar pb = findViewById(R.id.pgBar);
                    TextView txtTitle = findViewById(R.id.txtTitle);
                    TextView txtTagline = findViewById(R.id.txtTagline);
                    TextView txtOriginalLanguage = findViewById(R.id.txtOriginalLanguage);
                    TextView txtOverview = findViewById(R.id.txtOverview);
                    TextView txtVote = findViewById(R.id.txtVoteAverage);
                    TextView txtReleaseDate = findViewById(R.id.txtReleaseDate);
                    TextView txtPopularity = findViewById(R.id.txtPopularity);
                    TextView txtGenres = findViewById(R.id.txtGenres);
                    ImageView imgPoster = findViewById(R.id.imgPoster);

                    //para o progressbar
                    pb.setVisibility(View.INVISIBLE);

                    //caso o filme não seja em inglês, mostra o nome original do filme em parenteses
                    String originalLanguage = response.getString("original_language");
                    if (originalLanguage.equals("en"))
                        txtTitle.setText("Title: "+response.getString("title"));
                    else
                        txtTitle.setText("Title: "+response.getString("title")+" ("+response.getString("original_title")+")");

                    //mostra o slogan
                    txtTagline.setText(response.getString("tagline"));
                    //mostra a linguagem original
                    txtOriginalLanguage.setText("Original Language: "+response.getString("original_language").toUpperCase(Locale.ROOT));
                    //mostra a sinopse
                    txtOverview.setText("Overview: "+ response.getString("overview"));
                    //mostra a media de votos
                    txtVote.setText("Vote Average: "+response.getDouble("vote_average"));
                    //mostra a data de estreia
                    txtReleaseDate.setText("Release Date: "+response.getString("release_date"));
                    //mostra a popularidade
                    txtPopularity.setText("Popularity: "+response.getDouble("popularity"));

                    //recebe o vetor de gêneros do filme e os mostra como string separados por uma vírgula
                    JSONArray genresArray = response.getJSONArray("genres");
                    txtGenres.setText("Genres: ");
                    if (genresArray != null) {
                        txtGenres.setText(txtGenres.getText() + genresArray.get(0).toString());
                        for (int i = 1; i < genresArray.length(); i++) {
                            txtGenres.setText(txtGenres.getText() + ", "+genresArray.get(i).toString());
                        }
                    }

                    //mostra o poster
                    String imgUrl = response.getString("poster_url");
                    ImageLoader.loadImage(imgPoster,imgUrl,context);
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
                                finishAffinity();
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
        RequestMaker.requestMovieData(movie_id, context, listenerResponse, listenerError);
    }
}