package com.example.camaraDep;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camaraDep.Model.Deputado;
import com.example.camaraDep.Model.RespostaDeputado;
import com.example.camaraDep.Service.RestService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeputadoActivity extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";
    private Retrofit retrofitDeputado;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private TextView title;
    private TextView sigla;
    private TextView siglaUf;
    private TextView email;
    private TextView condicao;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputado);

        int idDeputado = getIntent().getIntExtra("ID_DEPUTADO_SELECIONADO", -1);

        progressBar = findViewById(R.id.progressDeputado);
        progressBar.setVisibility(View.GONE);

        logo = findViewById(R.id.imageView);

        title = findViewById(R.id.nome);
        sigla = findViewById(R.id.sigla);
        siglaUf = findViewById(R.id.siglaUf);
        email = findViewById(R.id.email);
        condicao = findViewById(R.id.condicao);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.deputados);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Deputados.class));
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

        retrofitDeputado = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (idDeputado != -1) {
            Toast.makeText(this, "ID do Deputado Selecionado: " + idDeputado, Toast.LENGTH_SHORT).show();
            listarDeputado(idDeputado);
        } else {
            Toast.makeText(this, "ID do Deputado não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private void listarDeputado(int id) {
        RestService restService = retrofitDeputado.create(RestService.class);

        Call<RespostaDeputado> call = restService.listarDeputadoPorId(id);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<RespostaDeputado>() {
            @Override
            public void onResponse(Call<RespostaDeputado> call, Response<RespostaDeputado> response) {
                if (response.isSuccessful()) {
                    Deputado.UltimoStatus deputado = response.body().getDeputado().getUltimoStatus();
                    title.setText(deputado.getNome());
                    sigla.setText(deputado.getSiglaPartido());
                    siglaUf.setText(deputado.getSiglaUf());
                    email.setText(deputado.getEmail());
                    condicao.setText(deputado.getCondicaoEleitoral());
                    Picasso.get().load(deputado.getUrlFoto()).into(logo);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<RespostaDeputado> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
