package br.ufal.ic.p2.wepayu.models.Empregados;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;

public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private boolean sindicate;

    public Empregado(String nome, String endereco, String tipo, double salario) throws EmpregadoNaoExisteException {
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
        sindicate = false;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean getSindicate(){return sindicate;}

    public double getSalario() {
        return salario;
    }

}
