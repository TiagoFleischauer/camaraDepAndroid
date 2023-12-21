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

import com.example.camaraDep.Model.Partido;
import com.example.camaraDep.Model.RespostaPartidos;
import com.example.camaraDep.Service.RestService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";
    private Retrofit retrofitPartidos;
    BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private ListView lista;
    private ArrayAdapter<String> partidoAdapter;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        lista = findViewById(R.id.lista);
        partidoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lista.setAdapter(partidoAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.deputados:
                        startActivity(new Intent(getApplicationContext(), Deputados.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.config:
                        startActivity(new Intent(getApplicationContext(), Config.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        retrofitPartidos = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        listarPartidos();
    }

    private void listarPartidos() {
        RestService restService = retrofitPartidos.create(RestService.class);

        Call<RespostaPartidos> call = restService.listarPartidos();
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<RespostaPartidos>() {
            @Override
            public void onResponse(Call<RespostaPartidos> call, Response<RespostaPartidos> response) {
                if (response.isSuccessful()) {
                    List<Partido> partidos = response.body().getPartidos();
                    List<String> partidosInfo = new ArrayList<>();

                    for (Partido partido : partidos) {
                        String info = partido.getSigla() + " - " + partido.getNome();
                        partidosInfo.add(info);
                    }

                    partidoAdapter.clear();
                    partidoAdapter.addAll(partidosInfo);
                    progressBar.setVisibility(View.GONE);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position < partidos.size()) {
                                Partido partidoSelecionado = partidos.get(position);
                                int idDoPartido = partidoSelecionado.getId(); // Obtendo o ID do partido selecionado

                                // Iniciar a nova atividade e enviar o ID do partido selecionado
                                Intent intent = new Intent(MainActivity.this, PartidoActivity.class);
                                intent.putExtra("ID_PARTIDO_SELECIONADO", idDoPartido);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespostaPartidos> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
