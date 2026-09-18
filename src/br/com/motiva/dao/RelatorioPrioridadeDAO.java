package br.com.motiva.dao;

import br.com.motiva.db.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelatorioPrioridadeDAO {
    public record RelatorioPrioridadeRecord(Long id, int qtCritico, int qtAtencao,
                                            int qtAlerta, int qtNormal, String resumo,
                                            Timestamp dataGeracao) {}

    private static final String SQL_INSERIR =
            "INSERT INTO RELATORIO_PRIORIDADE (QT_CRITICO, QT_ATENCAO, QT_ALERTA, QT_NORMAL, RESUMO) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM RELATORIO_PRIORIDADE WHERE ID_RELATORIO = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT * FROM RELATORIO_PRIORIDADE ORDER BY ID_RELATORIO";
    private static final String SQL_ATUALIZAR =
            "UPDATE RELATORIO_PRIORIDADE SET QT_CRITICO = ?, QT_ATENCAO = ?, QT_ALERTA = ?, " +
                    "QT_NORMAL = ?, RESUMO = ? WHERE ID_RELATORIO = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM RELATORIO_PRIORIDADE WHERE ID_RELATORIO = ?";

    public RelatorioPrioridadeDAO() {
    }

    public RelatorioPrioridadeRecord salvarRelatorio(int qtCritico, int qtAtencao,
                                                     int qtAlerta, int qtNormal, String resumo) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID_RELATORIO"});
            stmt.setInt(1, qtCritico);
            stmt.setInt(2, qtAtencao);
            stmt.setInt(3, qtAlerta);
            stmt.setInt(4, qtNormal);
            stmt.setString(5, resumo);
            stmt.executeUpdate();

            keys = stmt.getGeneratedKeys();
            Long novoId = null;
            if (keys.next()) {
                novoId = keys.getLong(1);
            }
            return buscarPorId(novoId);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar RelatorioPrioridade: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(keys, stmt);
        }
    }

    public RelatorioPrioridadeRecord buscarPorId(Long id) {
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
            System.err.println("Erro ao buscar RelatorioPrioridade por id: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(rs, stmt);
        }
    }

    public List<RelatorioPrioridadeRecord> listarTodas() {
        List<RelatorioPrioridadeRecord> lista = new ArrayList<>();
        Connection conn = ConexaoBD.getInstancia().conectar();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_LISTAR_TODAS);
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar RelatorioPrioridade: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(RelatorioPrioridadeRecord relatorio) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setInt(1, relatorio.qtCritico());
            stmt.setInt(2, relatorio.qtAtencao());
            stmt.setInt(3, relatorio.qtAlerta());
            stmt.setInt(4, relatorio.qtNormal());
            stmt.setString(5, relatorio.resumo());
            stmt.setLong(6, relatorio.id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar RelatorioPrioridade: " + e.getMessage());
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
            System.err.println("Erro ao deletar RelatorioPrioridade: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt);
        }
    }

    private RelatorioPrioridadeRecord mapear(ResultSet rs) throws SQLException {
        return new RelatorioPrioridadeRecord(
                rs.getLong("ID_RELATORIO"),
                rs.getInt("QT_CRITICO"),
                rs.getInt("QT_ATENCAO"),
                rs.getInt("QT_ALERTA"),
                rs.getInt("QT_NORMAL"),
                rs.getString("RESUMO"),
                rs.getTimestamp("DATA_GERACAO")
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
