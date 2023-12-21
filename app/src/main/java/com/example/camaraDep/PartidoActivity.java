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

import com.example.camaraDep.Model.Partido;
import com.example.camaraDep.Model.RespostaPartido;
import com.example.camaraDep.Service.RestService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PartidoActivity extends AppCompatActivity {

    private final String URL = "https://dadosabertos.camara.leg.br/api/v2/";
    private Retrofit retrofitPartido;
    BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    private TextView title;
    private TextView sigla;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);

        int idPartido = getIntent().getIntExtra("ID_PARTIDO_SELECIONADO", -1);

        progressBar = findViewById(R.id.progressPartido);
        progressBar.setVisibility(View.GONE);

        logo = findViewById(R.id.imageView);

        title = findViewById(R.id.textView);
        sigla = findViewById(R.id.sigla);

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

        retrofitPartido = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (idPartido != -1) {
            Toast.makeText(this, "ID do Partido Selecionado: " + idPartido, Toast.LENGTH_SHORT).show();
            listarPartido(idPartido);
        } else {
            Toast.makeText(this, "ID do Partido não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private void listarPartido(int id) {
        RestService restService = retrofitPartido.create(RestService.class);

        Call<RespostaPartido> call = restService.listarPartidoPorId(id);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<RespostaPartido>() {
            @Override
            public void onResponse(Call<RespostaPartido> call, Response<RespostaPartido> response) {
                if (response.isSuccessful()) {
                    Partido partido = response.body().getPartido();
                    title.setText(partido.getNome());
                    sigla.setText(partido.getSigla());
                    Picasso.get().load(partido.getUrlLogo()).into(logo);
                    Toast.makeText(getApplicationContext(), partido.getNome(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<RespostaPartido> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
