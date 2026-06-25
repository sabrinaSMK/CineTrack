package com.example.cinetrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinetrack.database.DatabaseHelper;
import com.example.cinetrack.model.Filme;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etTitulo, etGenero, etComentario;
    private RatingBar ratingNota;
    private Spinner spStatus;
    private TextView tvTotalFilmes, tvMediaNotas, tvAssistidos, tvQueroAssistir;
    private Button btnSalvar, btnVerFilmes;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        etTitulo = findViewById(R.id.etTitulo);
        etGenero = findViewById(R.id.etGenero);
        ratingNota = findViewById(R.id.ratingNota);
        spStatus = findViewById(R.id.spStatus);
        etComentario = findViewById(R.id.etComentario);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVerFilmes = findViewById(R.id.btnVerFilmes);

        tvTotalFilmes = findViewById(R.id.tvTotalFilmes);
        tvMediaNotas = findViewById(R.id.tvMediaNotas);
        tvAssistidos = findViewById(R.id.tvAssistidos);
        tvQueroAssistir = findViewById(R.id.tvQueroAssistir);

        configurarSpinnerStatus();

        btnSalvar.setOnClickListener(v -> salvarFilme());

        btnVerFilmes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaFilmesActivity.class);
            startActivity(intent);
        });

        carregarEstatisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            carregarEstatisticas();
        }
    }

    private void configurarSpinnerStatus() {
        String[] statusFilme = {
                "Assistido",
                "Quero Assistir"
        };

        ArrayAdapter<String> adapterStatus =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        statusFilme
                );

        adapterStatus.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spStatus.setAdapter(adapterStatus);
    }

    private void salvarFilme() {
        String titulo = etTitulo.getText().toString().trim();
        String genero = etGenero.getText().toString().trim();
        String status = spStatus.getSelectedItem().toString();
        String comentario = etComentario.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("Informe o título do filme");
            return;
        }

        double nota = ratingNota.getRating() * 2;

        Filme filme = new Filme();
        filme.setTitulo(titulo);
        filme.setGenero(genero);
        filme.setNota(nota);
        filme.setStatus(status);
        filme.setComentario(comentario);

        boolean sucesso = databaseHelper.inserirFilme(filme);

        if (sucesso) {
            Toast.makeText(this, "Filme salvo com sucesso!", Toast.LENGTH_SHORT).show();
            limparCampos();
            carregarEstatisticas();
        } else {
            Toast.makeText(this, "Erro ao salvar filme.", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarEstatisticas() {
        List<Filme> filmes = databaseHelper.listarFilmes();

        int total = filmes.size();
        int assistidos = 0;
        int queroAssistir = 0;
        double somaNotas = 0;

        for (Filme filme : filmes) {
            somaNotas += filme.getNota();

            String status = filme.getStatus().toLowerCase();

            if (status.contains("assistido")) {
                assistidos++;
            }

            if (status.contains("quero")) {
                queroAssistir++;
            }
        }

        double media = total > 0 ? somaNotas / total : 0;

        String estrelasMedia;

        if (media >= 9) {
            estrelasMedia = "⭐⭐⭐⭐⭐";
        } else if (media >= 7) {
            estrelasMedia = "⭐⭐⭐⭐";
        } else if (media >= 5) {
            estrelasMedia = "⭐⭐⭐";
        } else if (media >= 3) {
            estrelasMedia = "⭐⭐";
        } else if (media > 0) {
            estrelasMedia = "⭐";
        } else {
            estrelasMedia = "Sem avaliações";
        }

        tvTotalFilmes.setText("🎬 Filmes cadastrados: " + total);
        tvMediaNotas.setText("⭐ Avaliação média: " + estrelasMedia + " (" + String.format("%.1f", media) + ")");
        tvAssistidos.setText("✅ Assistidos: " + assistidos);
        tvQueroAssistir.setText("🕒 Quero assistir: " + queroAssistir);
    }

    private void limparCampos() {
        etTitulo.setText("");
        etGenero.setText("");
        ratingNota.setRating(0);
        spStatus.setSelection(0);
        etComentario.setText("");
    }
}