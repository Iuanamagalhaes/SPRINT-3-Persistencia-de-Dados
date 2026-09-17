package br.com.motiva.dao;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.TipoAmbiente;
import br.com.motiva.model.TrechoRodovia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TrechoRodoviaDAO {
    public record TrechoRodoviaRecord(Long id, double quilometroInicial, double quilometroFinal,
                                      double nivelVegetacao, TipoAmbiente tipoAmbiente,
                                      boolean possuiSensorIoT) {}

    private static final String SQL_INSERIR =
            "INSERT INTO TRECHO_RODOVIA (KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_AMBIENTE, POSSUI_SENSOR_IOT) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_TRECHO, KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_AMBIENTE, POSSUI_SENSOR_IOT " +
                    "FROM TRECHO_RODOVIA WHERE ID_TRECHO = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_TRECHO, KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_AMBIENTE, POSSUI_SENSOR_IOT " +
                    "FROM TRECHO_RODOVIA ORDER BY ID_TRECHO";
    private static final String SQL_ATUALIZAR =
            "UPDATE TRECHO_RODOVIA SET KM_INICIAL = ?, KM_FINAL = ?, NIVEL_VEGETACAO = ?, " +
                    "TIPO_AMBIENTE = ?, POSSUI_SENSOR_IOT = ? WHERE ID_TRECHO = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM TRECHO_RODOVIA WHERE ID_TRECHO = ?";

    public TrechoRodoviaDAO() {
    }

    public TrechoRodoviaRecord inserir(TrechoRodovia trecho) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID_TRECHO"});
            stmt.setDouble(1, trecho.getQuilometroInicial());
            stmt.setDouble(2, trecho.getQuilometroFinal());
            stmt.setDouble(3, trecho.getNivelVegetacao());
            stmt.setString(4, trecho.getTipoAmbiente().name());
            stmt.setString(5, trecho.isPossuiSensorIoT() ? "S" : "N");
            stmt.executeUpdate();

            keys = stmt.getGeneratedKeys();
            Long novoId = null;
            if (keys.next()) {
                novoId = keys.getLong(1);
                trecho.setId(novoId);
            }
            return new TrechoRodoviaRecord(novoId, trecho.getQuilometroInicial(),
                    trecho.getQuilometroFinal(), trecho.getNivelVegetacao(),
                    trecho.getTipoAmbiente(), trecho.isPossuiSensorIoT());
        } catch (SQLException e) {
            System.err.println("Erro ao inserir TrechoRodovia: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(keys, stmt);
        }
    }

    public TrechoRodoviaRecord buscarPorId(Long id) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar TrechoRodovia por id: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(rs, stmt);
        }
    }

    public TrechoRodovia[] listarTodas() {
        List<TrechoRodovia> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_LISTAR_TODAS);
            while (rs.next()) {
                TrechoRodoviaRecord r = mapear(rs);
                lista.add(new TrechoRodovia(r.id(), r.quilometroInicial(), r.quilometroFinal(),
                        r.nivelVegetacao(), r.tipoAmbiente(), r.possuiSensorIoT()));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar TrechoRodovia: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista.toArray(new TrechoRodovia[0]);
    }

    public boolean atualizar(TrechoRodovia trecho) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setDouble(1, trecho.getQuilometroInicial());
            stmt.setDouble(2, trecho.getQuilometroFinal());
            stmt.setDouble(3, trecho.getNivelVegetacao());
            stmt.setString(4, trecho.getTipoAmbiente().name());
            stmt.setString(5, trecho.isPossuiSensorIoT() ? "S" : "N");
            stmt.setLong(6, trecho.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar TrechoRodovia: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt);
        }
    }

    public boolean deletar(Long id) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_DELETAR);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar TrechoRodovia: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt);
        }
    }

    private TrechoRodoviaRecord mapear(ResultSet rs) throws SQLException {
        return new TrechoRodoviaRecord(
                rs.getLong("ID_TRECHO"),
                rs.getDouble("KM_INICIAL"),
                rs.getDouble("KM_FINAL"),
                rs.getDouble("NIVEL_VEGETACAO"),
                TipoAmbiente.valueOf(rs.getString("TIPO_AMBIENTE")),
                "S".equals(rs.getString("POSSUI_SENSOR_IOT"))
        );
    }

    private void fecharRecursos(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}
