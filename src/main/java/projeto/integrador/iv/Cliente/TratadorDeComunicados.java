package projeto.integrador.iv.Cliente;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoDeCarona;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoInexistente;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoCaronaCancelada;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoEncerramento;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoSaida;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;

public class TratadorDeComunicados extends Thread {
    private Parceiro servidor;

    public TratadorDeComunicados(Parceiro servidor) throws Exception {
        if (servidor == null)
            throw new Exception("Porta invalida");

        this.servidor = servidor;
    }

    public void run() {
        try {
            Comunicado comunicado = null;
            do {
                comunicado = (Comunicado) servidor.espie();
                if (comunicado != null) {
                    comunicado = (Comunicado) servidor.envie();
                    // System.out.println("Comunicado recebido: " + comunicado.toString());
                    if (comunicado instanceof ComunicadoGrupoDeCarona) {
                        ComunicadoGrupoDeCarona comunicadoGrupoDeCarona = (ComunicadoGrupoDeCarona) comunicado;
                        System.out.println("\nAtualização de grupo de carona recebida:");
                        System.out.println("Id da carona: " + comunicadoGrupoDeCarona.getIdGrupo());
                        // printar membros um por um da lista de membros
                        for (Usuario usuario : comunicadoGrupoDeCarona.getUsuarios()) {
                            System.out.println("    " + usuario.toString());
                        }
                    }else if (comunicado instanceof ComunicadoGrupoJaExiste) {
                        System.out.println("Grupo de carona já existe");
                    } else if (comunicado instanceof ComunicadoGrupoInexistente) {
                        System.out.println("Grupo de carona inexistente");
                    }
                    else if (comunicado instanceof ComunicadoEncerramento) {
                        if (comunicado instanceof ComunicadoSaida)
                            System.out.println("Saida efetuada com sucesso");
                        else if (comunicado instanceof ComunicadoCaronaCancelada)
                            System.out.println("Carona cancelada pelo motorista");

                        System.exit(0);
                    }
                }
            } while (true);
        } catch (Exception erro) {
            // System.err.println(erro.getMessage());
            System.err.println("Erro de comunicação com o servidor");
        }
    }

}