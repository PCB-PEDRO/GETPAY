package br.ufal.ic.p2.wepayu.models.Empregados.pagamento;

public class Banco extends Payment{
    private String banco;
    private String agencia;
    private String conta;

    public Banco(String metodo, String banco, String agencia, String conta) {
        super(metodo);
        this.banco = banco;
        this.agencia = agencia;
        this.conta = conta;
    }

    public Banco(){ }

    public String getBanco() {return banco;}
    public void setBanco(String banco) {this.banco = banco;}

    public String getAgencia() {return agencia;}
    public void setAgencia(String agencia) {this.agencia = agencia;}

    public String getConta() {return conta;}
    public void setConta(String conta) {this.conta = conta;}
}
