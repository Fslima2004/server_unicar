package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;

public class ComunicadoMeuGrupoCarona implements Comunicado {
    private GrupoCarona grupoCarona;

    public ComunicadoMeuGrupoCarona(GrupoCarona grupoCarona) {
        this.grupoCarona = grupoCarona;
    }

    public GrupoCarona getGrupoCarona() {
        return grupoCarona;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("grupoCarona", grupoCarona.toJson());
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoMeuGrupoCarona");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoMeuGrupoCarona(
                GrupoCarona.fromJson(json.getJSONObject("grupoCarona")));
    }

}
