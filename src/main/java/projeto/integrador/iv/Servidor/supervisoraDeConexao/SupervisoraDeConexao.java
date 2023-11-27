package projeto.integrador.iv.Servidor.supervisoraDeConexao;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoInexistente;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoDeCarona;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;

public class SupervisoraDeConexao extends Thread {
    private Parceiro usuario;
    private Socket conexao;
    private Map<String, GrupoDeCarona> gruposDeCarona;

    public SupervisoraDeConexao(Socket conexao, Map<String, GrupoDeCarona> gruposDeCarona)
            throws Exception {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        if (gruposDeCarona == null)
            throw new Exception("gruposDeCarona ausentes");

        this.conexao = conexao;
        this.gruposDeCarona = gruposDeCarona;
    }

    public void run() {
        ObjectOutputStream transmissor;
        try {
            transmissor = new ObjectOutputStream(
                    this.conexao.getOutputStream());
        } catch (Exception erro) {
            return;
        }

        ObjectInputStream receptor = null;
        try {
            receptor = new ObjectInputStream(
                    this.conexao.getInputStream());
        } catch (Exception err0) {
            try {
                transmissor.close();
            } catch (Exception falha) {
            } // so tentando fechar antes de acabar a thread

            return;
        }

        try {
            this.usuario = new Parceiro(this.conexao,
                    receptor,
                    transmissor);
        } catch (Exception erro) {
        } // sei que passei os parametros corretos

        try {
            for (;;) {
                Comunicado comunicado = this.usuario.envie();

                if (comunicado == null)
                    return;
                else if (comunicado instanceof PedidoCriarGrupoDeCarona) {
                    tratarPedidoCriarGrupoDeCarona((PedidoCriarGrupoDeCarona) comunicado);
                } else if (comunicado instanceof PedidoEntrarNoGrupoDeCarona) {
                    tratarPedidoEntrarNoGrupoDeCarona((PedidoEntrarNoGrupoDeCarona) comunicado);
                } else if (comunicado instanceof PedidoSairDoGrupoDeCarona) {
                    tratarPedidoSairDoGrupoDeCarona((PedidoSairDoGrupoDeCarona) comunicado);
                }
            }
        } catch (Exception erro) {
            try {
                transmissor.close();
                receptor.close();
            } catch (Exception falha) {
            } // so tentando fechar antes de acabar a thread

            return;
        }
    }

    private void tratarPedidoCriarGrupoDeCarona(PedidoCriarGrupoDeCarona pedidoCriarGrupo) {

        System.out.println("recebido pedido de criar grupo de carona");

        if (this.gruposDeCarona.containsKey(pedidoCriarGrupo.getIdDoGrupoDeCarona())) {
            System.out.println("grupo de carona " + pedidoCriarGrupo.getIdDoGrupoDeCarona() + " já existe");

            try {
                this.usuario.receba(new ComunicadoGrupoJaExiste());
            } catch (Exception erro) {
                // sei que passei os parametros corretos
            }
            return;
        }
        this.usuario.setIdUsuario(pedidoCriarGrupo.getIdDoSolicitante());
        this.usuario.setIdGrupo(pedidoCriarGrupo.getIdDoGrupoDeCarona());
        System.out.println("criando grupo de carona " + pedidoCriarGrupo.getIdDoGrupoDeCarona()
                + " para o usuario " + pedidoCriarGrupo.getIdDoSolicitante());
        synchronized (this.gruposDeCarona) {
            this.gruposDeCarona.put(pedidoCriarGrupo.getIdDoGrupoDeCarona(),
                    new GrupoDeCarona(pedidoCriarGrupo.getIdDoGrupoDeCarona(), this.usuario));
        }
    }

    private void tratarPedidoEntrarNoGrupoDeCarona(PedidoEntrarNoGrupoDeCarona pedidoEntrarNoGrupo) {
        System.out.println("recebido pedido para entrar no grupo de carona");
        if (!this.gruposDeCarona.containsKey(pedidoEntrarNoGrupo.getIdDoGrupoDeCarona())) {
            System.out.println("grupo de carona " + pedidoEntrarNoGrupo.getIdDoGrupoDeCarona() + " não existe");

            try {
                this.usuario.receba(new ComunicadoGrupoInexistente());
            } catch (Exception erro) {
                // sei que passei os parametros corretos
            }
            return;
        }
        this.usuario.setIdUsuario(pedidoEntrarNoGrupo.getIdDoSolicitante());
        this.usuario.setIdGrupo(pedidoEntrarNoGrupo.getIdDoGrupoDeCarona());

        System.out.println("entrando no grupo de carona " + pedidoEntrarNoGrupo.getIdDoGrupoDeCarona()
                + " para o usuario " + pedidoEntrarNoGrupo.getIdDoSolicitante());
        synchronized (this.gruposDeCarona) {
            this.gruposDeCarona.get(usuario.getIdGrupo()).addMembro(this.usuario);

            System.out.println(this.gruposDeCarona.get(usuario.getIdGrupo()).toString());
        }
    }

    private void tratarPedidoSairDoGrupoDeCarona(PedidoSairDoGrupoDeCarona pedidoSairDoGrupo) throws Exception {
        System.out.println("recebido pedido para sair do grupo de carona");
        synchronized (this.gruposDeCarona) {

            System.out.println("removendo membro " + usuario.getIdUsuario());
            GrupoDeCarona grupoDeCarona = this.gruposDeCarona.get(usuario.getIdGrupo());
            grupoDeCarona.removeMembro(this.usuario);

            if (grupoDeCarona.isEmpty()) {
                this.gruposDeCarona.remove(usuario.getIdGrupo());
            }

            System.out.println(this.gruposDeCarona.get(usuario.getIdGrupo()).toString());
        }

        // deve validar aqui se o usuario que saiu era o criador do grupo
        this.usuario.adeus(); // encerra as conexões com o usuario
    }

}
