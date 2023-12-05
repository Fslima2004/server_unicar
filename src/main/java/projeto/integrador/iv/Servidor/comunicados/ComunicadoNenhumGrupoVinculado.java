package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

public class ComunicadoNenhumGrupoVinculado implements Comunicado {

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoNenhumGrupoVinculado");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoNenhumGrupoVinculado();
    }

}
