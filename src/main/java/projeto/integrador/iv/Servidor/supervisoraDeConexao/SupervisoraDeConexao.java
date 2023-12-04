package projeto.integrador.iv.Servidor.supervisoraDeConexao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoInexistente;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoMeuGrupoCarona;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoTodosGupos;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoMeuGrupoCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoTodosGruposDisponiveis;

public class SupervisoraDeConexao extends Thread {
    private Parceiro cliente;
    private Socket conexao;
    private Map<String, GrupoCarona> gruposDeCarona;
    private ArrayList<Parceiro> usuariosSemCarona;

    void print(String msg) {
        System.out.println(msg);
    }

    public SupervisoraDeConexao(Socket conexao, Map<String, GrupoCarona> gruposDeCarona,
            ArrayList<Parceiro> usuariosSemCarona)
            throws Exception {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        if (gruposDeCarona == null)
            throw new Exception("gruposDeCarona ausentes");

        if (usuariosSemCarona == null)
            throw new Exception("usuariosSemCarona ausentes");

        this.conexao = conexao;
        this.gruposDeCarona = gruposDeCarona;
        this.usuariosSemCarona = usuariosSemCarona;
    }

    public void run() {
        PrintWriter transmissor;
        try {
            transmissor = new PrintWriter(this.conexao.getOutputStream(), true);
        } catch (Exception erro) {
            System.out.println("[ERRO - transmissor]: " + erro.getMessage());
            return;
        }

        print("transmissor ok");

        BufferedReader receptor = null;
        try {
            receptor = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
        } catch (Exception erro) {
            try {
                transmissor.close();
            } catch (Exception falha) {
            } // so tentando fechar antes de acabar a thread

            System.out.println("[ERRO - receptor]: " + erro.getMessage());
            return;
        }

        print("receptor ok");

        try {
            this.cliente = new Parceiro(this.conexao,
                    receptor,
                    transmissor);
        } catch (Exception erro) {
            System.out.println("[ERRO - usuario]: " + erro.getMessage());
        } // sei que passei os parametros corretos

        print("usuario ok");

        try {
            for (;;) {

                Comunicado comunicado;

                do {
                    comunicado = this.cliente.espie();
                } while (!(comunicado instanceof Comunicado));

                comunicado = this.cliente.envie();

                if (comunicado instanceof PedidoCriarGrupoDeCarona) {
                    tratarPedidoCriarGrupoDeCarona((PedidoCriarGrupoDeCarona) comunicado);
                } else if (comunicado instanceof PedidoEntrarNoGrupoDeCarona) {
                    tratarPedidoEntrarNoGrupoDeCarona((PedidoEntrarNoGrupoDeCarona) comunicado);
                } else if (comunicado instanceof PedidoSairDoGrupoDeCarona) {
                    tratarPedidoSairDoGrupoDeCarona((PedidoSairDoGrupoDeCarona) comunicado);
                } else if (comunicado instanceof PedidoMeuGrupoCarona) {
                    tratarPedidoMeuGrupoCarona((PedidoMeuGrupoCarona) comunicado);
                } else if (comunicado instanceof PedidoTodosGruposDisponiveis) {
                    tratarPedidoTodosGruposDisponiveis((PedidoTodosGruposDisponiveis) comunicado);
                } else {
                    System.out.println("comunicado desconhecido");
                }

            }
        } catch (Exception erro) {
            try {
                transmissor.close();
                receptor.close();
            } catch (Exception falha) {
            } // so tentando fechar antes de acabar a thread

            System.out.println("[ERRO - for loop]: " + erro.getMessage());
            return;
        }
    }

