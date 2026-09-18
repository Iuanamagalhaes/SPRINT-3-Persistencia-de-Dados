package br.com.motiva.dao;

import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.Pulverizacao;
import br.com.motiva.model.RocadaManual;
import br.com.motiva.model.RocadaMecanizada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IntervencaoOperacionalDAO {
    public record IntervencaoOperacionalRecord(Long id, String descricao, Long idTrechoAlvo,
                                               String tipoIntervencao, String tipoEquipamento,
                                               Integer numeroPessoas, String produtoQuimico,
                                               Double volumeLitros) {}

    private static final String SQL_INSERIR =
            "INSERT INTO INTERVENCAO_OPERACIONAL " +
                    "(DESCRICAO, ID_TRECHO_ALVO, TIPO_INTERVENCAO, TIPO_EQUIPAMENTO, NUMERO_PESSOAS, PRODUTO_QUIMICO, VOLUME_LITROS) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM INTERVENCAO_OPERACIONAL WHERE ID_INTERVENCAO = ?";
    private static final String SQL_LISTAR_TODAS =
            "SELECT * FROM INTERVENCAO_OPERACIONAL ORDER BY ID_INTERVENCAO";
    private static final String SQL_ATUALIZAR =
            "UPDATE INTERVENCAO_OPERACIONAL SET DESCRICAO = ?, ID_TRECHO_ALVO = ? WHERE ID_INTERVENCAO = ?";
    private static final String SQL_DELETAR =
            "DELETE FROM INTERVENCAO_OPERACIONAL WHERE ID_INTERVENCAO = ?";

    public IntervencaoOperacionalDAO() {
    }

    public IntervencaoOperacionalRecord inserir(IntervencaoOperacional intervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID_INTERVENCAO"});
            stmt.setString(1, intervencao.getDescricao());
            stmt.setLong(2, intervencao.getTrechoAlvo().getId());

            String tipo;
            if (intervencao instanceof RocadaMecanizada rm) {
                tipo = "MECANIZADA";
                stmt.setString(3, tipo);
                stmt.setString(4, rm.getTipoEquipamento());
                stmt.setNull(5, Types.NUMERIC);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.NUMERIC);
            } else if (intervencao instanceof RocadaManual rman) {
                tipo = "MANUAL";
                stmt.setString(3, tipo);
                stmt.setNull(4, Types.VARCHAR);
                stmt.setInt(5, rman.getNumeroDePessoas());
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.NUMERIC);
            } else if (intervencao instanceof Pulverizacao p) {
                tipo = "PULVERIZACAO";
                stmt.setString(3, tipo);
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.NUMERIC);
                stmt.setString(6, p.getProdutoQuimico());
                stmt.setDouble(7, p.getVolumeLitros());
            } else {
                throw new IllegalArgumentException("Tipo de intervencao desconhecido");
            }

            stmt.executeUpdate();
            keys = stmt.getGeneratedKeys();
            Long novoId = null;
            if (keys.next()) {
                novoId = keys.getLong(1);
                intervencao.setId(novoId);
            }
            return buscarPorId(novoId);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir IntervencaoOperacional: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(keys, stmt);
        }
    }

    public IntervencaoOperacionalRecord buscarPorId(Long id) {
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
            System.err.println("Erro ao buscar IntervencaoOperacional por id: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            fecharRecursos(rs, stmt);
        }
    }

    public List<IntervencaoOperacionalRecord> listarTodas() {
        List<IntervencaoOperacionalRecord> lista = new ArrayList<>();
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
            System.err.println("Erro ao listar IntervencaoOperacional: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharRecursos(rs, stmt);
        }
        return lista;
    }

    public boolean atualizar(IntervencaoOperacionalRecord intervencao) {
        Connection conn = ConexaoBD.getInstancia().conectar();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(SQL_ATUALIZAR);
            stmt.setString(1, intervencao.descricao());
            stmt.setLong(2, intervencao.idTrechoAlvo());
            stmt.setLong(3, intervencao.id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar IntervencaoOperacional: " + e.getMessage());
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
            System.err.println("Erro ao deletar IntervencaoOperacional: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            fecharRecursos(null, stmt);
        }
    }

    private IntervencaoOperacionalRecord mapear(ResultSet rs) throws SQLException {
        Double volume = rs.getObject("VOLUME_LITROS") != null ? rs.getDouble("VOLUME_LITROS") : null;
        Integer pessoas = rs.getObject("NUMERO_PESSOAS") != null ? rs.getInt("NUMERO_PESSOAS") : null;
        return new IntervencaoOperacionalRecord(
                rs.getLong("ID_INTERVENCAO"),
                rs.getString("DESCRICAO"),
                rs.getLong("ID_TRECHO_ALVO"),
                rs.getString("TIPO_INTERVENCAO"),
                rs.getString("TIPO_EQUIPAMENTO"),
                pessoas,
                rs.getString("PRODUTO_QUIMICO"),
                volume
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
