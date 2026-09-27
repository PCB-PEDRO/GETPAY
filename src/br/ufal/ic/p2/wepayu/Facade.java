package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.BankEmp;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoHora;
import br.ufal.ic.p2.wepayu.models.Empregados.pagamento.Banco;
import br.ufal.ic.p2.wepayu.models.Empregados.pagamento.Payment;
import br.ufal.ic.p2.wepayu.models.Sindicato.Sindicate;

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

    public void alteraEmpregado(String emp, String atribute, String valor) throws EmpregadoNaoExisteException, NonullException, InvalidTipeException {

        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.getEmpregado(emp);

        if (consulta == null) {
            throw new EmpregadoNaoExisteException();
        }

        switch (atribute) {
            case "nome":
                validaString(valor, "Nome nao pode ser nulo.");
                consulta.setNome(valor);
                break;
            case "endereco":
                validaString(valor, "Endereco nao pode ser nulo.");
                consulta.setEndereco(valor);
                break;
            case "salario":
                validaString(valor, "Salario nao pode ser nulo.");
                double value = conversaoDouble(valor,"Salario deve ser numerico.", "Salario deve ser nao-negativo.");
                consulta.setSalario(value);
                break;
            case "tipo":
                validaString(valor, "Tipo invalido.");

                if (valor.equals("assalariado")) {

                    Empregado novoEmp = new Empregado(consulta.getNome(), consulta.getEndereco(), "assalariado", consulta.getSalario());

                    novoEmp.setSindicato(consulta.getSindicato());
                    novoEmp.setMetodoPagamento(consulta.getMetodoPagamento());

                    banco.atualizarEmpregado(emp, novoEmp);
                }
                else if (valor.equals("horista") || valor.equals("comissionado")) {
                    throw new InvalidTipeException("Tipo invalido.");
                }
                else {
                    throw new InvalidTipeException("Tipo invalido.");
                }
                break;
            case "comissao":
                validaString(valor, "Comissao nao pode ser nula." );
                if (!consulta.getTipo().equals("comissionado")){
                    throw new InvalidTipeException("Empregado nao eh comissionado.");
                }

                double values = conversaoDouble(valor,"Comissao deve ser numerica.", "Comissao deve ser nao-negativa." );
                EmpregadoComis cosul = conversaoComissionado(consulta);

                cosul.setComissao(values);
                break;

            case "sindicalizado":
                validaString(valor,"Valor deve ser true ou false.");
                if(!valor.equals("true") && !valor.equals("false")){
                    throw new InvalidTipeException("Valor deve ser true ou false.");
                }
                if (valor.equals("false")) {
                    consulta.setSindicalizado(false);
                    consulta.setSindicato(null);
                }
                break;
            case "metodoPagamento":
                validaString(valor, "Metodo de pagamento nao pode ser nulo.");

                if (valor.equals("correios")) {
                    consulta.setMetodoPagamento(new Payment("correios"));

                } else if (valor.equals("emMaos")) {
                    consulta.setMetodoPagamento(new Payment("emMaos"));

                } else if (valor.equals("banco")) {
                    throw new InvalidTipeException("Falta Paramentros para este Metodo de pagamento.");
                } else {
                    throw new InvalidTipeException("Metodo de pagamento invalido.");
                }
                break;
            default:
                throw new InvalidTipeException("Atributo nao existe.");
        }
    }

    public void alteraEmpregado(String emp, String atribute, String valor, String alternancia) throws Exception {

        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.getEmpregado(emp);
        if (consulta == null) {
            throw new EmpregadoNaoExisteException();
        }

        validaString(atribute, "Atributo nao existe.");
        if (!atribute.equals("tipo")) {
            throw new InvalidTipeException("Atributo invalido.");
        }

        if (valor.equals("comissionado")) {

            double novaComissao = conversaoDouble(alternancia, "Valor deve ser numerico.", "Comissao nao pode ser negativa.");

            EmpregadoComis novoEmp = new EmpregadoComis(consulta.getNome(), consulta.getEndereco(), "comissionado", consulta.getSalario(), novaComissao);

            novoEmp.setSindicato(consulta.getSindicato());
            novoEmp.setMetodoPagamento(consulta.getMetodoPagamento());

            banco.atualizarEmpregado(emp, novoEmp);

        }

        else if (valor.equals("horista")) {
            double novoSalario = conversaoDouble(alternancia, "Valor deve ser numerico.", "Salario nao pode ser negativo.");

            EmpregadoHora novoEmp = new EmpregadoHora(consulta.getNome(), consulta.getEndereco(), "horista", novoSalario);

            novoEmp.setSindicato(consulta.getSindicato());
            novoEmp.setMetodoPagamento(consulta.getMetodoPagamento());

            banco.atualizarEmpregado(emp, novoEmp);

        } else {
            throw new InvalidTipeException("Tipo invalido.");
        }
    }

    public void alteraEmpregado(String emp, String atribute, String valor, String idSindicato, String taxaSindical) throws EmpregadoNaoExisteException, NonullException, InvalidTipeException, JaEsxisteException {

        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.getEmpregado(emp);
        if (consulta == null) {
            throw new EmpregadoNaoExisteException();
        }

        validaString(atribute,"Atributo nao existe.");
        if(!atribute.equals("sindicalizado")){
            throw new InvalidTipeException("Tipo invalido.");
        }

        validaString(valor,"Valor deve ser true ou false.");
        if(!valor.equals("true") && !valor.equals("false")){
            throw new InvalidTipeException("Valor deve ser true ou false.");
        }
        boolean value = Boolean.parseBoolean(valor);

        validaString(idSindicato, "Identificacao do sindicato nao pode ser nula.");
        Empregado test = banco.getEmpreSind(idSindicato);
        if(test != null){
            throw new JaEsxisteException("Ha outro empregado com esta identificacao de sindicato");
        }
        validaString(taxaSindical, "Taxa sindical nao pode ser nula.");
        double taxa = conversaoDouble(taxaSindical, "Taxa sindical deve ser numerica.", "Taxa sindical deve ser nao-negativa.");

        if(value){
            Sindicate novo = new Sindicate(idSindicato,taxa);
            consulta.setSindicato(novo);
            consulta.setSindicalizado(true);
        } else{
            consulta.setSindicato(null);
        }
    }

    public void alteraEmpregado(String emp, String atribute, String valor, String bank, String agencia, String conta) throws NonullException, EmpregadoNaoExisteException, InvalidTipeException {
        validaString(emp, "Identificacao do empregado nao pode ser nula.");

        Empregado consulta = banco.getEmpregado(emp);
        if (consulta == null) {
            throw new EmpregadoNaoExisteException();
        }

        validaString(atribute,"Atributo nao existe.");
        if(!atribute.equals("metodoPagamento")){
            throw new InvalidTipeException("Tipo invalido.");
        }

        validaString(valor,"Metodo de pagamento invalido.");
        if(!valor.equals("banco")){
            throw new InvalidTipeException("Metodo de pagamento invalido.");
        }

        validaString(bank,"Banco nao pode ser nulo.");
        validaString(agencia,"Agencia nao pode ser nulo.");
        validaString(conta,"Conta corrente nao pode ser nulo.");

        Banco novo = new Banco(valor, bank, agencia, conta);
        consulta.setMetodoPagamento(novo);

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

    public void lancaTaxaServico(String sinid, String sdata, String svalor) throws NonullException, NaoExisteException{
        validaString(sinid, "Identificacao do membro nao pode ser nula.");

        Empregado aux = banco.getEmpreSind(sinid);
        if(aux == null){
            throw new NaoExisteException("Membro nao existe.");
        }
        Sindicate men = aux.getSindicato();

        LocalDate data = conversaoData(sdata, "Data invalida.");
        double valor = conversaoDouble(svalor,"Valor deve ser um numero.", "Valor deve ser positivo." );

        men.addTaxas(sdata, valor);
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

    public void lancaVenda(String sid, String sdata, String svalor) throws NonullException, EmpregadoNaoExisteException, InvalidTipeException {

        validaString(sid, "Identificacao do empregado nao pode ser nula.");

        Empregado aux = banco.getEmpregado(sid);
        if(aux == null){
            throw new EmpregadoNaoExisteException();
        }
        EmpregadoComis func = conversaoComissionado(aux);

        LocalDate data = conversaoData(sdata, "Data invalida.");

        double valor = conversaoDouble(svalor,"Valor deve ser um numero.", "Valor deve ser positivo." );

        func.addVenda(sdata, valor);
    }

    public String getTaxasServico(String sid, String datai, String dataf) throws NonullException, EmpregadoNaoExisteException, InvalidTipeException {
        validaString(sid, "Identificacao do empregado nao pode ser nula.");

        Empregado aux = banco.getEmpregado(sid);
        if (aux == null) {
            throw new EmpregadoNaoExisteException();
        }

        if (aux.getSindicato() == null) {
            throw new InvalidTipeException("Empregado nao eh sindicalizado.");
        }

        Sindicate men = aux.getSindicato();

        LocalDate datain = conversaoData(datai, "Data inicial invalida.");
        LocalDate datafn = conversaoData(dataf, "Data final invalida.");

        if(datain.isAfter(datafn)){
            throw new InvalidDataException("Data inicial nao pode ser posterior aa data final.");
        }
        return String.format("%.2f", men.getTotalTax(datain, datafn)).replace(".", ",");
    }

    public String getVendasRealizadas(String sid, String datai, String dataf) throws NonullException, EmpregadoNaoExisteException, InvalidTipeException {
        validaString(sid, "Identificacao do empregado nao pode ser nula.");
        Empregado aux = banco.getEmpregado(sid);

        if(aux == null){
            throw new EmpregadoNaoExisteException();
        }

        EmpregadoComis func = conversaoComissionado(aux);

        LocalDate datain = conversaoData(datai, "Data inicial invalida.");
        LocalDate datafn = conversaoData(dataf, "Data final invalida.");

        if(datain.isAfter(datafn)){
            throw new InvalidDataException("Data inicial nao pode ser posterior aa data final.");
        }
        return String.format("%.2f", func.getVendas(datain, datafn)).replace(".", ",");
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
                if(!consulta.getTipo().equals("comissionado")){
                    throw new InvalidTipeException("Empregado nao eh comissionado.");
                }

                EmpregadoComis consul = conversaoComissionado(consulta);
                return String.format("%.2f", consul.getComissao()).replace(".", ",");
            case "sindicalizado":
                return String.valueOf(consulta.isSindicalizado());
            case "idSindicato":
                if (consulta.getSindicato() == null) {
                    throw new InvalidTipeException("Empregado nao eh sindicalizado.");
                }
                return consulta.getSindicato().getIdSindical();
            case "taxaSindical":
                if (consulta.getSindicato() == null) {
                    throw new InvalidTipeException("Empregado nao eh sindicalizado.");
                }

                double taxa = consulta.getSindicato().getTaxaSindical();

                return String.format("%.2f", taxa).replace(".", ",");
            case"metodoPagamento":
                return consulta.getMetodoPagamento().getTipo();
            case "banco":
                Payment bank = consulta.getMetodoPagamento();

                if (!bank.getTipo().equals("banco")) {
                    throw new InvalidTipeException("Empregado nao recebe em banco.");
                }

                Banco b = (Banco) bank;

                return b.getBanco();
            case "agencia":
                Payment pagAgencia = consulta.getMetodoPagamento();

                if (!pagAgencia.getTipo().equals("banco")) {
                    throw new InvalidTipeException("Empregado nao recebe em banco.");
                }

                Banco ag = (Banco) pagAgencia;
                return ag.getAgencia();

            case "contaCorrente":
                Payment pagConta = consulta.getMetodoPagamento();

                if (!pagConta.getTipo().equals("banco")) {
                    throw new InvalidTipeException("Empregado nao recebe em banco.");
                }

                Banco cc = (Banco) pagConta;
                return cc.getConta();
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