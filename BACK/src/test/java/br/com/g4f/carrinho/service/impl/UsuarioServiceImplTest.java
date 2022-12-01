package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CarrinhoRepository;
import br.com.g4f.carrinho.repository.UsuarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl userService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setPassword("password");
        user.setEmail("email@email.com");
        user.setName("Name");
        user.setPhone("Phone Test");
        user.setAddress("Address Test");
    }

    @Test
    public void createUserTest() {
        when(usuarioRepository.save(user)).thenReturn(user);

        userService.save(user);

        Mockito.verify(usuarioRepository, Mockito.times(2)).save(user);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void createUserExceptionTest() {
        userService.save(user);
    }

    @Test
    public void updateTest() {
        User oldUser = new User();
        oldUser.setEmail("email@test.com");

        when(usuarioRepository.findByEmail(user.getEmail())).thenReturn(oldUser);
        when(usuarioRepository.save(oldUser)).thenReturn(oldUser);

        User userResult = userService.update(user);

        assertThat(userResult.getName(), is(oldUser.getName()));
    }
}
