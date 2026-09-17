package br.com.motiva.model;

public class TrechoRodovia implements MonitoravelViaIoT {

    private Long id;
    private double quilometroInicial;
    private double quilometroFinal;
    private double nivelVegetacao;
    private TipoAmbiente tipoAmbiente;
    private boolean possuiSensorIoT;

    public TrechoRodovia(double quilometroInicial, double quilometroFinal,
                         double nivelVegetacao, TipoAmbiente tipoAmbiente,
                         boolean possuiSensorIoT) {
        this.quilometroInicial = quilometroInicial;
        setQuilometroFinal(quilometroFinal);
        this.nivelVegetacao = nivelVegetacao;
        this.tipoAmbiente = tipoAmbiente;
        this.possuiSensorIoT = possuiSensorIoT;
    }

    public TrechoRodovia(Long id, double quilometroInicial, double quilometroFinal,
                         double nivelVegetacao, TipoAmbiente tipoAmbiente,
                         boolean possuiSensorIoT) {
        this(quilometroInicial, quilometroFinal, nivelVegetacao, tipoAmbiente, possuiSensorIoT);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getQuilometroInicial() {
        return quilometroInicial;
    }

    public double getQuilometroFinal() {
        return quilometroFinal;
    }

    private void setQuilometroFinal(double quilometroFinal) {
        if (quilometroFinal <= this.quilometroInicial) {
            throw new IllegalArgumentException(
                    "O quilometroFinal deve ser maior que quilometroInicial.");
        }
        this.quilometroFinal = quilometroFinal;
    }

    public double getNivelVegetacao() {
        return nivelVegetacao;
    }

    public void setNivelVegetacao(double nivelVegetacao) {
        this.nivelVegetacao = Math.max(0, nivelVegetacao);
    }

    public TipoAmbiente getTipoAmbiente() {
        return tipoAmbiente;
    }

    public boolean isPossuiSensorIoT() {
        return possuiSensorIoT;
    }

    public void registrarCrescimento(double taxaBase) {
        double taxaReal = taxaBase * tipoAmbiente.getFatorCrescimento();
        setNivelVegetacao(this.nivelVegetacao + taxaReal);
    }

    @Override
    public void transmitirDadosSensor(double leituraDoSensor) {
        if (isSensorAtivo() && leituraDoSensor >= 0) {
            setNivelVegetacao(leituraDoSensor);
        }
    }

    @Override
    public boolean isSensorAtivo() {
        return possuiSensorIoT;
    }

    @Override
    public String toString() {
        return String.format(
                "TrechoRodovia{id=%s, km=%.2f-%.2f, nivelVegetacao=%.2fcm, ambiente=%s, sensorIoT=%s}",
                id, quilometroInicial, quilometroFinal, nivelVegetacao,
                tipoAmbiente.getDescricao(), possuiSensorIoT ? "S" : "N");
    }
}
