package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.models.Empregados.Empregado;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSave {

    private Map<String, Empregado> empregados = new LinkedHashMap<>();
    private int proximoid;

    public DataSave() {}

    public DataSave(Map<String, Empregado> empregados, int proximoid) {
        this.empregados = new LinkedHashMap<>(empregados);
        this.proximoid = proximoid;
    }
    public Map<String, Empregado> getEmpregados() { return empregados; }
    public void setEmpregados(Map<String, Empregado> empregados) { this.empregados = empregados; }

    public int getProximoid() { return proximoid; }
    public void setProximoid(int proximoid) { this.proximoid = proximoid; }
}
