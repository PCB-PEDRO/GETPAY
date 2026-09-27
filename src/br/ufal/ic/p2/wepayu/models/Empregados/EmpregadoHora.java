package br.ufal.ic.p2.wepayu.models.Empregados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmpregadoHora extends Empregado {
    private List<CardPoint> cards = new ArrayList<>();

    public EmpregadoHora(String nome, String endereco, String tipo, double salario){

        super(nome, endereco, tipo, salario);
    }

    public EmpregadoHora(){ }

    public List<CardPoint> getCards(){return this.cards;}
    public void setCards(List<CardPoint> cards){this.cards = cards;}

    public double getHoras(LocalDate datain, LocalDate datafn){
         double htotal = 0;

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (CardPoint aux : cards) {
            LocalDate dataCartao = LocalDate.parse(aux.getData(), formatador);

            if (!dataCartao.isBefore(datain) && dataCartao.isBefore(datafn)) {



                htotal += aux.getHoras();
            }
        }
        return htotal;

    }

    public double getHorasEx(LocalDate datain, LocalDate datafn){
        double htotal = 0;

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (CardPoint aux : cards) {
            LocalDate dataCartao = LocalDate.parse(aux.getData(), formatador);

            if (!dataCartao.isBefore(datain) && dataCartao.isBefore(datafn)) {



                htotal += aux.getHorasEx();
            }
        }
        return htotal;

    }

    public void addCard(String data, double horas)
    {

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate novaData = LocalDate.parse(data, formatador);

        for (CardPoint cp : cards) {
            if (cp.getData().equals(novaData)) {

                return;
            }
        }

        double horasEx = 0;

        if(horas > 8){
            horasEx = horas - 8;
            horas = 8;
        }

        this.cards.add(new CardPoint(data, horas, horasEx));

    }

}
