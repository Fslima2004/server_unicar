package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;

public class ComunicadoGrupoCriadoComSucesso implements Comunicado {
    private GrupoCarona grupoCarona;

    public ComunicadoGrupoCriadoComSucesso(GrupoCarona grupoCarona) {
        this.grupoCarona = grupoCarona;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        data.put("grupoCarona", grupoCarona.toJson());
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoGrupoCriadoComSucesso");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoCriadoComSucesso(
                GrupoCarona.fromJson(json.getJSONObject("grupoCarona")));
    }
}
