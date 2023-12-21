package com.example.camaraDep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.camaraDep.Model.Deputado;
import com.example.camaraDep.Model.RespostaDeputados;
import com.example.camaraDep.Service.RestService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Deputados extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";
    private Retrofit retrofitDeputados;
    BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private ListView lista;
    private ArrayAdapter<String> deputadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputados);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        lista = findViewById(R.id.listaDeputados);
        deputadoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lista.setAdapter(deputadoAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.deputados);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.deputados:
                        return true;
                    case R.id.config:
                        startActivity(new Intent(getApplicationContext(), Config.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        retrofitDeputados = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listarDeputados();
    }

    private void listarDeputados() {
        RestService restService = retrofitDeputados.create(RestService.class);

        Call<RespostaDeputados> call = restService.listarDeputados();
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<RespostaDeputados>() {
            @Override
            public void onResponse(Call<RespostaDeputados> call, Response<RespostaDeputados> response) {
                if (response.isSuccessful()) {
                    List<Deputado> deputados = response.body().getDeputados();
                    List<String> deputadosInfo = new ArrayList<>();

                    for (Deputado deputado : deputados) {
                        String info =  deputado.getNome() + " (" + deputado.getSiglaPartido() + " - " + deputado.getSiglaUf() + ")";
                        deputadosInfo.add(info);
                    }

                    deputadoAdapter.clear();
                    deputadoAdapter.addAll(deputadosInfo);
                    progressBar.setVisibility(View.GONE);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position < deputados.size()) {
                                Deputado deputadoSelecionado = deputados.get(position);
                                int idDoDeputado = deputadoSelecionado.getId();

                                Intent intent = new Intent(Deputados.this, DeputadoActivity.class);
                                intent.putExtra("ID_DEPUTADO_SELECIONADO", idDoDeputado);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<RespostaDeputados> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
