package br.ufal.ic.p2.wepayu.Exception;

public class NoUndoException extends RuntimeException {
    public NoUndoException( ) {
        super("Nao ha comando a desfazer.");
    }
}
