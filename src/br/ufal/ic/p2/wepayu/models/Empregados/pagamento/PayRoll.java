package br.ufal.ic.p2.wepayu.models.Empregados.pagamento;

import br.ufal.ic.p2.wepayu.Exception.InvalidDataException;
import br.ufal.ic.p2.wepayu.models.BankEmp;
import br.ufal.ic.p2.wepayu.models.Empregados.Cards.CardPoint;
import br.ufal.ic.p2.wepayu.models.Empregados.Cards.CardSale;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoHora;
import br.ufal.ic.p2.wepayu.models.Sindicato.ServiceTax;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PayRoll {
    private LocalDate data;
    private String saida;

    public PayRoll(LocalDate data, String saida) {
        this.data = data;
        this.saida = saida;
    }

    public void pagar(BankEmp banco) throws Exception {

        List<Empregado> todos = new ArrayList<>(banco.getEmpregados().values());
        todos.sort(Comparator.comparing(Empregado::getNome));

        List<Empregado> assa = separar(todos, "assalariado");
        List<Empregado> comis = separar(todos, "comissionado");
        List<Empregado> horis = separar(todos, "horista");

        List<ContraCheque> holeritesHoristas = new ArrayList<>();
        List<ContraCheque> holeritesComissionado = new ArrayList<>();
        List<ContraCheque> holeritesAssalariado = new ArrayList<>();



        for (Empregado emp : assa) {
            if (!deveReceberAssalariado(data)) continue;

            double bruto = trunca(emp.getSalario());
            double descontosTotais = trunca(calcularDescontos(emp, data));
            double liquido = bruto - descontosTotais;
            double descontoNoPapel = descontosTotais;

            if (liquido < 0) {
                emp.setDividaSindicato(Math.abs(liquido));
                descontoNoPapel = bruto;
                liquido = 0.0;
            } else {
                emp.setDividaSindicato(0.0);
            }

            String metodo = formatarMetodo(emp);
            emp.setUltimoPagamento(data);
            holeritesAssalariado.add(new ContraCheque(emp.getNome(), bruto, descontoNoPapel, liquido, metodo));
        }

        for (Empregado h : horis) { // Horistas
            EmpregadoHora eh = (EmpregadoHora) h;
            if (data.getDayOfWeek() != DayOfWeek.FRIDAY) continue;

            int horas = somatorioH(eh, data, "Nor");
            int extra = somatorioH(eh, data, "Ex");
            double bruto = trunca((eh.getSalario() * horas) + ((eh.getSalario() * 1.5) * extra));
            double descontosTotais = trunca(calcularDescontos(eh, data));

            double liquido = bruto - descontosTotais;
            double descontoNoPapel = descontosTotais;

            if (liquido < 0) {
                eh.setDividaSindicato(Math.abs(liquido));
                descontoNoPapel = bruto;
                liquido = 0.0;
            } else {
                eh.setDividaSindicato(0.0);
            }

            String metodo = formatarMetodo(eh);
            eh.setUltimoPagamento(data);
            holeritesHoristas.add(new ContraCheque(eh.getNome(), horas, extra, bruto, descontoNoPapel, liquido, metodo));
        }

        for (Empregado c : comis) { //comissionados
            EmpregadoComis ec = (EmpregadoComis) c;
            if (!deveReceberComissionado(ec, data)) continue;

            double fixo = trunca((ec.getSalario() * 12.0) / 26.0);
            double vendas = somatorioVendas(ec, data);
            double valorComissao = trunca(vendas * ec.getComissao());

            double bruto = trunca(fixo + valorComissao);
            double descontosTotais = trunca(calcularDescontos(ec, data));

            double liquido = bruto - descontosTotais;
            double descontoNoPapel = descontosTotais;

            if (liquido < 0) {
                ec.setDividaSindicato(Math.abs(liquido));
                descontoNoPapel = bruto;
                liquido = 0.0;
            } else {
                ec.setDividaSindicato(0.0);
            }

            String metodo = formatarMetodo(ec);
            ec.setUltimoPagamento(data);
            holeritesComissionado.add(new ContraCheque(ec.getNome(), fixo, vendas, valorComissao, bruto, descontoNoPapel, liquido, metodo));
        }

        imprimirFolha(holeritesHoristas, holeritesAssalariado, holeritesComissionado);
    }

    public double getTotalFolha(BankEmp banco, LocalDate dataConsulta) {
        List<Empregado> todos = new ArrayList<>(banco.getEmpregados().values());
        List<Empregado> assa = separar(todos, "assalariado");
        List<Empregado> comis = separar(todos, "comissionado");
        List<Empregado> horis = separar(todos, "horista");
        double totalGeral = 0.0;

        for (Empregado emp : assa) {
            if (!deveReceberAssalariado(dataConsulta)) continue;
            totalGeral += trunca(emp.getSalario());
        }

        for (Empregado h : horis) {
            EmpregadoHora eh = (EmpregadoHora) h;
            if (dataConsulta.getDayOfWeek() != DayOfWeek.FRIDAY) continue;
            int horas = somatorioH(eh, dataConsulta, "Nor");
            int extra = somatorioH(eh, dataConsulta, "Ex"); totalGeral += trunca((eh.getSalario() * horas) + ((eh.getSalario() * 1.5) * extra));
        }

        for (Empregado c : comis) {
            EmpregadoComis ec = (EmpregadoComis) c;
            if (!deveReceberComissionado(ec, dataConsulta)) continue;
            double fixo = trunca((ec.getSalario() * 12.0) / 26.0);
            double vendas = somatorioVendas(ec, dataConsulta);
            double valorComissao = trunca(vendas * ec.getComissao()); totalGeral += trunca(fixo + valorComissao);
        }
        return totalGeral;
    }

    private void imprimirFolha(List<ContraCheque> holeritesHoristas, List<ContraCheque> holeritesAssalariado, List<ContraCheque> holeritesComissionado) throws Exception {

        String LINHA_COMPLETA = "===============================================================================================================================";

        try (PrintWriter writer = new PrintWriter(new FileWriter(this.saida))) {

            writer.println("FOLHA DE PAGAMENTO DO DIA " + this.data.toString());
            writer.println("====================================");
            writer.println();

            writer.println(LINHA_COMPLETA);
            writer.println("===================== HORISTAS ================================================================================================");
            writer.println(LINHA_COMPLETA);
            writer.println("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo");
            writer.println("==================================== ===== ===== ============= ========= =============== ======================================");

            int somatorioHoras = 0, somatorioExtra = 0;
            double somatorioBrutoH = 0, somatorioDescH = 0, somatorioLiqH = 0;

            for (ContraCheque ch : holeritesHoristas) {somatorioHoras += ch.getHorasNormais(); somatorioExtra += ch.getHorasExtras();somatorioBrutoH += ch.getBruto(); somatorioDescH += ch.getDescontos(); somatorioLiqH += ch.getSliquido();

                writer.println(String.format("%-36s %5s %5s %13s %9s %15s %s",
                        ch.getNome(), ch.getHorasNormais(), ch.getHorasExtras(),
                        fmt(ch.getBruto()), fmt(ch.getDescontos()), fmt(ch.getSliquido()), ch.getMetodoFormatado()));
            }
            writer.println();
            writer.println(String.format("%-36s %5s %5s %13s %9s %15s",
                    "TOTAL HORISTAS", somatorioHoras, somatorioExtra, fmt(somatorioBrutoH), fmt(somatorioDescH), fmt(somatorioLiqH)));
            writer.println();

            writer.println(LINHA_COMPLETA);
            writer.println("===================== ASSALARIADOS ============================================================================================");
            writer.println(LINHA_COMPLETA);
            writer.println("Nome                                             Salario Bruto Descontos Salario Liquido Metodo");
            writer.println("================================================ ============= ========= =============== ======================================");

            double somatorioBrutoA = 0, somatorioDescA = 0, somatorioLiqA = 0;

            for (ContraCheque ch : holeritesAssalariado) {somatorioBrutoA += ch.getBruto(); somatorioDescA += ch.getDescontos(); somatorioLiqA += ch.getSliquido();

                writer.println(String.format("%-48s %13s %9s %15s %s",
                        ch.getNome(), fmt(ch.getBruto()), fmt(ch.getDescontos()), fmt(ch.getSliquido()), ch.getMetodoFormatado()));
            }
            writer.println();
            writer.println(String.format("%-48s %13s %9s %15s",
                    "TOTAL ASSALARIADOS", fmt(somatorioBrutoA), fmt(somatorioDescA), fmt(somatorioLiqA)));
            writer.println();

            writer.println(LINHA_COMPLETA);
            writer.println("===================== COMISSIONADOS ===========================================================================================");
            writer.println(LINHA_COMPLETA);
            writer.println("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo");
            writer.println("===================== ======== ======== ======== ============= ========= =============== ======================================");

            double somaFixo = 0, somaVendas = 0, somaComis = 0, somaBrutoC = 0, somaDescC = 0, somaLiqC = 0;

            for (ContraCheque ch : holeritesComissionado) {somaFixo += ch.getSalarioFixo(); somaVendas += ch.getVendas(); somaComis += ch.getComissao();somaBrutoC += ch.getBruto(); somaDescC += ch.getDescontos(); somaLiqC += ch.getSliquido();

                writer.println(String.format("%-21s %8s %8s %8s %13s %9s %15s %s",
                        ch.getNome(), fmt(ch.getSalarioFixo()), fmt(ch.getVendas()), fmt(ch.getComissao()),
                        fmt(ch.getBruto()), fmt(ch.getDescontos()), fmt(ch.getSliquido()), ch.getMetodoFormatado()));
            }
            writer.println();
            writer.println(String.format("%-21s %8s %8s %8s %13s %9s %15s",
                    "TOTAL COMISSIONADOS", fmt(somaFixo), fmt(somaVendas), fmt(somaComis), fmt(somaBrutoC), fmt(somaDescC), fmt(somaLiqC)));
            writer.println();

            double totalGeralFolha = somatorioBrutoH + somatorioBrutoA + somaBrutoC;
            writer.println("TOTAL FOLHA: " + fmt(totalGeralFolha));

        } catch (IOException e) {
            throw new Exception("Erro ao gerar folha de pagamento.");
        }
    }

    private String fmt(double valor) {
        return String.format("%.2f", valor).replace(".", ",");
    }

    private int somatorioH(EmpregadoHora eh, LocalDate dataFolha, String tipo) {
        int total = 0;

        LocalDate dataInicial = eh.getUltimoPagamento();
        if (dataInicial == null) {
            dataInicial = eh.getDataContrato();
        }

        if (dataInicial == null) {
            return 0;
        }


        for (CardPoint cartao : eh.getCards()) {

            LocalDate dataCartao = conversaoData(cartao.getData(), "data invalida");

            if ((dataCartao.isEqual(dataInicial) || dataCartao.isAfter(dataInicial)) && dataCartao.isBefore(dataFolha)){
                if (tipo.equals("Nor")) {
                    total += cartao.getHoras();
                } else if (tipo.equals("Ex")) {
                    total += cartao.getHorasEx();
                }
            }
        }
        return total;
    }

    private double somatorioVendas(EmpregadoComis ec, LocalDate dataFolha) {
        double totalVendas = 0.0;

        LocalDate dataInicial = ec.getUltimoPagamento();
        if (dataInicial == null) {
            dataInicial = ec.getDataContrato();
        }

        if (dataInicial == null) return 0.0;

        for (CardSale venda : ec.getVendas()) {
            LocalDate dataVenda = conversaoData(venda.getData(), "data invalida");

            if ((dataVenda.isEqual(dataInicial) || dataVenda.isAfter(dataInicial)) && dataVenda.isBefore(dataFolha)) {
                totalVendas += venda.getValor();
            }
        }

        return totalVendas;
    }

    private double calcularDescontos(Empregado e, LocalDate dataFolha) {
        if (e.getSindicato() == null) {
            return 0.0;
        }

        LocalDate dataInicial = e.getUltimoPagamento();
        if (dataInicial == null) {
            dataInicial = e.getDataContrato();
        }
        if (dataInicial == null) return 0.0;

        long diasPassados = ChronoUnit.DAYS.between(dataInicial, dataFolha);

        if (e.getUltimoPagamento() == null) {
            diasPassados += 1;
        }

        double taxaDiaria = e.getSindicato().getTaxaSindical();
        double totalTaxaSindical = diasPassados * taxaDiaria;
        double totalTaxasServico = 0.0;

        for (ServiceTax servico : e.getSindicato().getTaxas()) {
            LocalDate dataServico = conversaoData(servico.getData(), "Data invalida");
            if ((dataServico.isEqual(dataInicial) || dataServico.isAfter(dataInicial)) && dataServico.isBefore(dataFolha)) {
                totalTaxasServico += servico.getValor();
            }
        }

        return totalTaxaSindical + totalTaxasServico + e.getDividaSindicato();
    }

    private boolean deveReceberAssalariado(LocalDate dataFolha) {
        LocalDate ultimoDiaDoMes = dataFolha.with(TemporalAdjusters.lastDayOfMonth());

        if (ultimoDiaDoMes.getDayOfWeek() == DayOfWeek.SATURDAY) {
            ultimoDiaDoMes = ultimoDiaDoMes.minusDays(1);
        } else if (ultimoDiaDoMes.getDayOfWeek() == DayOfWeek.SUNDAY) {
            ultimoDiaDoMes = ultimoDiaDoMes.minusDays(2);
        }
        return dataFolha.isEqual(ultimoDiaDoMes);
    }

    private boolean deveReceberComissionado(Empregado ec, LocalDate dataFolha) {
        if (dataFolha.getDayOfWeek() != DayOfWeek.FRIDAY) {
            return false;
        }

        LocalDate ultimoPgto = ec.getUltimoPagamento();

        if (ultimoPgto != null) {
            long diasPassados = ChronoUnit.DAYS.between(ultimoPgto, dataFolha);
            return diasPassados >= 14;
        } else {
            LocalDate contrato = ec.getDataContrato();
            if (contrato == null) return false;

            long diasDesdeContrato = ChronoUnit.DAYS.between(contrato, dataFolha);
            return diasDesdeContrato >= 13;
        }
    }

    private String formatarMetodo(Empregado emp) {
        Payment pag = emp.getMetodoPagamento();

        if (pag == null) {
            return "Em maos";
        }

        String tipo = pag.getTipo();

        if (tipo.equals("em maos")) {
            return "Em maos";
        }
        else if (tipo.equals("correios")) {
            return "Correios, " + emp.getEndereco();
        }
        else if (tipo.equals("banco")) {
            Banco b = (Banco) pag;
            return b.getBanco() + ", Ag. " + b.getAgencia() + " CC " + b.getConta();
        }

        return "Em maos";
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

    private List<Empregado> separar(List<Empregado> todos, String tipo) {
        List<Empregado> listaSeparada = new ArrayList<>();

        for (Empregado emp : todos) {
            if (emp.getTipo().equals(tipo)) {
                listaSeparada.add(emp);
            }
        }
        return listaSeparada;
    }

    private double trunca(double valor) {
        return Math.floor(valor * 100.0 + 0.001) / 100.0;
    }
}
