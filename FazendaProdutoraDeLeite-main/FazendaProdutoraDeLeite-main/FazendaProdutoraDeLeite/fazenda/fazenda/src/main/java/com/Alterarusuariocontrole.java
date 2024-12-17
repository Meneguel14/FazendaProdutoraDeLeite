/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author JoãoHenriqueMeneguel
 */


import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Dao;

/**
 *
 * @author Joao
 */
public class Alterarusuariocontrole {

    @FXML
    private ComboBox<String> comboLogin; // ComboBox para listar os logins

    @FXML
    private TextField campoNome; // Campo para mostrar e editar o nome

    @FXML
    private TextField campoSenha; // Campo para mostrar e editar a senha

    private Dao<Usuario> dao;

    @FXML
    private void initialize() {
        dao = new Dao<>(Usuario.class);
        carregarLogins(); // Preenche a ComboBox com os logins
    }

    private void carregarLogins() {
        try {
            // Busca todos os usuários no banco de dados
            List<Usuario> usuarios = dao.listarTodos();
            ObservableList<String> logins = FXCollections.observableArrayList();

            // Adiciona os logins na lista observável
            for (Usuario usuario : usuarios) {
                logins.add(usuario.getLogin());
            }

            // Configura a ComboBox com a lista de logins
            comboLogin.setItems(logins);

            // Adiciona um listener para detectar mudanças na seleção
            comboLogin.setOnAction(event -> selecionarUsuario());
        } catch (Exception e) {
            mostrarErro("Erro ao carregar os logins: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void selecionarUsuario() {
        String loginSelecionado = comboLogin.getValue();

        if (loginSelecionado != null) {
            try {
                // Busca os dados do usuário pelo login
                Usuario usuario = dao.buscarPorChave("login", loginSelecionado);

                if (usuario != null) {
                    // Preenche os campos com os dados do usuário
                    campoNome.setText(usuario.getNome());
                    campoSenha.setText(usuario.getSenha());
                }
            } catch (Exception e) {
                mostrarErro("Erro ao buscar o usuário: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void salvarAlteracoes() {
        String login = comboLogin.getValue();
        String nome = campoNome.getText();
        String senha = campoSenha.getText();

        if (login == null || nome.isBlank() || senha.isBlank()) {
            mostrarErro("Todos os campos são obrigatórios para salvar as alterações.");
            return;
        }

        try {
            // Cria o objeto Usuario atualizado
            Usuario usuario = new Usuario(login, nome, senha);

            // Atualiza o usuário no banco de dados
            dao.alterar("login", login, usuario);

            mostrarSucesso("Dados do usuário alterados com sucesso!");
            limparCampos();
            carregarLogins(); // Atualiza a ComboBox após salvar
        } catch (Exception e) {
            mostrarErro("Erro ao salvar alterações: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
   private void excluirUsuario() {
    String loginSelecionado = comboLogin.getValue();

    if (loginSelecionado == null) {
        mostrarErro("Selecione um usuário para excluir.");
        return;
    }

    try {
        // Exclui o usuário do banco de dados usando o login como chave
        dao.excluir("login", loginSelecionado);

        mostrarSucesso("Usuário excluído com sucesso!");
        limparCampos();
        carregarLogins(); // Atualiza a lista de logins na ComboBox
    } catch (Exception e) {
        mostrarErro("Erro ao excluir o usuário: " + e.getMessage());
        e.printStackTrace();
    }
}


    @FXML
    public void Menu() throws IOException {
        App.setRoot("menu");
    }

    private void limparCampos() {
        campoNome.clear();
        campoSenha.clear();
        comboLogin.getSelectionModel().clearSelection();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }
}
