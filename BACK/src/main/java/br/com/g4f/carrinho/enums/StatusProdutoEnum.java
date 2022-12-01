package br.com.g4f.carrinho.enums;

import lombok.Getter;

@Getter
public enum StatusProdutoEnum implements CodeEnum{
    UP(0, "Disponível"),
    DOWN(1, "Indosponível")
    ;
    private Integer code;
    private String message;

    StatusProdutoEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getStatus(Integer code) {

        for(StatusProdutoEnum statusEnum : StatusProdutoEnum.values()) {
            if(statusEnum.getCode() == code) return statusEnum.getMessage();
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }
}
