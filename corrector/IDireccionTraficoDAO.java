package dgt.corrector;

import java.util.List;
import java.util.Map;

public interface IDireccionTraficoDAO {
    
    Map<String, List<String>> cargarSoluciones() throws DAOException;
    List<Candidato> cargarCandidatos(String idTest) throws DAOException;
    void guardarPuntuajes(List<Candidato> candidatos) throws DAOException;

}
