package br.com.motiva.service;

import br.com.motiva.dao.RelatorioPrioridadeDAO;
import br.com.motiva.model.MotorPrioridade;
import br.com.motiva.model.Prioridade;
import br.com.motiva.model.TrechoRodovia;

public class GeradorRelatorio {
    private final MotorPrioridade motorPrioridade = new MotorPrioridade();

    public void gerarRelatorio(TrechoRodovia[] trechos) {
        imprimirCabecalho();

        int qtCritico = 0, qtAtencao = 0, qtAlerta = 0, qtNormal = 0;
        TrechoRodovia[] ordenados = motorPrioridade.ordenarPorCriticidade(trechos);

        for (TrechoRodovia trecho : ordenados) {
            Prioridade prioridade = motorPrioridade.classificarPrioridade(trecho);
            System.out.printf("Trecho km %.2f-%.2f | vegetacao: %.2fcm | prioridade: %s%n",
                    trecho.getQuilometroInicial(), trecho.getQuilometroFinal(),
                    trecho.getNivelVegetacao(), prioridade);

            switch (prioridade) {
                case CRITICO -> qtCritico++;
                case ATENCAO -> qtAtencao++;
                case ALERTA -> qtAlerta++;
                case NORMAL -> qtNormal++;
            }
        }

        String resumo = String.format(
                "Relatorio com %d trecho(s): %d critico(s), %d em atencao, %d em alerta, %d normal(is).",
                trechos.length, qtCritico, qtAtencao, qtAlerta, qtNormal);
        imprimirRodape(resumo);

        RelatorioPrioridadeDAO dao = new RelatorioPrioridadeDAO();
        var salvo = dao.salvarRelatorio(qtCritico, qtAtencao, qtAlerta, qtNormal, resumo);
        if (salvo != null) {
            System.out.println("Relatorio salvo no banco com id: " + salvo.id());
        } else {
            System.err.println("Falha ao salvar o relatorio no banco.");
        }
    }

    private void imprimirCabecalho() {
        System.out.println("\n+--------------------------------------+");
        System.out.println("|   RELATORIO DE PRIORIDADE - MOTIVA   |");
        System.out.println("+--------------------------------------+");
    }

    private void imprimirRodape(String resumo) {
        System.out.println("----------------------------------------");
        System.out.println(resumo);
    }
}
