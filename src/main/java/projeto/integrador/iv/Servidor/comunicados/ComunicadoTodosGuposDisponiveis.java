package projeto.integrador.iv.Servidor.comunicados;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;


public class ComunicadoTodosGuposDisponiveis implements Comunicado {
    private ArrayList<GrupoCarona> grupos;

    public ComunicadoTodosGuposDisponiveis(ArrayList<GrupoCarona> grupos) {
        this.grupos = grupos;
    }

    public ArrayList<GrupoCarona> getGrupos() {
        return grupos;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        JSONArray gruposArray = new JSONArray();
        for (GrupoCarona grupo : grupos) {
            gruposArray.put(grupo.toJson());
        }

        data.put("grupos", gruposArray);

        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoTodosGuposDisponiveis");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {

        JSONArray gruposArray = json.getJSONArray("grupos");
        ArrayList<GrupoCarona> grupos = new ArrayList<GrupoCarona>();
        for (int i = 0; i < gruposArray.length(); i++) {
            JSONObject grupoJson = gruposArray.getJSONObject(i);
            GrupoCarona grupo = GrupoCarona.fromJson(grupoJson);
            grupos.add(grupo);
        }

        return new ComunicadoTodosGuposDisponiveis(grupos);
    }

}
