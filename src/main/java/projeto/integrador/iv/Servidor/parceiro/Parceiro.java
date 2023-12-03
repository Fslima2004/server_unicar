package projeto.integrador.iv.Servidor.parceiro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoDeDesligamento;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoCriadoComSucesso;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoDeCarona;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoInexistente;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoCaronaCancelada;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoSaida;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;

public class Parceiro {
    private Socket conexao;
    private BufferedReader receptor;
    private PrintWriter transmissor;
    private Usuario usuario;

    private Comunicado proximoComunicado = null;

    private Semaphore mutEx = new Semaphore(1, true);

    public Parceiro(Socket conexao,
            BufferedReader receptor,
            PrintWriter transmissor)
            throws Exception // se parametro nulos
    {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        // if (receptor == null)
        // throw new Exception("Receptor ausente");

        if (transmissor == null)
            throw new Exception("Transmissor ausente");

        this.conexao = conexao;
        this.receptor = receptor;
        this.transmissor = transmissor;
    }

    public void receba(Comunicado x) throws Exception {
        try {
            this.transmissor.println(x.toJson().toString());
            this.transmissor.flush();
        } catch (Exception erro) {
            throw new Exception("Erro de transmissao");
        }
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario novoUsuario) {
        this.usuario = novoUsuario;
    }

    public Comunicado getComunicadoCorrespondente(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("data");

            switch (json.getString("type")) {
                case "PedidoEntrarNoGrupoDeCarona":
                    return PedidoEntrarNoGrupoDeCarona.fromJson(data);
                case "PedidoCriarGrupoDeCarona":
                    return PedidoCriarGrupoDeCarona.fromJson(data);
                case "PedidoSairDoGrupoDeCarona":
                    return PedidoSairDoGrupoDeCarona.fromJson(data);
                case "ComunicadoGrupoInexistente":
                    return ComunicadoGrupoInexistente.fromJson(data);
                case "ComunicadoGrupoJaExiste":
                    return ComunicadoGrupoJaExiste.fromJson(data);
                case "ComunicadoGrupoDeCarona":
                    return ComunicadoGrupoDeCarona.fromJson(data);
                case "ComunicadoDeDesligamento":
                    return ComunicadoDeDesligamento.fromJson(data);
                case "ComunicadoSaida":
                    return ComunicadoSaida.fromJson(data);
                case "ComunicadoCaronaCancelada":
                    return ComunicadoCaronaCancelada.fromJson(data);
                case "ComunicadoGrupoCriadoComSucesso":
                    return ComunicadoGrupoCriadoComSucesso.fromJson(data);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Comunicado espie() throws Exception {
        try {
            // System.out.println("espie chamado");
            this.mutEx.acquireUninterruptibly();
            if (this.proximoComunicado == null) {
                String aux = this.receptor.readLine();

                if (aux != null) {
                    System.out.println("Received: " + aux);

                    JSONObject auxJson = new JSONObject(aux);

                    this.proximoComunicado = (Comunicado) getComunicadoCorrespondente(auxJson);
                }

            }
            this.mutEx.release();
            return this.proximoComunicado;
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
            throw new Exception("espie: Erro de recepcao");
        }
    }

    public Comunicado envie() throws Exception {
        try {

            System.out.println("envie chamado");
            if (this.proximoComunicado == null) {

                System.out.println("envie: proximoComunicado == null");
                String aux = this.receptor.readLine();

                if (aux != null) {
                    System.out.println("Received: " + aux);

                    JSONObject auxJson = new JSONObject(aux);
                    this.proximoComunicado = (Comunicado) getComunicadoCorrespondente(auxJson);
                }
            }
            Comunicado ret = this.proximoComunicado;
            this.proximoComunicado = null;
            return ret;
        } catch (Exception erro) {
            System.err.println(erro.getMessage());
            throw new Exception("envie: Erro de recepcao");
        }
    }

    public void adeus() throws Exception {
        try {
            this.transmissor.close();
            this.receptor.close();
            this.conexao.close();
        } catch (Exception erro) {
            throw new Exception("Erro de desconexao");
        }
    }
}
