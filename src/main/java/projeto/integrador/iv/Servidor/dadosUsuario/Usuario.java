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
        
        JSONObject json = new JSONObject();
        json.put("id", id);
        return json;
    }

    
    public static Usuario fromJson(JSONObject json) {
        return new Usuario(json.getString("id"));
    }
}
