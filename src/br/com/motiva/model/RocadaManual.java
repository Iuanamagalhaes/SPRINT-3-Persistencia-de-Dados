package br.com.motiva.model;

public class RocadaManual extends IntervencaoOperacional {
    private static final double NIVEL_POS_INTERVENCAO = 5.0;
    private int numeroDePessoas;

    public RocadaManual(String descricao, TrechoRodovia trechoAlvo, int numeroDePessoas) {
        super(descricao, trechoAlvo);
        this.numeroDePessoas = numeroDePessoas;
    }

    public int getNumeroDePessoas() {
        return numeroDePessoas;
    }

    @Override
    public void executarServico() {
        System.out.println("Executando rocada manual com " + numeroDePessoas + " pessoas");
        getTrechoAlvo().setNivelVegetacao(NIVEL_POS_INTERVENCAO);
    }
}
