package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.BankEmp;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoHora;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Facade {
    BankEmp banco;

    public Facade() {
        this.banco = new BankEmp();
        this.banco.carregarDados();
    }

    public void encerrarSistema() {
        this.banco.salvarDados();
    }

    public void zerarSistema() {
        banco.zerar();
        this.banco.salvarDados();
    }


    public String criarEmpregado(String nome, String endereco, String tipo, String sal) throws EmpregadoNaoExisteException, IllegalArgumentException, NonullException {
        validaString(nome,"Nome nao pode ser nulo.");
        validaString(endereco, "Endereco nao pode ser nulo.");
        validaString(sal, "Salario nao pode ser nulo.");
        validaTipo(tipo);

        if(tipo.equals("comissionado")){throw new InvalidTipeException("Tipo nao aplicavel.");}

        double salario = conversaoDouble(sal, "Salario deve ser numerico.", "Salario deve ser nao-negativo." );

        return banco.addfunc(nome, endereco, tipo, salario);


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

        return banco.adcfunc(nome, endereco, tipo, salario, comissao);
    }

    public void removerEmpregado(String sid) throws EmpregadoNaoExisteException, NonullException {
        validaString(sid, "Identificacao do empregado nao pode ser nula.");

        banco.removefunc(sid);
    }

    public void lancaCartao(String sid, String sdata, String shoras) throws EmpregadoNaoExisteException, InvalidTipeException, NonullException {

        validaString(sid, "Identificacao do empregado nao pode ser nula.");

        Empregado aux = banco.getEmpregado(sid);

        if(aux == null){
            throw new EmpregadoNaoExisteException();
        }

        EmpregadoHora func = conversaoHorista(aux);

        LocalDate data = conversaoData(sdata, "Data invalida.");

        double horas = conversaoDouble(shoras,"Horas devem ser um numero.", "Horas devem ser positivas." );
        func.addCard(sdata, horas);

    }

    public String getHorasNormaisTrabalhadas(String sid, String datai, String dataf) throws EmpregadoNaoExisteException, InvalidTipeException, NonullException {
        validaString(sid, "Identificacao do empregado nao pode ser nula.");
        Empregado aux = banco.getEmpregado(sid);

        if(aux == null){
            throw new EmpregadoNaoExisteException();
        }

        EmpregadoHora func = conversaoHorista(aux);

        LocalDate datain = conversaoData(datai, "Data inicial invalida.");
        LocalDate datafn = conversaoData(dataf, "Data final invalida.");

        if(datain.isAfter(datafn)){
            throw new InvalidDataException("Data inicial nao pode ser posterior aa data final.");
        }
        double horas = func.getHoras(datain, datafn);

        return formatarSaida(horas);
    }

    public String getHorasExtrasTrabalhadas(String sid, String datai, String dataf) throws EmpregadoNaoExisteException, InvalidTipeException, NonullException {
        validaString(sid, "Identificacao do empregado nao pode ser nula.");
        Empregado aux = banco.getEmpregado(sid);

        if(aux == null){
            throw new EmpregadoNaoExisteException();
        }

        EmpregadoHora func = conversaoHorista(aux);

        LocalDate datain = conversaoData(datai, "Data inicial invalida.");
        LocalDate datafn = conversaoData(dataf, "Data final invalida.");

        if(datain.isAfter(datafn)){
            throw new InvalidDataException("Data inicial nao pode ser posterior aa data final.");
        }
        double horas = func.getHorasEx(datain, datafn);

        return formatarSaida(horas);


    }

    public String getEmpregadoPorNome(String nome, String indice) throws NonullException {
        validaString(nome,"nome do empregado nao pode ser nula.");
        validaString(indice, "indice nao pode ser nulo");

        String sid = banco.getEmpregnome(nome,indice);

        return sid;
    }


    public String getAtributoEmpregado(String emp, String atribute) throws EmpregadoNaoExisteException, NonullException, InvalidTipeException {

        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.getEmpregado(emp);

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
                EmpregadoComis consul = conversaoComissionado(consulta);
                return String.format("%.2f", consul.getComissao()).replace(".", ",");
            case "sindicalizado":
                return String.valueOf(consulta.isSindicate());
            default:
                throw new InvalidTipeException("Atributo nao existe.");
        }
    }

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

    private LocalDate conversaoData(String dataStr, String mensagemErro) throws InvalidDataException {

        if (dataStr == null || dataStr.isBlank()) {
            throw new InvalidDataException(mensagemErro);
        }
        try {
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(dataStr, formatador);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException(mensagemErro);
        }
    }


    private EmpregadoHora conversaoHorista(Empregado emp) throws InvalidTipeException {
        if (!emp.getTipo().equals("horista")) {
            throw new InvalidTipeException("Empregado nao eh horista.");
        }
        return (EmpregadoHora) emp;
    }



    private EmpregadoComis conversaoComissionado(Empregado emp) throws InvalidTipeException {
        if (!emp.getTipo().equals("comissionado")) {
            throw new InvalidTipeException("Empregado nao eh comissionado.");
        }
        return (EmpregadoComis) emp;
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

    private String formatarSaida(double valor) {
        if (valor % 1 == 0) {
            return String.format("%.0f", valor);
        } else {
            return String.valueOf(valor).replace(".", ",");
        }
    }


}