    private void tratarPedidoCriarGrupoDeCarona(PedidoCriarGrupoDeCarona pedidoCriarGrupo) {
        System.out.println("recebido pedido de criar grupo de carona");

        if (this.gruposDeCarona.containsKey(pedidoCriarGrupo.getGrupoDeCarona().getIdCarona())) {
            System.out.println("grupo de carona " + pedidoCriarGrupo.getGrupoDeCarona().getIdCarona() + " já existe");

            try {
                this.cliente.receba(new ComunicadoGrupoJaExiste());
            } catch (Exception erro) {
                // sei que passei os parametros corretos
                System.out.println("[ERRO - tratarPedidoCriarGrupoDeCarona]: " + erro.getMessage());
            }
            return;
        }

        Usuario motorista = new Usuario(pedidoCriarGrupo.getGrupoDeCarona().getMotorista());
        motorista.setIdCaronaAtual(pedidoCriarGrupo.getGrupoDeCarona().getIdCarona());

        this.cliente.setUsuario(motorista);

        GrupoCarona grupoDeCarona = new GrupoCarona(pedidoCriarGrupo.getGrupoDeCarona());
        grupoDeCarona.setMotoristaConexao(this.cliente);
        grupoDeCarona.setCallbackAtualizacaoCarona(new Runnable() {
            @Override
            public void run() {
                notificaUsuariosComCaronasAtualizadas();
            }
        });

        System.out.println("motorista: " + motorista.toString());
        System.out.println("idCaronaAtual: " + motorista.getIdCaronaAtual());

        System.out.println("criando grupo de carona " + pedidoCriarGrupo.getGrupoDeCarona().getIdCarona()
                + " para o usuario " + pedidoCriarGrupo.getGrupoDeCarona().getMotorista().getId());
        synchronized (this.gruposDeCarona) {
            this.gruposDeCarona.put(pedidoCriarGrupo.getGrupoDeCarona().getIdCarona(),
                    grupoDeCarona);
        }
    }

    private void tratarPedidoEntrarNoGrupoDeCarona(PedidoEntrarNoGrupoDeCarona pedidoEntrarNoGrupo) {
        System.out.println("recebido pedido para entrar no grupo de carona");
        if (!this.gruposDeCarona.containsKey(pedidoEntrarNoGrupo.getIdGrupoCarona())) {
            System.out.println("grupo de carona " + pedidoEntrarNoGrupo.getIdGrupoCarona() + " não existe");

            try {
                this.cliente.receba(new ComunicadoGrupoInexistente());
            } catch (Exception erro) {
                // sei que passei os parametros corretos
                System.out.println("[ERRO - tratarPedidoEntrarNoGrupoDeCarona]: " + erro.getMessage());
            }
            return;
        }
        Usuario passageiro = new Usuario(pedidoEntrarNoGrupo.getUsuario());
        passageiro.setIdCaronaAtual(pedidoEntrarNoGrupo.getIdGrupoCarona());

        this.cliente.setUsuario(passageiro);

        System.out.println("passageiro: " + passageiro.toString());
        System.out.println("idCaronaAtual: " + passageiro.getIdCaronaAtual());

        // printar usuario do cliente
        System.out.println("usuario do cliente: " + this.cliente.getUsuario().toString());

        System.out.println("entrando no grupo de carona " + pedidoEntrarNoGrupo.getIdGrupoCarona()
                + " para o usuario " + pedidoEntrarNoGrupo.getUsuario().getId());
        synchronized (this.gruposDeCarona) {
            GrupoCarona caronaAtual = this.gruposDeCarona.get(pedidoEntrarNoGrupo.getIdGrupoCarona());

            caronaAtual.setCallbackAtualizacaoCarona(new Runnable() {
                @Override
                public void run() {
                    notificaUsuariosComCaronasAtualizadas();
                }
            });

            caronaAtual.addMembro(this.cliente);

            System.out.println(caronaAtual.toString());
        }
    }

    private void tratarPedidoSairDoGrupoDeCarona(PedidoSairDoGrupoDeCarona pedidoSairDoGrupo) throws Exception {
        System.out.println("recebido pedido para sair do grupo de carona");
        synchronized (this.gruposDeCarona) {

            System.out.println("removendo membro " + cliente.getUsuario().getId());

            // obter grupo de carona atual => adicionar atirbuto na classe Usuario
            // e setar ele quando usuario entrar em um grupo ou for o motorista

            String idGrupo = this.cliente.getUsuario().getIdCaronaAtual();
            GrupoCarona grupoDeCarona = this.gruposDeCarona.get(idGrupo);

            grupoDeCarona.setCallbackAtualizacaoCarona(new Runnable() {
                @Override
                public void run() {
                    notificaUsuariosComCaronasAtualizadas();
                }
            });

            this.cliente.getUsuario().setIdCaronaAtual(null);

            grupoDeCarona.removeMembro(this.cliente);

            if (grupoDeCarona.isEmpty() && grupoDeCarona.isMotoristaAusente()) {
                this.gruposDeCarona.remove(idGrupo);
            }

            // precisa notificar após remover o membro
            // pois pode ser que o grupo sequer exista mais
            notificaUsuariosComCaronasAtualizadas();

            System.out.println(this.gruposDeCarona.get(idGrupo).toString());
        }

        // deve validar aqui se o usuario que saiu era o criador do grupo
        this.cliente.adeus(); // encerra as conexões com o usuario
    }

