package com.example.cinetrack;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinetrack.database.DatabaseHelper;
import com.example.cinetrack.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class ListaFilmesActivity extends AppCompatActivity {
    private ListView listViewFilmes;
    private DatabaseHelper databaseHelper;
    private List<Filme> listaFilmes;
    private List<String> listaTextoFilmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_filmes);

        listViewFilmes = findViewById(R.id.listViewFilmes);
        databaseHelper = new DatabaseHelper(this);

        carregarFilmes();

        listViewFilmes.setOnItemClickListener((parent, view, position, id) -> {

            Filme filmeSelecionado = listaFilmes.get(position);

            Intent intent = new Intent(
                    ListaFilmesActivity.this,
                    EditarFilmeActivity.class
            );

            intent.putExtra("filme_id", filmeSelecionado.getId());

            startActivity(intent);
        });

        listViewFilmes.setOnItemLongClickListener((parent, view, position, id) -> {

            Filme filmeSelecionado = listaFilmes.get(position);

            confirmarExclusao(filmeSelecionado);

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarFilmes();
    }

    private void carregarFilmes() {

        listaFilmes = databaseHelper.listarFilmes();
        listaTextoFilmes = new ArrayList<>();

        for (Filme filme : listaFilmes) {

            String estrelas;

            if (filme.getNota() >= 9) {
                estrelas = "⭐⭐⭐⭐⭐";
            } else if (filme.getNota() >= 7) {
                estrelas = "⭐⭐⭐⭐";
            } else if (filme.getNota() >= 5) {
                estrelas = "⭐⭐⭐";
            } else if (filme.getNota() >= 3) {
                estrelas = "⭐⭐";
            } else {
                estrelas = "⭐";
            }

            String texto = "🎬 " + filme.getTitulo() +
                    "\n\n🎭 Gênero: " + filme.getGenero() +
                    "\n⭐ Avaliação: " + estrelas +
                    "\n📌 Status: " + filme.getStatus() +
                    "\n💬 Comentário: " + filme.getComentario() +
                    "\n\n✏️ Toque para editar   🗑️ Segure para excluir";

            listaTextoFilmes.add(texto);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_filme,
                listaTextoFilmes
        );

        listViewFilmes.setAdapter(adapter);
    }

    private void confirmarExclusao(Filme filme) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Excluir filme");
        builder.setMessage("Deseja excluir o filme \"" + filme.getTitulo() + "\"?");

        builder.setPositiveButton("Sim", (dialog, which) -> {

            boolean sucesso = databaseHelper.excluirFilme(filme.getId());

            if (sucesso) {
                Toast.makeText(this,
                        "Filme excluído com sucesso!",
                        Toast.LENGTH_SHORT).show();

                carregarFilmes();
            } else {
                Toast.makeText(this,
                        "Erro ao excluir filme.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }
}
