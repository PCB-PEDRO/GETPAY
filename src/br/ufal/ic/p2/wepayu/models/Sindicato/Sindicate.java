package br.ufal.ic.p2.wepayu.models.Sindicato;

import br.ufal.ic.p2.wepayu.models.Empregados.Cards.CardSale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Sindicate {
    private String idSindical;
    private double taxaSindical;
    private List<ServiceTax> taxas = new ArrayList<>();

    public Sindicate(String idSindical, double taxaSindical) {
        this.idSindical = idSindical;
        this.taxaSindical = taxaSindical;
    }

    public Sindicate(){ }

    public String getIdSindical() {return idSindical;}
    public void setIdSindical(String idSindical) {this.idSindical = idSindical;}

    public double getTaxaSindical() {return taxaSindical;}
    public void setTaxaSindical(double taxaSindical) {this.taxaSindical = taxaSindical;}

    public List<ServiceTax> getTaxas() {return taxas;}
    public void setTaxas(List<ServiceTax> taxas) {this.taxas = taxas;}

    public double getTotalTax(LocalDate datain, LocalDate datafn){
        double taxtotal = 0;

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (ServiceTax aux : taxas) {
            LocalDate dataTax = LocalDate.parse(aux.getData(), formatador);

            if (!dataTax.isBefore(datain) && dataTax.isBefore(datafn)) {
                taxtotal += aux.getValor();
            }
        }
        return taxtotal;
    }

    public void addTaxas(String data, double valor){
        this.taxas.add(new ServiceTax(data,valor));
    }
}
