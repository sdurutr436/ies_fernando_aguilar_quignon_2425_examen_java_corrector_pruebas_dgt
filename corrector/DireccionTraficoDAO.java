package dgt.corrector;

import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DireccionTraficoDAO implements IDireccionTraficoDAO {

    private File file_soluciones;
    private File file_puntuaciones;
    private static final String SOLUCIONES = "soluciones.txt";
    private static final String PUNTUACIONES = "puntuaciones.txt";

    public DireccionTraficoDAO() throws IllegalArgumentException {
        this.file_soluciones = new File(SOLUCIONES);
        this.file_puntuaciones = new File(PUNTUACIONES);
    }

    @Override
    public Map<String, List<String>> cargarSoluciones() throws DAOException {
        Map<String, List<String>> mapaSoluciones = new HashMap<>();
        try (BufferedReader brSol = new BufferedReader(new FileReader(file_soluciones))) {
            String linea;
            while ((linea = brSol.readLine()) != null) {
                String[] datos = linea.split(";");
                String idTest = datos[0];
                List<String> resultados = new ArrayList<>(Arrays.asList(datos).subList(1, datos.length));
                mapaSoluciones.putIfAbsent(idTest, resultados);
            }
        } catch (FileNotFoundException e2) {
            throw new DAOException("El archivo de soluciones no se encuentra " + SOLUCIONES);
        } catch (IOException e1) {
            throw new DAOException("Error leyendo el archivo de soluciones " + SOLUCIONES);
        }
        return mapaSoluciones;
    }

    @Override
    public List<Candidato> cargarCandidatos(String idTest) throws DAOException {
        File archivoTest = new File(idTest + ".txt");
        List<Candidato> listaCandidatos = new ArrayList<>();
        try (BufferedReader brCandidatos = new BufferedReader(new FileReader(archivoTest))) {
            String linea;
            while ((linea = brCandidatos.readLine()) != null) {
                String[] datos = linea.split(";");
                String nombre = datos[0];
                List<String> resultados = new ArrayList<>(Arrays.asList(datos).subList(1, datos.length));
                listaCandidatos.add(new Candidato(nombre, idTest, resultados));
            }
        } catch (FileNotFoundException e2) {
            throw new DAOException("El archivo del test no se encuentra " + archivoTest);
        } catch (IOException e1) {
            throw new DAOException("Error leyendo el archivo de test " + archivoTest);
        }
        return listaCandidatos;
    }

    @Override
    public void guardarPuntuajes(List<Candidato> candidatos) throws DAOException {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file_puntuaciones))) {
            Map<String, List<String>> mapaSol = cargarSoluciones();
            for (Candidato candidato : candidatos) {
                List<String> respuestasCandidato = candidato.getRespuestas();
                List<String> soluciones = mapaSol.get(candidato.getId());
                int totalPuntos = new CalculadoraPuntuajes().calcularPuntuaje(soluciones, respuestasCandidato);
                candidato.setPuntuaje(totalPuntos);
                bWriter.write(candidato.toString());
                if (totalPuntos < 7) {
                    bWriter.write("\t" + "suspenso");
                }
                bWriter.newLine();
            }
        } catch (IOException e) {
            throw new DAOException("Error escribiendo el archivo de puntuajes " + file_puntuaciones);
        }
    }

}
