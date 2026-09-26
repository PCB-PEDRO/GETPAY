package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.InvalidTipeException;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoHora;

import java.util.HashMap;
import java.util.Map;

public class BankEmp {
    private final Map<String, Empregado> empregados = new HashMap<>();
    private int Newid = 1;


    public String addfunc(String nome, String endereco, String tipo, double salario) throws InvalidTipeException, EmpregadoNaoExisteException {

        Empregado novo;

        switch(tipo){
            case "horista":
                novo = new EmpregadoHora(nome, endereco, tipo, salario);
                break;
            case "assalariado":
                novo = new Empregado(nome, endereco,tipo, salario);
                break;
            default:
                throw new InvalidTipeException("Tipo invalido");
        }
        String id = "Sid" + Newid;

        empregados.put(id, novo);
        Newid++;

        return id;
    }

    public String adcfunc(String nome, String endereco, String tipo, double salario, double comissao) throws IllegalArgumentException, EmpregadoNaoExisteException {

        Empregado novo;

        novo = new EmpregadoComis(nome, endereco, tipo, salario, comissao);
        String id = "Sid" + Newid;

        empregados.put(id, novo);
        Newid++;

        return id;
    }


    public void zerar(){
        empregados.clear();
        Newid = 1;
    }

    public Empregado busca(String id){

        return empregados.get(id);
    }
}


