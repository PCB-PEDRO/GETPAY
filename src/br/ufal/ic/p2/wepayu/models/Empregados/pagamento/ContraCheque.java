package br.ufal.ic.p2.wepayu.models.Empregados.pagamento;

public class ContraCheque {
    private String nome;
    private double bruto;
    private double descontos;
    private double sliquido;
    private String metodoFormatado;

    private int horasNormais;
    private int horasExtras;

    private double salarioFixo;
    private double vendas;
    private double comissao;

    public ContraCheque(String nome, int horasNormais, int horasExtras, double bruto, double descontos, double sliquido, String metodo) {
        this.nome = nome;
        this.horasNormais = horasNormais;
        this.horasExtras = horasExtras;
        this.bruto = bruto;
        this.descontos = descontos;
        this.sliquido = sliquido;
        this.metodoFormatado = metodo;
    }

    public ContraCheque(String nome, double bruto, double descontos, double sliquido, String metodo) {
        this.nome = nome;
        this.bruto = bruto;
        this.descontos = descontos;
        this.sliquido = sliquido;
        this.metodoFormatado = metodo;
    }

    public ContraCheque(String nome, double salarioFixo, double vendas, double comissao, double bruto, double descontos, double sliquido, String metodo) {
        this.nome = nome;
        this.salarioFixo = salarioFixo;
        this.vendas = vendas;
        this.comissao = comissao;
        this.bruto = bruto;
        this.descontos = descontos;
        this.sliquido = sliquido;
        this.metodoFormatado = metodo;
    }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public double getBruto() {return bruto;}
    public void setBruto(double bruto) {this.bruto = bruto;}

    public double getDescontos() {return descontos;}
    public void setDescontos(double descontos) {this.descontos = descontos;}

    public double getSliquido() {return sliquido;}
    public void setSliquido(double sliquido) {this.sliquido = sliquido;}

    public String getMetodoFormatado() {return metodoFormatado;}
    public void setMetodoFormatado(String metodoFormatado) {this.metodoFormatado = metodoFormatado;}

    public int getHorasNormais() {return horasNormais;}
    public void setHorasNormais(int horasNormais) {this.horasNormais = horasNormais;}

    public int getHorasExtras() {return horasExtras;}
    public void setHorasExtras(int horasExtras) {this.horasExtras = horasExtras;}

    public double getSalarioFixo() {return salarioFixo;}
    public void setSalarioFixo(double salarioFixo) {this.salarioFixo = salarioFixo;}

    public double getVendas() {return vendas;}
    public void setVendas(double vendas) {this.vendas = vendas;}

    public double getComissao() {return comissao;}
    public void setComissao(double comissao) {this.comissao = comissao;}
}