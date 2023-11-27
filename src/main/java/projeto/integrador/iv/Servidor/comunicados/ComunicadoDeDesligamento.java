package projeto.integrador.iv.Servidor.comunicados;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.encerramento.ComunicadoEncerramento;

public class ComunicadoDeDesligamento implements ComunicadoEncerramento
{
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
