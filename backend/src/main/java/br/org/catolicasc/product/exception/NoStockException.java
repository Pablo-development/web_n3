package br.org.catolicasc.product.exception;


public class NoStockException extends RuntimeException {
    public NoStockException(String mensagem) {
        super("This product does not have enough stock! ");
    }
}
