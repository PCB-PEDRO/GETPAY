package br.ufal.ic.p2.wepayu.Exception;

public class NoRedoException extends RuntimeException {
    public NoRedoException() {
        super("Nao ha comando a refazer.");
    }
}
