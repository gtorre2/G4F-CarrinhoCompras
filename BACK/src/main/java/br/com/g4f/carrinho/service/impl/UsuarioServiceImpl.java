package br.com.g4f.carrinho.service.impl;


import br.com.g4f.carrinho.entity.Cart;
import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.enums.ResultEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CarrinhoRepository;
import br.com.g4f.carrinho.repository.UsuarioRepository;
import br.com.g4f.carrinho.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@DependsOn("passwordEncoder")
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    CarrinhoRepository carrinhoRepository;

    @Override
    public User findOne(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findByRole(String role) {
        return usuarioRepository.findAllByRole(role);
    }

    @Override
    @Transactional
    public User save(User user) {
        //register
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = usuarioRepository.save(user);

            // initial Cart
            Cart savedCart = carrinhoRepository.save(new Cart(savedUser));
            savedUser.setCart(savedCart);
            return usuarioRepository.save(savedUser);

        } catch (Exception e) {
            throw new ValidacaoCustomizada(ResultEnum.VALID_ERROR);
        }

    }

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = usuarioRepository.findByEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return usuarioRepository.save(oldUser);
    }

}
