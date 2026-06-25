package com.example.cinetrack.model;

public class Filme {

    private int id;
    private String titulo;
    private String genero;
    private double nota;
    private String status;
    private String comentario;

    public Filme() {
    }

    public Filme(int id, String titulo, String genero, double nota, String status, String comentario) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.nota = nota;
        this.status = status;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}