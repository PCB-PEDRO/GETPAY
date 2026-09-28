package br.ufal.ic.p2.wepayu.models.Empregados;

import br.ufal.ic.p2.wepayu.models.Empregados.pagamento.Payment;
import br.ufal.ic.p2.wepayu.models.Sindicato.Sindicate;

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

    public Empregado(String nome, String endereco, String tipo, double salario){
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
    }

    public Empregado(){ }

    public String getNome() { return nome; }
    public void setNome(String nome){ this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco){ this.endereco = endereco; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo){ this.tipo = tipo; }

    public boolean isSindicalizado(){ return sindicalizado; }
    public void setSindicalizado(boolean sindicalizado){ this.sindicalizado = sindicalizado; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Sindicate getSindicato() { return sindicato; }
    public void setSindicato(Sindicate sindicato) { this.sindicato = sindicato; }

    public Payment getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(Payment metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public double getDividaSindicato() { return dividaSindicato; }
    public void setDividaSindicato(double dividaSindicato) { this.dividaSindicato = dividaSindicato; }


    public String getStringDataContrato() {
        return stringDataContrato;
    }

    public void setStringDataContrato(String stringDataContrato) {
        this.stringDataContrato = stringDataContrato;
    }

    public String getStringUltimoPagamento() {
        return stringUltimoPagamento;
    }

    public void setStringUltimoPagamento(String stringUltimoPagamento) {
        this.stringUltimoPagamento = stringUltimoPagamento;
    }

    @java.beans.Transient
    public LocalDate getDataContrato() {
        if (stringDataContrato == null || stringDataContrato.isEmpty()) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(stringDataContrato, fmt);
    }

    @java.beans.Transient
    public void setDataContrato(LocalDate data) {
        if (data != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
            this.stringDataContrato = data.format(fmt);
        } else {
            this.stringDataContrato = null;
        }
    }

    @java.beans.Transient
    public LocalDate getUltimoPagamento() {
        if (stringUltimoPagamento == null || stringUltimoPagamento.isEmpty()) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(stringUltimoPagamento, fmt);
    }

    @java.beans.Transient
    public void setUltimoPagamento(LocalDate data) {
        if (data != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
            this.stringUltimoPagamento = data.format(fmt);
        } else {
            this.stringUltimoPagamento = null;
        }
    }
}