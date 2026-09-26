package br.ufal.ic.p2.wepayu.Exception;

public class NoNameException extends RuntimeException {
    public NoNameException() {
        super("Nao ha empregado com esse nome.");
    }
}
