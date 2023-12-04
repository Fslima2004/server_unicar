package projeto.integrador.iv.Servidor.dadosUsuario;

import java.io.Serializable;

import org.json.JSONObject;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String contato;
    private String idCaronaAtual;

    public Usuario(String id, String nome, String contato) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.idCaronaAtual = "";
    }

    public Usuario(String id, String nome, String contato, String idCaronaAtual) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.idCaronaAtual = idCaronaAtual;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getContato() {
        return contato;
    }

    public String getIdCaronaAtual() {
        return idCaronaAtual;
    }

    public void setId(String novoId) {
        this.id = novoId;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

    public void setContato(String novoContato) {
        this.contato = novoContato;
    }

    public void setIdCaronaAtual(String novoIdCaronaAtual) {
        System.out.println("Usuario: setIdCaronaAtual: " + idCaronaAtual + " -> " + novoIdCaronaAtual);

        this.idCaronaAtual = novoIdCaronaAtual;
        System.out.println("Usuario: idCarona agora é: " + novoIdCaronaAtual);
    }

    public Usuario(Usuario usuario) {
        this.id = usuario.id;
        this.nome = usuario.nome;
        this.contato = usuario.contato;
        this.idCaronaAtual = usuario.idCaronaAtual;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome=" + nome + ", contato=" + contato + ", idCaronaAtual=" + idCaronaAtual
                + '}';
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nome", nome);
        json.put("contato", contato);
        json.put("idCaronaAtual", idCaronaAtual);
        return json;
    }

    public static Usuario fromJson(JSONObject json) {
        String id = json.getString("id");
        String nome = json.getString("nome");
        String contato = json.getString("contato");
        String idCaronaAtual = json.optString("idCaronaAtual", "");

        return new Usuario(id, nome, contato, idCaronaAtual);
    }
}
