package projeto.integrador.iv.Servidor.comunicados;

import java.util.ArrayList;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;

public class ComunicadoGrupoDeCarona implements Comunicado {
    private String idGrupo;
    private ArrayList<Usuario> usuarios;

    public ComunicadoGrupoDeCarona(String idGrupo, ArrayList<Usuario> usuarios) {
        this.idGrupo = idGrupo;
        this.usuarios = usuarios;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        return json;
    }

    @Override
    public Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoDeCarona(idGrupo, usuarios);
    }

    

}
