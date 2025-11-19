package dgt.corrector;

import java.util.List;

public class Candidato {
 
    private String nombre;
    private String id;
    private int puntuaje;
    private List<String> respuestas;

    
    public Candidato(String nombre, String id, List<String> respuestas) {
        this.nombre = nombre;
        this.id = id;
        this.puntuaje = 0;
        this.respuestas = respuestas;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPuntuaje() {
        return puntuaje;
    }

    public void setPuntuaje(int puntuaje) {
        this.puntuaje = puntuaje;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }

    @Override
    public String toString() {
        return String.format("%15s  %s", nombre, "\t") + puntuaje + "\t" + id;
    }

}
