package br.com.motiva.dao;

import br.com.motiva.db.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeManutencaoDAO {
    public record EquipeManutencaoRecord(Long id, String nome, int quantidadeIntegrantes) {}

    private static final String SQL_INSERIR =
            "INSERT INTO EQUIPE_MANUTENCAO (NOME, QTD_INTEGRANTES) VALUES (?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_EQUIPE, NOME, QTD_INTEGRANTES FROM EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT ID_EQUIPE, NOME, QTD_INTEGRANTES FROM EQUIPE_MANUTENCAO ORDER BY ID_EQUIPE";
    private static final String SQL_ATUALIZAR =
            "UPDATE EQUIPE_MANUTENCAO SET NOME = ?, QTD_INTEGRANTES = ? WHERE ID_EQUIPE = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";

    public EquipeManutencaoDAO() {
    }

    public EquipeManutencaoRecord inserir(String nome, int quantidadeIntegrantes) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID_EQUIPE"});
            stmt.setString(1, nome);
            stmt.setInt(2, quantidadeIntegrantes);
            stmt.executeUpdate();

            keys = stmt.getGeneratedKeys();
            Long novoId = null;
            if (keys.next()) {
                novoId = keys.getLong(1);
            }
            return new EquipeManutencaoRecord(novoId, nome, quantidadeIntegrantes);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir EquipeManutencao: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(keys, stmt, null);
        }
    }

    public EquipeManutencaoRecord buscarPorId(Long id) {
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
            System.err.println("Erro ao buscar EquipeManutencao por id: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(rs, stmt, null);
        }
    }

    public List<EquipeManutencaoRecord> listarTodas() {
        List<EquipeManutencaoRecord> lista = new ArrayList<>();
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
            System.err.println("Erro ao listar EquipeManutencao: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(rs, stmt, null);
        }
        return lista;
    }

    public boolean atualizar(EquipeManutencaoRecord equipe) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setString(1, equipe.nome());
            stmt.setInt(2, equipe.quantidadeIntegrantes());
            stmt.setLong(3, equipe.id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar EquipeManutencao: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt, null);
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
            System.err.println("Erro ao deletar EquipeManutencao: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt, null);
        }
    }

    private EquipeManutencaoRecord mapear(ResultSet rs) throws SQLException {
        return new EquipeManutencaoRecord(
                rs.getLong("ID_EQUIPE"),
                rs.getString("NOME"),
                rs.getInt("QTD_INTEGRANTES")
        );
    }

    private void fecharRecursos(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar recursos: " + e.getMessage());
        }
    }
}