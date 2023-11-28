package projeto.integrador.iv.Servidor.dadosUsuario;

import java.io.Serializable;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.comunicados.ComunicadoGrupoJaExiste;

public class Usuario implements Serializable{
    private String id;

    public Usuario(String id) {
        this.id = id;
    }

    public Usuario() {
        this.id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String novoId) {
        this.id = novoId;
    }

    @Override
    public String toString() {
        return "DadosUsuario{" + "id=" + id + '}';
    }

    
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("id", id);
        JSONObject json = new JSONObject();
        json.put("type", "Usuario");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    
    public static Usuario fromJson(JSONObject json) {
        return new Usuario(json.getJSONObject("data").getString("id"));
    }
}
