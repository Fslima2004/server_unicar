package projeto.integrador.iv.Servidor.dadosUsuario;

import java.io.Serializable;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.carro.Carro;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String contato;
    private String idCaronaAtual;
    private Carro carro;
    private String parada;

    public Usuario(String id, String nome, String contato, Carro carro, String parada) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.idCaronaAtual = "";
        this.carro = carro;
        this.parada = parada;
    }

    public Usuario(String id, String nome, String contato, String idCaronaAtual, Carro carro, String parada) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
        this.idCaronaAtual = idCaronaAtual;
        this.carro = carro;
        this.parada = parada;
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

    public Carro getCarro() {
        return carro;
    }

    public String getParada() {
        return parada;
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

    public void setParada(String novaParada) {
        this.parada = novaParada;
    }

    public void setIdCaronaAtual(String novoIdCaronaAtual) {

        this.idCaronaAtual = novoIdCaronaAtual;

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
                + "parada=" + parada + '}';
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        // print valores individuais
        System.out.println("Usuario: toJson: " + id + " " + nome + " " + contato + " " + idCaronaAtual + " " + carro
                + " " + parada);

        json.put("id", id);
        json.put("nome", nome);
        json.put("contato", contato);
        json.put("idCaronaAtual", idCaronaAtual == null ? "não informada" : idCaronaAtual);
        json.put("carro", carro == null ? (new Carro("", "", "")).toJson() : carro.toJson());

        json.put("parada", parada == null ? "não informada" : parada);

        return json;
    }

    public static Usuario fromJson(JSONObject json) {
        String id = json.getString("id");
        String nome = json.getString("nome");
        String contato = json.getString("contato");
        String idCaronaAtual = json.optString("idCaronaAtual", "");
        Carro carro = Carro.fromJson(json.getJSONObject("carro"));
        String parada = json.optString("parada", "");

        return new Usuario(id, nome, contato, idCaronaAtual, carro, parada);
    }
}
