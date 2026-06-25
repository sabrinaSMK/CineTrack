package com.example.cinetrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cinetrack.model.Filme;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinetrack.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FILMES = "filmes";

    private static final String COL_ID = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_GENERO = "genero";
    private static final String COL_NOTA = "nota";
    private static final String COL_STATUS = "status";
    private static final String COL_COMENTARIO = "comentario";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String criarTabelaFilmes = "CREATE TABLE " + TABLE_FILMES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT NOT NULL, " +
                COL_GENERO + " TEXT, " +
                COL_NOTA + " REAL, " +
                COL_STATUS + " TEXT, " +
                COL_COMENTARIO + " TEXT)";

        db.execSQL(criarTabelaFilmes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMES);
        onCreate(db);
    }

    public boolean inserirFilme(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(COL_TITULO, filme.getTitulo());
        valores.put(COL_GENERO, filme.getGenero());
        valores.put(COL_NOTA, filme.getNota());
        valores.put(COL_STATUS, filme.getStatus());
        valores.put(COL_COMENTARIO, filme.getComentario());

        long resultado = db.insert(TABLE_FILMES, null, valores);
        db.close();

        return resultado != -1;
    }

    public List<Filme> listarFilmes() {
        List<Filme> listaFilmes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILMES + " ORDER BY titulo ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Filme filme = new Filme();

                filme.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                filme.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)));
                filme.setGenero(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO)));
                filme.setNota(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_NOTA)));
                filme.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
                filme.setComentario(cursor.getString(cursor.getColumnIndexOrThrow(COL_COMENTARIO)));

                listaFilmes.add(filme);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaFilmes;
    }

    public boolean atualizarFilme(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(COL_TITULO, filme.getTitulo());
        valores.put(COL_GENERO, filme.getGenero());
        valores.put(COL_NOTA, filme.getNota());
        valores.put(COL_STATUS, filme.getStatus());
        valores.put(COL_COMENTARIO, filme.getComentario());

        int resultado = db.update(
                TABLE_FILMES,
                valores,
                COL_ID + " = ?",
                new String[]{String.valueOf(filme.getId())}
        );

        db.close();

        return resultado > 0;
    }

    public boolean excluirFilme(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int resultado = db.delete(
                TABLE_FILMES,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return resultado > 0;
    }

    public Filme buscarFilmePorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FILMES + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        Filme filme = null;

        if (cursor.moveToFirst()) {
            filme = new Filme();

            filme.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            filme.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)));
            filme.setGenero(cursor.getString(cursor.getColumnIndexOrThrow(COL_GENERO)));
            filme.setNota(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_NOTA)));
            filme.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
            filme.setComentario(cursor.getString(cursor.getColumnIndexOrThrow(COL_COMENTARIO)));
        }

        cursor.close();
        db.close();

        return filme;
    }
}