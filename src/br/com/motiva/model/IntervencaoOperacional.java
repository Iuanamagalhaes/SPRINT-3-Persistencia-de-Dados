package br.com.motiva.model;

public abstract class IntervencaoOperacional {

    private Long id;
    private String descricao;
    private TrechoRodovia trechoAlvo;

    protected IntervencaoOperacional(String descricao, TrechoRodovia trechoAlvo) {
        this.descricao = descricao;
        this.trechoAlvo = trechoAlvo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public TrechoRodovia getTrechoAlvo() {
        return trechoAlvo;
    }

    public abstract void executarServico();

    public final void iniciarIntervencao() {
        System.out.println("\nIniciando intervençãoo: " + descricao);
        System.out.printf("Trecho alvo: km %.2f - %.2f (nivel atual: %.2fcm)%n",
                trechoAlvo.getQuilometroInicial(), trechoAlvo.getQuilometroFinal(),
                trechoAlvo.getNivelVegetacao());
        executarServico();
        System.out.printf("Intervenção concluída. Novo nível de vegetação: %.2fcm%n",
                trechoAlvo.getNivelVegetacao());
    }
}
