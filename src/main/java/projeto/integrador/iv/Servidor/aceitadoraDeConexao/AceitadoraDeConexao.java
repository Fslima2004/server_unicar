package projeto.integrador.iv.Servidor.aceitadoraDeConexao;

import java.net.*;
import java.util.*;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;
import projeto.integrador.iv.Servidor.supervisoraDeConexao.SupervisoraDeConexao;

public class AceitadoraDeConexao extends Thread {
    private ServerSocket pedido;
    private Map<String, GrupoCarona> gruposDeCarona;
    private ArrayList<Parceiro> usuariosSemCarona;

    public AceitadoraDeConexao(String porta, Map<String, GrupoCarona> gruposDeCarona,
            ArrayList<Parceiro> usuariosSemCarona)
            throws Exception {
        if (porta == null)
            throw new Exception("Porta ausente");

        try {
            this.pedido = new ServerSocket(Integer.parseInt(porta));
        } catch (Exception erro) {
            throw new Exception("Porta invalida");
        }

        if (gruposDeCarona == null)
            throw new Exception("Usuarios ausentes");

        this.gruposDeCarona = gruposDeCarona;

        if (usuariosSemCarona == null)
            throw new Exception("Usuarios ausentes");

        this.usuariosSemCarona = usuariosSemCarona;
    }

    public void run() {
        for (;;) {
            Socket conexao = null;
            try {
                conexao = this.pedido.accept();
            } catch (Exception erro) {
                System.err.println('n' + "Não há pedido de conexão");
                continue; // pula para proximo loop, já que nao há pedido de conexao
            }

            SupervisoraDeConexao supervisoraDeConexao = null;
            try {
                supervisoraDeConexao = new SupervisoraDeConexao(conexao, gruposDeCarona, usuariosSemCarona);
            } catch (Exception erro) {
                System.err.println("Conexao invalida");
            } // sei que passei parametros corretos para o construtor

            System.out.println("Conexao estabelecida com: " + conexao.getInetAddress().getHostAddress());
            System.out.println("supervisoraDeConexao executando");
            supervisoraDeConexao.start();
        }
    }
}
