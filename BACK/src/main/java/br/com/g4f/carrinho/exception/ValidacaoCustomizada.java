package br.com.g4f.carrinho.exception;

import br.com.g4f.carrinho.enums.ResultEnum;

public class ValidacaoCustomizada extends RuntimeException {

    private Integer code;

    public ValidacaoCustomizada(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public ValidacaoCustomizada(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
