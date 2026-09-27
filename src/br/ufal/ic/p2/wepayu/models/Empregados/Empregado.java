package br.ufal.ic.p2.wepayu.models.Empregados;


import br.ufal.ic.p2.wepayu.models.Empregados.pagamento.Banco;
import br.ufal.ic.p2.wepayu.models.Empregados.pagamento.Payment;
import br.ufal.ic.p2.wepayu.models.Sindicato.Sindicate;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Empregado {
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private boolean sindicalizado;
    private Sindicate sindicato;
    private Payment metodoPagamento = new Payment("emMaos");
    private String stringDataContrato;
    private String stringUltimoPagamento;
    private double dividaSindicato = 0.0;

    private transient DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

    public Empregado(String nome, String endereco, String tipo, double salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
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

    public boolean isSindicalizado(){return sindicalizado;}
    public void setSindicalizado(boolean sindicate){this.sindicalizado = sindicate;}

    public double getSalario() {
        return salario;
    }
    public void setSalario(double salario) {this.salario = salario;}

    public Sindicate getSindicato() {return sindicato;}
    public void setSindicato(Sindicate sindicato) {this.sindicato = sindicato;}

    public Payment getMetodoPagamento() {return metodoPagamento;}
    public void setMetodoPagamento(Payment metodoPagamento) {this.metodoPagamento = metodoPagamento;}

    @Transient
    public LocalDate getDataContrato() {
        if (stringDataContrato == null || stringDataContrato.isEmpty()) {
            return null;
        }
        return LocalDate.parse(stringDataContrato, formatador);
    }

    @Transient
    public void setDataContrato(LocalDate data) {
        if (data != null) {
            this.stringDataContrato = data.format(formatador);
        } else {
            this.stringDataContrato = null;
        }
    }

    @Transient
    public LocalDate getUltimoPagamento() {
        if (stringUltimoPagamento == null || stringUltimoPagamento.isEmpty()) {
            return null;
        }
        return LocalDate.parse(stringUltimoPagamento, formatador);
    }

    @Transient
    public void setUltimoPagamento(LocalDate data) {
        if (data != null) {
            this.stringUltimoPagamento = data.format(formatador);
        } else {
            this.stringUltimoPagamento = null;
        }
    }

    public double getDividaSindicato() {return dividaSindicato;}
    public void setDividaSindicato(double dividaSindicato) {this.dividaSindicato = dividaSindicato;}

}
