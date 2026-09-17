package br.com.motiva.model;

public class EquipeManutencao {
    private Long id;
    private String nome;
    private int quantidadeIntegrantes;

    public EquipeManutencao(String nome, int quantidadeIntegrantes) {
        this.nome = nome;
        this.quantidadeIntegrantes = quantidadeIntegrantes;
    }

    public EquipeManutencao(Long id, String nome, int quantidadeIntegrantes) {
        this(nome, quantidadeIntegrantes);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeIntegrantes() {
        return quantidadeIntegrantes;
    }

    public void atenderTrecho(TrechoRodovia trecho) {
        System.out.printf(
                nome, quantidadeIntegrantes, trecho.getQuilometroInicial(),
                trecho.getQuilometroFinal(), trecho.getNivelVegetacao());
    }

    public void executarIntervencao(IntervencaoOperacional intervencao) {
        System.out.println("Equipe '" + nome + "' executando intervencao...");
        intervencao.iniciarIntervencao();
    }

    @Override
    public String toString() {
        return String.format("EquipeManutencao{id=%s, nome='%s', integrantes=%d}",
                id, nome, quantidadeIntegrantes);
    }
}
