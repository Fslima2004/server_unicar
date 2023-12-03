package projeto.integrador.iv.Servidor.dadosUsuario;

import java.io.Serializable;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;

public class Usuario implements Serializable{
    private String id;
    private String nome;
    private String contato;

    public Usuario(String id, String nome, String contato) {
        this.id = id;
        this.nome = nome;
        this.contato = contato;
    }

    public Usuario() {
        this.id = "";
        this.nome = "";
        this.contato = "";
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

    public void setId(String novoId) {
        this.id = novoId;
    }

    public void setNome(String novoNome) {
        this.id = novoNome;
    }

    public void setContato(String novoContato) {
        this.id = novoContato;
    }

    @Override
    public String toString() {
        return "DadosUsuario{" + "id=" + id + ", nome=" + nome + ", contato=" + contato + '}';
    }

    
    public JSONObject toJson() {
        
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nome", nome);
        json.put("contato", contato);
        return json;
    }

    
    public static Usuario fromJson(JSONObject json) {
        return new Usuario(json.getString("id"), json.getString("nome"), json.getString("cadastro"));
    }
}
