package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.BankEmp;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;

public class Facade {

    private BankEmp banco = new BankEmp();

    public void zerarSistema(){
        banco.zerar();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String sal) throws EmpregadoNaoExisteException, IllegalArgumentException, NonullException {
        validaString(nome,"Nome nao pode ser nulo.");
        validaString(endereco, "Endereco nao pode ser nulo.");
        validaString(sal, "Salario nao pode ser nulo.");
        validaTipo(tipo);

        if(tipo.equals("comissionado")){throw new InvalidTipeException("Tipo nao aplicavel.");}

        double salario = conversaoDouble(sal, "Salario deve ser numerico.", "Salario deve ser nao-negativo." );

        String novo = banco.addfunc(nome, endereco, tipo, salario);

        return novo;

    }

    public String criarEmpregado(String nome, String endereco, String tipo, String sal, String comi) throws EmpregadoNaoExisteException, InvalidTipeException, NonullException {
        validaString(nome,"Nome nao pode ser nulo.");
        validaString(endereco, "Endereco nao pode ser nulo.");
        validaString(sal, "Salario nao pode ser nulo.");
        validaString(comi, "Comissao nao pode ser nula.");
        validaTipo(tipo);

        if(!tipo.equals("comissionado")){throw new InvalidTipeException("Tipo nao aplicavel.");}

        double salario = conversaoDouble(sal, "Salario deve ser numerico.", "Salario deve ser nao-negativo." );

        double comissao = conversaoDouble(comi, "Comissao deve ser numerica.", "Comissao deve ser nao-negativa." );

        String novo = banco.adcfunc(nome, endereco, tipo, salario, comissao);
        return novo;
    }


    public String getAtributoEmpregado(String emp, String atribute) throws EmpregadoNaoExisteException, NonullException, InvalidTipeException {

        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.busca(emp);

        if (consulta == null) {
            throw new EmpregadoNaoExisteException();
        }

        switch (atribute) {
            case "nome":
                return consulta.getNome();
            case "endereco":
                return consulta.getEndereco();
            case "salario":
                return String.format("%.2f", consulta.getSalario()).replace(".", ",");
            case "tipo":
                return consulta.getTipo();
            case "comissao":
                if (!(consulta instanceof EmpregadoComis)) {
                    throw new InvalidTipeException("Empregado nao eh comissionado.");
                }
                EmpregadoComis consul = (EmpregadoComis) consulta;
                return String.format("%.2f", consul.getComissao()).replace(".", ",");
            case "sindicalizado":
                return String.valueOf(consulta.getSindicate());
            default:
                throw new InvalidTipeException("Atributo nao existe.");
        }
    }

   // public void encerrarSistema(){}

    private void validaString(String valor, String mensagemErro) throws NonullException {
        if (valor == null || valor.isBlank()) {
            throw new NonullException(mensagemErro);
        }
    }

    private void validaTipo(String tipo) throws InvalidTipeException {
        if (tipo == null || tipo.isBlank()) {
            throw new InvalidTipeException("Tipo invalido.");
        }
        if (!tipo.equals("horista") && !tipo.equals("assalariado") && !tipo.equals("comissionado")) {
            throw new InvalidTipeException("Tipo invalido.");
        }
    }

    private double conversaoDouble(String valor, String erroNum, String erroNegativo) {
        double num;
        try {
            num = Double.parseDouble(valor.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new NoNumException(erroNum);
        }
        if (num <= 0) {
            throw new NegativeNumException(erroNegativo);
        }
        return num;
    }
}