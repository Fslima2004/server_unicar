package projeto.integrador.iv.Servidor.grupoDeCarona;

import java.io.Serializable;
// o GrupoDeCarona é uma classe que reunirá os usuarios que estao em uma carona
// sendo cada usuario um client conectado ao servidor e ouvindo as mudanças
// de estado da carona, assim como a lista de membros
import java.util.ArrayList;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoDeCarona;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoCaronaCancelada;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoSaida;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;

public class GrupoDeCarona implements Serializable {
    private ArrayList<Parceiro> membros;
    private String idCarona;
    private String idCriador;

    public GrupoDeCarona(String idCarona, Parceiro criador) {
        this.idCarona = idCarona;
        this.membros = new ArrayList<Parceiro>();
        this.idCriador = criador.getIdUsuario();
        this.membros.add(criador);
    }

    public void addMembro(Parceiro membro) {
        this.membros.add(membro);
        // notifica os membros que um novo membro entrou
        // obter ids dos membros deste grupo de carona em uma lista
        this.notificaMembrosComComunicado(this.getComunicadoGrupoDeCarona());
    }

    private Comunicado getComunicadoGrupoDeCarona() {
        // obter ids dos membros deste grupo de carona em uma lista
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        for (Parceiro membroDoGrupo : membros) {
            usuarios.add(new Usuario(membroDoGrupo.getIdUsuario()));
        }
        Comunicado comunicado = new ComunicadoGrupoDeCarona(idCarona, usuarios);
        return comunicado;
    }

    private boolean isCriador(Parceiro membro) {
        return membro.getIdUsuario().equals(this.idCriador);
    }

    public boolean isEmpty() {
        return this.membros.isEmpty();
    }

    public void removeMembro(Parceiro membro) {
        this.membros.remove(membro);
        try {
            membro.receba(new ComunicadoSaida());
        } catch (Exception erro) {
            // sei que passei os parametros corretos
        }

        if (isCriador(membro)) { // é o criador
            // se o membro que saiu for o criador, o grupo de carona é dissolvido
            // comunicar todos os membros
            this.notificaMembrosComComunicado(new ComunicadoCaronaCancelada());
            this.membros.clear();
            return;
        }

        this.notificaMembrosComComunicado(this.getComunicadoGrupoDeCarona()); // notifica os membros que um membro saiu
    }

    public void notificaMembrosComComunicado(Comunicado comunicado) {
        for (Parceiro membro : membros) {
            try {
                membro.receba(comunicado);
            } catch (Exception erro) {
                // sei que passei os parametros corretos
            }
        }
    }

    public String toString() {
        String ret = "";
        ret += "\nId da carona: " + this.idCarona + "\n";
        for (Parceiro membro : membros) {
            ret += "    " + membro.getIdUsuario() + "\n";
        }
        return ret;
    }

}
