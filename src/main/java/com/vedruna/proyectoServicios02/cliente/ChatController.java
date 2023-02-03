package com.vedruna.proyectoServicios02.cliente;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ChatController {

    @FXML
    public Button botonEnviar;
    @FXML
    public Label labelNick;
    @FXML
    public TextArea areaMostrador;
    @FXML
    public TextField areaEscritor;

    public void enviar(ActionEvent actionEvent) {

    }
}