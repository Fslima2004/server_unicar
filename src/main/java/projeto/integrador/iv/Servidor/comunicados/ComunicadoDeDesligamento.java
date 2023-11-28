package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoEncerramento;

public class ComunicadoDeDesligamento implements ComunicadoEncerramento
{
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoDeDesligamento");
        json.put("data", new JSONObject()); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new ComunicadoGrupoInexistente();
    }
}
