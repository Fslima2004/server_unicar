package projeto.integrador.iv.Servidor.parceiro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoDeDesligamento;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoDeCarona;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoInexistente;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoCaronaCancelada;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoSaida;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;

public class Parceiro {
    private Socket conexao;
    private ObjectInputStream receptor;
    private ObjectOutputStream transmissor;
    private String idUsuario;
    private String idGrupo;

    private Comunicado proximoComunicado = null;

    private Semaphore mutEx = new Semaphore(1, true);

    public Parceiro(Socket conexao,
            ObjectInputStream receptor,
            ObjectOutputStream transmissor)
            throws Exception // se parametro nulos
    {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        if (receptor == null)
            throw new Exception("Receptor ausente");

        if (transmissor == null)
            throw new Exception("Transmissor ausente");

        this.conexao = conexao;
        this.receptor = receptor;
        this.transmissor = transmissor;
    }

    public void receba(Comunicado x) throws Exception {
        try {

            this.transmissor.writeObject(x.toJson().toString());
            // this.transmissor.writeObject(x);
            this.transmissor.flush();
        } catch (IOException erro) {
            throw new Exception("Erro de transmissao");
        }
    }

    public void setIdUsuario(String id) {
        this.idUsuario = id;
    }

    public String getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdGrupo(String id) {
        this.idGrupo = id;
    }

    public String getIdGrupo() {
        return this.idGrupo;
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
            this.mutEx.acquireUninterruptibly();
            if (this.proximoComunicado == null) {
                String aux = (String) receptor.readObject();
                JSONObject auxJson = new JSONObject(aux);
                this.proximoComunicado = (Comunicado) getComunicadoCorrespondente(auxJson);
                // this.proximoComunicado = (Comunicado) receptor.readObject();

            }
            this.mutEx.release();
            return this.proximoComunicado;
        } catch (Exception erro) {
            // print error message
            System.err.println(erro.getMessage());
            throw new Exception("Erro de recepcao");
        }
    }

    public Comunicado envie() throws Exception {
        try {
            if (this.proximoComunicado == null)
                this.proximoComunicado = (Comunicado) this.receptor.readObject();
            Comunicado ret = this.proximoComunicado;
            this.proximoComunicado = null;
            return ret;
        } catch (Exception erro) {
            throw new Exception("Erro de recepcao");
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
