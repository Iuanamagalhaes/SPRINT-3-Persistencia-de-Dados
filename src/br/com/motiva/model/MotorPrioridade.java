package br.com.motiva.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MotorPrioridade {
    private static final double LIMIAR_CRITICO = 40.0;
    private static final double LIMIAR_ATENCAO = 20.0;
    private static final double LIMIAR_ALERTA = 10.0;

    public Prioridade classificarPrioridade(TrechoRodovia trecho) {
        double nivel = trecho.getNivelVegetacao();
        if (nivel >= LIMIAR_CRITICO) return Prioridade.CRITICO;
        if (nivel >= LIMIAR_ATENCAO) return Prioridade.ATENCAO;
        if (nivel >= LIMIAR_ALERTA) return Prioridade.ALERTA;
        return Prioridade.NORMAL;
    }

    public IntervencaoOperacional criarIntervencao(TrechoRodovia trecho, Prioridade prioridade) {
        switch (prioridade) {
            case CRITICO:
                return new RocadaMecanizada(
                        "Rocada mecanizada - prioridade CRITICA", trecho, "Trator roçadeira");
            case ATENCAO:
                return new RocadaManual(
                        "Rocada manual - prioridade ATENCAO", trecho, 4);
            case ALERTA:
                return new Pulverizacao(
                        "Pulverizacao - prioridade ALERTA", trecho, "Herbicida seletivo", 10.0);
            case NORMAL:
            default:
                return null;
        }
    }

    public TrechoRodovia[] ordenarPorCriticidade(TrechoRodovia[] trechos) {
        TrechoRodovia[] copia = Arrays.copyOf(trechos, trechos.length);
        Arrays.sort(copia, (a, b) -> Double.compare(b.getNivelVegetacao(), a.getNivelVegetacao()));
        return copia;
    }

    public List<IntervencaoOperacional> gerarRelatorio(TrechoRodovia[] trechos) {
        TrechoRodovia[] ordenados = ordenarPorCriticidade(trechos);
        List<IntervencaoOperacional> intervencoes = new ArrayList<>();
        for (TrechoRodovia trecho : ordenados) {
            Prioridade prioridade = classificarPrioridade(trecho);
            IntervencaoOperacional intervencao = criarIntervencao(trecho, prioridade);
            if (intervencao != null) {
                intervencoes.add(intervencao);
            }
        }
        return intervencoes;
    }
}
