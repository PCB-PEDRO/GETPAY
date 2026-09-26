package br.ufal.ic.p2.wepayu.models.Empregados;

public class EmpregadoHora extends Empregado {
    int horas;

    public EmpregadoHora(String nome, String endereco, String tipo, double salario){

        super(nome, endereco, tipo, salario);
    }

    public EmpregadoHora(){ }

    public int getHoras(){return this.horas;}
    public void setHoras(int horas){this.horas = horas;}

}
