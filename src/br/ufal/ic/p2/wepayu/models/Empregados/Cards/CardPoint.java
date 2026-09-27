package br.ufal.ic.p2.wepayu.models.Empregados.Cards;

public class CardPoint {

    private String data;
    private double horas;
    private double horasEx;

    public CardPoint(String data, double horas, double horasEx){
        this.data = data;
        this.horas = horas;
        this.horasEx = horasEx;
    }

    public CardPoint(){ }

    public String getData(){return data;}
    public void setData(String data){this.data = data;}

    public double getHoras() {return horas;}
    public void setHoras(double horas) {this.horas = horas;}

    public double getHorasEx() {return horasEx;}
    public void setHorasEx(double horasEx) {this.horasEx = horasEx;}
}
