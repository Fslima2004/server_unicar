package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

public class ComunicadoGrupoInexistente implements Comunicado {
    
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        return json;
    }

    @Override
    public Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoInexistente();
    }
}
