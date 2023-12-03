package projeto.integrador.iv.Servidor.comunicados;

import java.util.ArrayList;

import org.json.JSONArray;
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
        JSONObject data = new JSONObject();
        data.put("idGrupo", idGrupo);

        JSONArray usuariosArray = new JSONArray();
        for (Usuario usuario : usuarios) {
            usuariosArray.put(usuario.toJson());
        }
        
        data.put("usuarios", usuariosArray);

        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoGrupoDeCarona");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        String idGrupo = json.getString("idGrupo");

        JSONArray usuariosArray = json.getJSONArray("usuarios");
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < usuariosArray.length(); i++) {
            JSONObject usuarioJson = usuariosArray.getJSONObject(i);
            Usuario usuario = Usuario.fromJson(usuarioJson);
            usuarios.add(usuario);
        }

        return new ComunicadoGrupoDeCarona(idGrupo, usuarios);
    }
}
