package br.ufal.ic.p2.wepayu.models.Empregados;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class EmpregadoComis extends Empregado{

    private double comissao;

    public EmpregadoComis(String nome, String endereco, String tipo, double salario, double comissao) throws EmpregadoNaoExisteException{

        super(nome, endereco, tipo, salario);
        this.comissao = comissao;
    }

    public double getComissao(){
        return this.comissao;
    }



}
