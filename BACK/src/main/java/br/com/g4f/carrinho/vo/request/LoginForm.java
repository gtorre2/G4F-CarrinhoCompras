package br.com.g4f.carrinho.vo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created By Zhu Lin on 1/1/2019.
 */
@Data
public class LoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
