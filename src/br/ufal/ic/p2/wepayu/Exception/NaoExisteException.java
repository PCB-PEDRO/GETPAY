package br.ufal.ic.p2.wepayu.Exception;

public class NaoExisteException extends RuntimeException {
    public NaoExisteException(String message) {
        super(message);
    }
}
