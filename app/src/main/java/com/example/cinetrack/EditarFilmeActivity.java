package com.example.cinetrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinetrack.database.DatabaseHelper;
import com.example.cinetrack.model.Filme;

public class EditarFilmeActivity extends AppCompatActivity {

    private EditText etTitulo, etGenero, etStatus, etComentario;
    private RatingBar ratingNotaEditar;
    private Button btnAtualizar;
    private DatabaseHelper databaseHelper;
    private int filmeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_filme);

        databaseHelper = new DatabaseHelper(this);

        etTitulo = findViewById(R.id.etTituloEditar);
        etGenero = findViewById(R.id.etGeneroEditar);
        ratingNotaEditar = findViewById(R.id.ratingNotaEditar);
        etStatus = findViewById(R.id.etStatusEditar);
        etComentario = findViewById(R.id.etComentarioEditar);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        filmeId = getIntent().getIntExtra("filme_id", -1);

        if (filmeId != -1) {
            carregarDadosFilme();
        }

        btnAtualizar.setOnClickListener(v -> atualizarFilme());
    }

    private void carregarDadosFilme() {
        Filme filme = databaseHelper.buscarFilmePorId(filmeId);

        if (filme != null) {
            etTitulo.setText(filme.getTitulo());
            etGenero.setText(filme.getGenero());
            ratingNotaEditar.setRating((float) filme.getNota() / 2);
            etStatus.setText(filme.getStatus());
            etComentario.setText(filme.getComentario());
        }
    }

    private void atualizarFilme() {
        String titulo = etTitulo.getText().toString().trim();
        String genero = etGenero.getText().toString().trim();
        String status = etStatus.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();

        if (titulo.isEmpty()) {
            etTitulo.setError("Informe o título do filme");
            return;
        }

        double nota = ratingNotaEditar.getRating() * 2;

        Filme filme = new Filme();
        filme.setId(filmeId);
        filme.setTitulo(titulo);
        filme.setGenero(genero);
        filme.setNota(nota);
        filme.setStatus(status);
        filme.setComentario(comentario);

        boolean sucesso = databaseHelper.atualizarFilme(filme);

        if (sucesso) {
            Toast.makeText(this, "Filme atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar filme.", Toast.LENGTH_SHORT).show();
        }
    }
}