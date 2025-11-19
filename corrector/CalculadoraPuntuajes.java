package dgt.corrector;

import java.util.List;

public class CalculadoraPuntuajes {
    
    public int calcularPuntuaje(List<String> soluciones, List<String> respuestas) {
        int totalPuntos = 0;
        if (soluciones != null) {
            for (int i = 0; i < respuestas.size(); i++) {
                if (respuestas.get(i).equals(soluciones.get(i))) {
                    totalPuntos++;
                }
            }
        }
        return totalPuntos;
    }

}
