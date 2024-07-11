package com.forum.exception;

public class ValidacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }

    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public ValidacaoException(Throwable causa) {
        super(causa);
    }
}
