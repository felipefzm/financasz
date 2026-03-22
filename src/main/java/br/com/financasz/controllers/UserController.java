package br.com.financasz.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.financasz.config.RestControllerPath;
import br.com.financasz.dtos.LoginResponseDTO;
import br.com.financasz.dtos.UserDTO;
import br.com.financasz.dtos.UserLoginDTO;
import br.com.financasz.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(RestControllerPath.USERS)
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints relacionados a usuários e autenticação/Endpoints related to users and authentication")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Registrar novo usuário/Register new User", description = "Cria um novo usuário no sistema/Creates a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso" + "/User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos" + "/Invalid input data")
    })
    @PostMapping(RestControllerPath.REGISTER)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @Operation(summary = "Login", description = "Autentica o usuário e retorna seus dados básicos/Authenticates the user and returns their basic data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso/Login successful"),
        @ApiResponse(responseCode = "401", description = "E-mail ou senha incorretos/Invalid email or password")
    })
    @PostMapping(RestControllerPath.LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginDTO loginDTO) {
        LoginResponseDTO loginResponse = userService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @Operation(summary = "Listar todos os usuários/List all users", description = "Retorna uma lista com todos os usuários do sistema/Returns a list of all users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso/Users list successfully returned"),
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

       @Operation(summary = "Listar todos os usuários paginados / List all users paged", description = "Retorna uma lista com todos os usuários do sistema com paginação / It returns a list of all users in the system with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso / Users list successfully returned"),
    })
    @GetMapping(RestControllerPath.PAGED)
    public ResponseEntity<Page<UserDTO>> getAllUsersPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsersPaged(page, size));
    }

    @Operation(summary = "Atualizar usuário por ID/Update user by ID", description = "Atualiza os dados de um usuário existente/Updates an existing user's data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso/User successfully updated"),
        @ApiResponse(responseCode = "404", description = "Usuário não localizado/User not found")
    })
    @PutMapping(RestControllerPath.ID)
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Operation(summary = "Excluir usuário por ID/Delete user by ID", description = "Remove um usuário existente/Delete an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso/User successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Usuário não localizado/User not found")
    })
    @DeleteMapping(RestControllerPath.ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
