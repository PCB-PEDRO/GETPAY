package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.InvalidTipeException;
import br.ufal.ic.p2.wepayu.Exception.NaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.NoNameException;
import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoComis;
import br.ufal.ic.p2.wepayu.models.Empregados.EmpregadoHora;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankEmp {
    private Map<String, Empregado> empregados = new LinkedHashMap<>();
    private int proximoid = 1;

    public Map<String, Empregado> getEmpregados() {
        return this.empregados;
    }

    public void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream("getpaydata.xml");
             XMLEncoder encoder = new XMLEncoder(fos)) {

            DataSave dados = new DataSave(this.empregados, this.proximoid);
            encoder.writeObject(dados);

        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    public void carregarDados() {
        File arquivo = new File("getpaydata.xml");

        if (!arquivo.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(arquivo);
             XMLDecoder decoder = new XMLDecoder(fis)) {

            DataSave dadosLidos = (DataSave) decoder.readObject();

            this.empregados = new LinkedHashMap<>(dadosLidos.getEmpregados());
            this.proximoid = dadosLidos.getProximoid();

        } catch (Exception e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            this.zerar();
        }
    }

    public void setEmpregados(Map<String, Empregado> empregados){
        this.empregados = new LinkedHashMap<>(empregados);

        int maiorId = 0;

        for (String idString : empregados.keySet()) {
            try {
                int numId = Integer.parseInt(idString.replace("Sid", ""));
                if (numId > maiorId) {
                    maiorId = numId;
                }
            } catch (NumberFormatException e) {

            }
        }

        this.proximoid = maiorId + 1;
    }

    public String addfunc(String nome, String endereco, String tipo, double salario) throws InvalidTipeException {

        Empregado novo;

        switch(tipo){
            case "horista":
                novo = new EmpregadoHora(nome, endereco, tipo, salario);
                break;
            case "assalariado":
                novo = new Empregado(nome, endereco,tipo, salario);
                break;
            default:
                throw new InvalidTipeException("Tipo invalido");
        }
        String id = "Sid" + proximoid;

        empregados.put(id, novo);
        proximoid++;

        return id;
    }

    public String adcfunc(String nome, String endereco, String tipo, double salario, double comissao) throws IllegalArgumentException{

        Empregado novo;

        novo = new EmpregadoComis(nome, endereco, tipo, salario, comissao);
        String id = "Sid" + proximoid;

        empregados.put(id, novo);
        proximoid++;

        return id;
    }

    public void removefunc(String sid) throws EmpregadoNaoExisteException {

        Empregado remove = empregados.remove(sid);

        if(remove == null) {
            throw new EmpregadoNaoExisteException();
        }

    }


    public void zerar(){
        empregados.clear();
        proximoid = 1;
        salvarDados();
    }

    public Empregado getEmpregado(String id){

        return empregados.get(id);

    }

    public Empregado getEmpreSind(String sinid){

        for (Empregado emp : empregados.values()) {
            if (emp.getSindicato() != null) {

                if (emp.getSindicato().getIdSindical().equals(sinid)) {
                    return emp;
                }
            }
        }
        return null;
    }

    public String getEmpregnome(String nome, String ind) {
        int indice = Integer.parseInt(ind);
        int cont = 0;

        for (String id : empregados.keySet()) {
            Empregado emp = empregados.get(id);

            if (emp.getNome().equals(nome)) {
                cont++;

                if (cont == indice) {
                    return id;
                }
            }
        }
        throw new NoNameException();
    }

    public void atualizarEmpregado(String empId, Empregado novoEmp) {
        this.empregados.put(empId, novoEmp);
    }
}


