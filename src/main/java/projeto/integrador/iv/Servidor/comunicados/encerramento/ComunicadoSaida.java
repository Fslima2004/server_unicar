package projeto.integrador.iv.Servidor.comunicados.encerramento;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class ComunicadoSaida implements ComunicadoEncerramento{
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        return json;
    }

    @Override
    public Comunicado fromJson(JSONObject json) {
        return new ComunicadoSaida();
    }
}
