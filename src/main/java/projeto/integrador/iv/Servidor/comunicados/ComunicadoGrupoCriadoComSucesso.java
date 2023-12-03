package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

public class ComunicadoGrupoCriadoComSucesso implements Comunicado{
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoGrupoCriadoComSucesso");
        json.put("data", new JSONObject()); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoCriadoComSucesso();
    }
}
