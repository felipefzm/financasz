package br.com.financasz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.financasz.auth.TokenService;
import br.com.financasz.dtos.LoginResponseDTO;
import br.com.financasz.dtos.UserDTO;
import br.com.financasz.exceptions.InvalidLoginException;
import br.com.financasz.exceptions.UserNotFoundException;
import br.com.financasz.models.User;
import br.com.financasz.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        String EncodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(EncodedPassword);
        userRepository.save(user);
        UserDTO responseUser = modelMapper.map(user, UserDTO.class);
        return responseUser;
    }

    @Transactional
    public LoginResponseDTO loginUser(String email, String password) {
        UserDTO responseUser;

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidLoginException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException();
        }

        String token = tokenService.generateToken(user);

        responseUser = modelMapper.map(user, UserDTO.class);
        LoginResponseDTO loginResponse = new LoginResponseDTO(token, responseUser);
        log.info("Usuário autenticado com sucesso: " + email);
        return loginResponse;
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário com email " + email + " não encontrado"));
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return userDTOs;
    }

    @Transactional
    public Page<UserDTO> getAllUsersPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }
@Transactional
public UserDTO updateUser(Long userId, UserDTO userDTO) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

    String newPassword = userDTO.getPassword();    
    userDTO.setPassword(null);

    modelMapper.map(userDTO, user);
    
    if (newPassword != null && !newPassword.isBlank()) {
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    userRepository.save(user);
    return modelMapper.map(user, UserDTO.class);
}

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + userId + " não encontrado"));

        userRepository.delete(user);
    }

}
