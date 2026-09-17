package br.com.motiva.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "SEU_RM";
    private static final String PASSWORD = "SUA_SENHA";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static ConexaoBD instancia;
    private Connection connection;

    private ConexaoBD() {
    }

    public static synchronized ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }

    public Connection conectar() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexao com o Oracle estabelecida com sucesso!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC nao encontrado. Verifique o ojdbc17.jar no classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar no banco: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexao encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexao: " + e.getMessage());
            e.printStackTrace();
        }
    }
}