    private void tratarPedidoMeuGrupoCarona(PedidoMeuGrupoCarona pedidoMeuGrupoCarona) {
        System.out.println("recebido pedido de meu grupo de carona");
        String id = pedidoMeuGrupoCarona.getIdUsuario();

        // printar todas as caronas
        for (GrupoCarona grupoCarona : this.gruposDeCarona.values()) {
            System.out.println(grupoCarona.toString());
        }

        try {
            if (pedidoMeuGrupoCarona.getCategoria().equals(PedidoMeuGrupoCarona.MOTORISTA)) {
                for (GrupoCarona grupoCarona : this.gruposDeCarona.values()) {
                    System.out.println("motorista: " + grupoCarona.getMotorista().getId() + " id recebido: " + id);
                    if (grupoCarona.getMotorista().getId().equals(id)) {
                        this.cliente.setUsuario(grupoCarona.getMotorista());
                        // restaura conexao
                        grupoCarona.setMotoristaConexao(cliente); // atualiza a conexao vigente do motorista
                        System.out.println("motorista encontrado no grupo de carona:" + grupoCarona.getIdCarona());
                        this.cliente.receba(new ComunicadoMeuGrupoCarona(grupoCarona));
                        return;
                    }
                }
            } else if (pedidoMeuGrupoCarona.getCategoria().equals(PedidoMeuGrupoCarona.PASSAGEIRO)) {
                for (GrupoCarona grupoCarona : this.gruposDeCarona.values()) {
                    for (Parceiro passageiro : grupoCarona.getMembros()) {
                        System.out.println("passageiro: " + passageiro.getUsuario().getId() + " id recebido: " + id);
                        if (passageiro.getUsuario().getId().equals(id)) {
                            Parceiro parceiroAtualizado = this.cliente;
                            // restaura conexao
                            parceiroAtualizado.setUsuario(passageiro.getUsuario());
                            grupoCarona.atualizarConexaoMembro(id, parceiroAtualizado);
                            System.out.println("passageiro encontrado no grupo de carona:" + grupoCarona.getIdCarona());
                            this.cliente.receba(new ComunicadoMeuGrupoCarona(grupoCarona));
                            return;
                        }
                    }
                }

            }

            System.out.println("usuario nao encontrado");
            this.cliente.receba(new ComunicadoGrupoInexistente());
        } catch (Exception erro) {
            System.out.println("[ERRO - tratarPedidoMeuGrupoCarona]: " + erro.getMessage());
        }
    }

    private void tratarPedidoTodosGruposDisponiveis(PedidoTodosGruposDisponiveis pedidoTodosGruposDisponiveis) {
        System.out.println("recebido pedido de todos os grupos disponiveis");

        synchronized (this.usuariosSemCarona) {
            usuariosSemCarona.add(this.cliente);
        }
        ComunicadoTodosGupos comunicadoTodosGruposDisponiveis = obterComunicadoTodosGruposDisponiveis(this.cliente.getUsuario().getId());

        try {
            this.cliente.receba(comunicadoTodosGruposDisponiveis);
        } catch (Exception erro) {
            System.out.println("[ERRO - tratarPedidoTodosGruposDisponiveis]: " + erro.getMessage());
        }
    }

    private ComunicadoTodosGupos obterComunicadoTodosGruposDisponiveis(String idUsuario) {
        ArrayList<GrupoCarona> gruposDisponiveis = new ArrayList<GrupoCarona>();

        for (GrupoCarona grupoCarona : this.gruposDeCarona.values()) {
            if (grupoCarona.getVagasTotais() > grupoCarona.getMembros().size() && !grupoCarona.getMotorista().getId().equals(idUsuario)) {
                gruposDisponiveis.add(grupoCarona);
            }
        }

        return new ComunicadoTodosGupos(gruposDisponiveis);
    }

    private void notificaUsuariosComCaronasAtualizadas() {
        try {
            synchronized (this.usuariosSemCarona) {
                for (Parceiro usuario : this.usuariosSemCarona) {
                    usuario.receba(obterComunicadoTodosGruposDisponiveis(usuario.getUsuario().getId()));
                }
            }
        } catch (Exception erro) {
            System.out.println("[ERRO - notificaMotoristaComComunicado]: " + erro.getMessage());
        }
    }

}
