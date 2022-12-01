package br.com.g4f.carrinho.enums;

public enum PedidoStatusEnum implements CodeEnum {
    NEW(0, "Novo Pedido"),
    FINISHED(1, "Finalizado"),
    CANCELED(2, "Cancelado");

    private  int code;
    private String msg;

    PedidoStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
