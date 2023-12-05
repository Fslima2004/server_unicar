package projeto.integrador.iv.Servidor.grupoDeCarona;

import java.io.Serializable;
// o GrupoDeCarona é uma classe que reunirá os usuarios que estao em uma carona
// sendo cada usuario um client conectado ao servidor e ouvindo as mudanças
// de estado da carona, assim como a lista de membros
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import projeto.integrador.iv.Servidor.carro.Carro;
import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoDeCarona;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoCaronaCancelada;
import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoSaida;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;

public class GrupoCarona implements Serializable {
    private ArrayList<Parceiro> membros;
    private Usuario motorista;
    private String idCarona;
    private String localPartida;
    private String horarioSaida;
    private String destino;
    private double preco;
    private int vagasTotais;
    private Parceiro motoristaConexao; // preciso atualizar esta conexao sempre que um motorista entrar com outra
                                       // conexao

    private Runnable callbackAtualizacaoCarona;

    public GrupoCarona(String idCarona, Usuario motorista, String localPartida,
            String horarioSaida, double preco, int vagasTotais, String destino) {
        this.idCarona = idCarona;
        this.membros = new ArrayList<Parceiro>();
        this.motorista = motorista;
        this.localPartida = localPartida;
        this.horarioSaida = horarioSaida;
        this.preco = preco;
        this.vagasTotais = vagasTotais;

        this.destino = destino;
    }

    public GrupoCarona(GrupoCarona grupoCarona) {
        this.idCarona = grupoCarona.idCarona;
        this.membros = grupoCarona.membros;
        this.motorista = grupoCarona.motorista;
        this.localPartida = grupoCarona.localPartida;
        this.horarioSaida = grupoCarona.horarioSaida;
        this.preco = grupoCarona.preco;
        this.vagasTotais = grupoCarona.vagasTotais;
        this.destino = grupoCarona.destino;
        this.motoristaConexao = grupoCarona.motoristaConexao;
    }

    public Usuario getMotorista() {
        return motorista;
    }

    // set motorista
    public void setMotorista(Usuario motorista) {
        this.motorista = motorista;
    }

    public Carro getCarro() {
        return this.motorista.getCarro();
    }

    public String getIdCarona() {
        return idCarona;
    }

    public void setMotoristaConexao(Parceiro motoristaConexao) {
        this.motoristaConexao = motoristaConexao;
    }

    public ArrayList<Parceiro> getMembros() {
        return membros;
    }

    public int getVagasTotais() {
        return vagasTotais;
    }

    public void setCallbackAtualizacaoCarona(Runnable callbackAtualizaoCarona) {
        this.callbackAtualizacaoCarona = callbackAtualizaoCarona;
    }

    public void addMembro(Parceiro membro) {
        this.membros.add(membro);
        // print usuario do membro
        System.out.println("GrupoCarona: addMembro: " + membro.getUsuario());
        // notifica os membros que um novo membro entrou
        // obter ids dos membros deste grupo de carona em uma lista
        this.notificaMotoristaComComunicado(this.getComunicadoGrupoDeCarona());

        callbackAtualizacaoCarona.run();
    }

    private Comunicado getComunicadoGrupoDeCarona() {
        // obter ids dos membros deste grupo de carona em uma lista
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        for (Parceiro membroDoGrupo : membros) {
            usuarios.add(membroDoGrupo.getUsuario());
        }
        Comunicado comunicado = new ComunicadoGrupoDeCarona(idCarona, usuarios);
        return comunicado;
    }

    public void atualizarConexaoMembro(String idUsuario, Parceiro conexaoNova) {
        for (Parceiro membro : membros) {
            if (membro.getUsuario().getId().equals(idUsuario)) {
                membro = conexaoNova;
                return;
            }
        }
    }

    private boolean isCriador(Parceiro membro) {
        return membro.getUsuario().getId().equals(this.motorista.getId());
    }

    public boolean isMotoristaAusente() {
        return this.motoristaConexao == null;
    }

    public boolean isEmpty() {
        return this.membros.isEmpty();
    }

    public void removeMembro(Parceiro membro) {
        if (isCriador(membro)) { // é o criador
            // se o membro que saiu for o criador, o grupo de carona é dissolvido
            // comunicar todos os membros
            this.notificaMembrosComComunicado(new ComunicadoCaronaCancelada());
            this.membros.clear();
            try {
                this.motoristaConexao.receba(new ComunicadoSaida());
                this.motoristaConexao = null;
            } catch (Exception erro) {
                // sei que passei os parametros corretos
            }
            return;
        } else {
            this.membros.remove(membro);
            try {
                membro.receba(new ComunicadoSaida());
            } catch (Exception erro) {
                // sei que passei os parametros corretos
            }
        }

        this.notificaMotoristaComComunicado(this.getComunicadoGrupoDeCarona()); // notifica o motorista que um membro
                                                                                // saiu
    }

    private void notificaMembrosComComunicado(Comunicado comunicado) {
        for (Parceiro membro : membros) {
            try {
                membro.receba(comunicado);
            } catch (Exception erro) {
            }
        }
    }

    private void notificaMotoristaComComunicado(Comunicado comunicado) {

        System.out.println("GrupoCarona: notificaMotoristaComComunicado");

        try {
            if (this.motoristaConexao == null) {
                System.out.println("GrupoCarona: notificaMotoristaComComunicado: motoristaConexao null");
            } else {
                System.out.println("GrupoCarona: notificaMotoristaComComunicado: " + motoristaConexao.getUsuario());
            }
            // notifica motorista print
            if (comunicado == null) {
                System.out.println("GrupoCarona: notificaMotoristaComComunicado: comunicado null");
            } else {
                System.out.println("GrupoCarona: notificaMotoristaComComunicado: " + comunicado.toJson().toString());
            }

            this.motoristaConexao.receba(comunicado);
        } catch (Exception erro) {
            System.out.println(erro.getMessage());
            // print stack trace
            erro.printStackTrace();
            System.out.println("Erro ao notificar motorista");
        }
    }

    public String toString() {
        String ret = "";
        ret += "\nId da carona: " + this.idCarona + "\n";
        for (Parceiro membro : membros) {
            ret += "    " + membro.getUsuario().getId() + "\n";
        }
        return ret;
    }

    public static GrupoCarona fromJson(JSONObject json) {
        return new GrupoCarona(json.getString("idCarona"), Usuario.fromJson(json.getJSONObject("motorista")),
                json.getString("localPartida"), json.getString("horarioSaida"), json.getDouble("preco"),
                json.getInt("vagasTotais"), json.getString("localDestino"));
    }

    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        JSONArray usuariosArray = new JSONArray();
        for (Parceiro usuario : membros) {
            usuariosArray.put(usuario.getUsuario().toJson());
        }

        json.put("usuarios", usuariosArray);
        json.put("idCarona", this.idCarona);
        json.put("motorista", this.motorista.toJson());
        json.put("localPartida", this.localPartida);
        json.put("horarioSaida", this.horarioSaida);
        json.put("preco", this.preco);
        json.put("vagasTotais", this.vagasTotais);
        json.put("localDestino", this.destino);
        return json;
    }

}
