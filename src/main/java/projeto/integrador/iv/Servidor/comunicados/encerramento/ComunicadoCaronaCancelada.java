package projeto.integrador.iv.Servidor.comunicados.encerramento;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class ComunicadoCaronaCancelada implements ComunicadoEncerramento{
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoCaronaCancelada");
        json.put("data", new JSONObject()); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoCaronaCancelada();
    }
}
