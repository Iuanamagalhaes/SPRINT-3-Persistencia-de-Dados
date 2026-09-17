package br.com.motiva.model;

public class Pulverizacao extends IntervencaoOperacional {
    private static final double FATOR_REDUCAO = 0.7;

    private String produtoQuimico;
    private double volumeLitros;

    public Pulverizacao(String descricao, TrechoRodovia trechoAlvo,
                        String produtoQuimico, double volumeLitros) {
        super(descricao, trechoAlvo);
        this.produtoQuimico = produtoQuimico;
        this.volumeLitros = volumeLitros;
    }

    public String getProdutoQuimico() {
        return produtoQuimico;
    }

    public double getVolumeLitros() {
        return volumeLitros;
    }

    @Override
    public void executarServico() {
        System.out.printf("Pulverizando %.2fL de %s%n", volumeLitros, produtoQuimico);
        double nivelAtual = getTrechoAlvo().getNivelVegetacao();
        getTrechoAlvo().setNivelVegetacao(nivelAtual * FATOR_REDUCAO);
    }
}
