package br.com.motiva.main;

import br.com.motiva.dao.EquipeManutencaoDAO;
import br.com.motiva.dao.IntervencaoOperacionalDAO;
import br.com.motiva.dao.TrechoRodoviaDAO;
import br.com.motiva.dao.RelatorioPrioridadeDAO;
import br.com.motiva.db.ConexaoBD;
import br.com.motiva.model.*;
import br.com.motiva.service.GeradorRelatorio;

public class Main {
    public static void main(String[] args) {
        ConexaoBD conexao = ConexaoBD.getInstancia();
        conexao.conectar();

        System.out.println("\n--- CRUD EquipeManutencao ---");
        EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
        var equipeInserida = daoEquipe.inserir("Equipe Teste Main", 5);
        System.out.println("Inserida: " + equipeInserida);
        System.out.println("Buscar por id: " + daoEquipe.buscarPorId(equipeInserida.id()));
        System.out.println("Listar todas: " + daoEquipe.listarTodas());
        daoEquipe.atualizar(new EquipeManutencaoDAO.EquipeManutencaoRecord(
                equipeInserida.id(), "Equipe Teste Main (atualizada)", 6));
        System.out.println("Apos atualizar: " + daoEquipe.buscarPorId(equipeInserida.id()));

        System.out.println("\n--- CRUD TrechoRodovia ---");
        TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
        TrechoRodovia trecho1 = new TrechoRodovia(0.0, 5.0, 42.0, TipoAmbiente.UMIDO, true);
        TrechoRodovia trecho2 = new TrechoRodovia(5.0, 10.0, 22.0, TipoAmbiente.TROPICAL, true);
        TrechoRodovia trecho3 = new TrechoRodovia(10.0, 15.0, 12.0, TipoAmbiente.SECO, false);
        TrechoRodovia trecho4 = new TrechoRodovia(15.0, 20.0, 6.0, TipoAmbiente.PADRAO, false);

        daoTrecho.inserir(trecho1);
        daoTrecho.inserir(trecho2);
        daoTrecho.inserir(trecho3);
        daoTrecho.inserir(trecho4);
        System.out.println("Trecho inserido: " + trecho1);
        System.out.println("Buscar por id: " + daoTrecho.buscarPorId(trecho1.getId()));

        System.out.println("\n--- CRUD IntervencaoOperacional ---");
        IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();
        MotorPrioridade motor = new MotorPrioridade();

        Prioridade prioridadeTrecho1 = motor.classificarPrioridade(trecho1);
        IntervencaoOperacional intervencao1 = motor.criarIntervencao(trecho1, prioridadeTrecho1);
        if (intervencao1 != null) {
            var intervencaoSalva = daoIntervencao.inserir(intervencao1);
            System.out.println("Intervencao salva: " + intervencaoSalva);

            EquipeManutencaoDAO.EquipeManutencaoRecord equipe = daoEquipe.buscarPorId(equipeInserida.id());
            System.out.println("Equipe responsavel: " + equipe);

            EquipeManutencao equipeManutencao = new EquipeManutencao(
                    equipe.id(), equipe.nome(), equipe.quantidadeIntegrantes());
            equipeManutencao.atenderTrecho(trecho1);
            equipeManutencao.executarIntervencao(intervencao1);
        }

        System.out.println("\n--- GeradorRelatorio ---");
        GeradorRelatorio gerador = new GeradorRelatorio();
        TrechoRodovia[] trechos = daoTrecho.listarTodas();
        gerador.gerarRelatorio(trechos);

        System.out.println("\n--- Historico de RelatorioPrioridade ---");
        RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();
        daoRelatorio.listarTodas().forEach(System.out::println);

        conexao.desconectar();
    }
}
