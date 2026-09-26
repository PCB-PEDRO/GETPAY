package br.ufal.ic.p2.wepayu.models.Empregados;

public class EmpregadoComis extends Empregado{

    private double comissao;

    public EmpregadoComis(String nome, String endereco, String tipo, double salario, double comissao){

        super(nome, endereco, tipo, salario);
        this.comissao = comissao;
    }

    public EmpregadoComis(){ }

    public double getComissao(){return this.comissao;}
    public void setComissao(double comissao){this.comissao = comissao;}



}
