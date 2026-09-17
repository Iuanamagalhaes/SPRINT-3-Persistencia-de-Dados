package br.com.motiva.model;

public class RocadaMecanizada extends IntervencaoOperacional {
    private static final double NIVEL_POS_INTERVENCAO = 2.0;
    private String tipoEquipamento;

    public RocadaMecanizada(String descricao, TrechoRodovia trechoAlvo, String tipoEquipamento) {
        super(descricao, trechoAlvo);
        this.tipoEquipamento = tipoEquipamento;
    }

    public String getTipoEquipamento() {
        return tipoEquipamento;
    }

    @Override
    public void executarServico() {
        System.out.println("Executando rocada mecanizada com equipamento: " + tipoEquipamento);
        getTrechoAlvo().setNivelVegetacao(NIVEL_POS_INTERVENCAO);
    }
}
