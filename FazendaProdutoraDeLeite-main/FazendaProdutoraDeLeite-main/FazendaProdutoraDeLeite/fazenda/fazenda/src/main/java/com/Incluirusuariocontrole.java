/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import modelo.Usuario;
import util.Dao;
/**
 *
 * @author Joao
 */
public class Incluirusuariocontrole {

    @FXML
    private AnchorPane menu;
    @FXML
    private TextField login;
    @FXML
    private TextField nome;
    @FXML
    private PasswordField senha;
    
    
 

    private Dao<Usuario> usuarioDao;

    public Incluirusuariocontrole() {
        try {
            usuarioDao = new Dao<>(Usuario.class);
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao conectar ao banco de dados.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalvar(ActionEvent event) {
        String login = this.login.getText();
        String nome = this.nome.getText();
        String senha = this.senha.getText();

        // Validação de campos vazios
        if (login.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Todos os campos são obrigatórios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Verifica se o login já existe
            Usuario existente = usuarioDao.buscarPorChave("login", login);
            if (existente != null) {
                mostrarAlerta("Erro", "Este login já está em uso.", Alert.AlertType.ERROR);
                return;
            }

            // Cria um novo usuário e salva no banco
            Usuario novoUsuario = new Usuario(login, nome, senha);
            usuarioDao.inserir(novoUsuario);

            mostrarAlerta("Sucesso", "Usuário registrado com sucesso!", Alert.AlertType.INFORMATION);

            // Opcional: Limpa os campos após salvar
            limparCampos();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar usuário: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        login.clear();
        nome.clear();
        senha.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void voltarMenu(ActionEvent event)throws IOException {
        App.setRoot("menu");
    }
    
}
