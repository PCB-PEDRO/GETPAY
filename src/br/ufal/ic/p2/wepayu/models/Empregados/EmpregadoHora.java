package br.ufal.ic.p2.wepayu.models.Empregados;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class EmpregadoHora extends Empregado {
    int horas;

    public EmpregadoHora(String nome, String endereco, String tipo, double salario) throws EmpregadoNaoExisteException {

        super(nome, endereco, tipo, salario);
    }

}
