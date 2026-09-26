package br.ufal.ic.p2.wepayu.models.Empregados;


public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private boolean sindicate;

    public Empregado(String nome, String endereco, String tipo, double salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
        sindicate = false;
    }

    public Empregado(){ }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome){this.nome = nome;}

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco){this.endereco = endereco;}

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo){this.tipo = tipo;}

    public boolean isSindicate(){return sindicate;}
    public void setSindicate(boolean sindicate){this.sindicate = sindicate;}

    public double getSalario() {
        return salario;
    }
    public void setSalario(double salario) {this.salario = salario;}
}
