package br.ufal.ic.p2.wepayu.models.Empregados.pagamento;

public class Payment {
    private String tipo;

    public Payment(String tipo) {
        this.tipo = tipo;
    }

    public Payment(){ }

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
}
