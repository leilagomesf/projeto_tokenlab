package com.example.projetotokenlabjava;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

//classe que atua como model do projeto, faz as realiza as chamadas http e cria o cache para essas chamadas
//usando a biblioteca volley
public class RequestMaker {
    private static final String ENDPOINT_URL = "https://desafio-mobile.nyc3.digitaloceanspaces.com/movies";

    //método que faz a chama da lista de filmes
    public static void requestMoviesData(Context context,
                                      Response.Listener<JSONArray> listenerResponse, Response.ErrorListener listenerError) {
        //cria a chamada para o JSONArray que representa a lista de filmes
        JsonArrayRequest requestMovies = new JsonArrayRequest(Request.Method.GET, ENDPOINT_URL, null, listenerResponse,
                listenerError) {
            //interpreta a resposta recebida criando cache com ela
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    //definições da cache para que ela possa ser criada
                    final long cacheExpired = 24 * 60 * 60 * 1000; //ela irá expirar em 24 horas
                    long now = System.currentTimeMillis();
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        //realiza a chamada
        Volley.newRequestQueue(context).add(requestMovies);
    }

    //método que faz a chama dos detalhes de um filme
    //além de receber os parâmetros do método anterior, também recebe o id do filme que vai ser consultado
    public static void requestMovieData(int movie_id, Context context,
                                    Response.Listener<JSONObject> listenerResponse, Response.ErrorListener listenerError){
        //cria a chamada para o JSONObject que representa o filme
        JsonObjectRequest requestMovie = new JsonObjectRequest(Request.Method.GET, ENDPOINT_URL+"/"+movie_id, null, listenerResponse,
                listenerError){
            //interpreta a resposta recebida criando cache com ela
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    //definições da cache para que ela possa ser criada
                    final long cacheExpired = 24 * 60 * 60 * 1000; //ela irá expirar em 24 horas
                    long now = System.currentTimeMillis();
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,"UTF-8");
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        //realiza a chamada
        Volley.newRequestQueue(context).add(requestMovie);
    }
}
