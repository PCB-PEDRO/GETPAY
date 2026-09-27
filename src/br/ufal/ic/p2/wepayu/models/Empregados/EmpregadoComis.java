package br.ufal.ic.p2.wepayu.models.Empregados;

import br.ufal.ic.p2.wepayu.models.Empregados.Cards.CardSale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmpregadoComis extends Empregado{

    private double comissao;
    private List<CardSale> vendas = new ArrayList<>();

    public EmpregadoComis(String nome, String endereco, String tipo, double salario, double comissao){

        super(nome, endereco, tipo, salario);
        this.comissao = comissao;
    }

    public EmpregadoComis(){ }

    public List<CardSale> getVendas(){return this.vendas;}
    public void setVendas(List<CardSale> vendas){this.vendas = vendas;}

    public double getComissao(){return this.comissao;}
    public void setComissao(double comissao){this.comissao = comissao;}

    public double getVendas(LocalDate datain, LocalDate datafn){
        double vtotal = 0;

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (CardSale aux : vendas) {
            LocalDate dataCartao = LocalDate.parse(aux.getData(), formatador);

            if (!dataCartao.isBefore(datain) && dataCartao.isBefore(datafn)) {
                vtotal += aux.getValor();
            }
        }
        return vtotal;

    }

    public void addVenda(String data, double valor)
    {
        this.vendas.add(new CardSale(data, valor));
    }
}
