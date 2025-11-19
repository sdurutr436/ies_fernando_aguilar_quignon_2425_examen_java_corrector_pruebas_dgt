package dgt.corrector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DireccionTraficoApp {

    private IDireccionTraficoDAO dao;

    public DireccionTraficoApp(IDireccionTraficoDAO dao) {
        this.dao = dao;
    }

    public void procesaTest() throws DAOException {
        Map<String, List<String>> mapaSol = new HashMap<>();
        List<Candidato> candidatos = new ArrayList<>();

        try {
            mapaSol = dao.cargarSoluciones();
        } catch (RuntimeException e) {
            throw new DAOException("Error al cargar archivo");
        }


            for (String idTest : mapaSol.keySet()) {
                try {
                    candidatos.addAll(dao.cargarCandidatos(idTest));
                } catch (DAOException e) {
                    System.out.println("Error, test no se encuentra: " + idTest);
                    continue;
                }

                try {
                    dao.guardarPuntuajes(candidatos);
                } catch (RuntimeException e) {
                    throw new DAOException("Error al cargar archivo");
                }

            }


    }

    public static void main(String[] args) {
        try {
            IDireccionTraficoDAO dao = new DireccionTraficoDAO();
            DireccionTraficoApp programa = new DireccionTraficoApp(dao);
            programa.procesaTest();
        } catch (DAOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
