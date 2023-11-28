package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

public class ComunicadoGrupoJaExiste implements Comunicado{
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoGrupoJaExiste");
        json.put("data", new JSONObject()); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoJaExiste();
    }
}
