package projeto.integrador.iv.Servidor.comunicados;

import java.util.Map;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoDeCarona;

public class ComunicadoTodasCaronas implements Comunicado {

    private Map<String, GrupoDeCarona> gruposDeCarona;

    public ComunicadoTodasCaronas(Map<String, GrupoDeCarona> gruposDeCarona) {
        this.gruposDeCarona = gruposDeCarona;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        // Iterar sobre os grupos e converter cada GrupoDeCarona para JSON
        for (Map.Entry<String, GrupoDeCarona> entry : gruposDeCarona.entrySet()) {
            data.put(entry.getKey(), entry.getValue().toJson());
        }

        JSONObject json = new JSONObject();
        json.put("type", "ComunicadoTodasCaronas");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        // Extrair os dados do JSON
        JSONObject grupos = json.getJSONObject("data");
        
        // Iterar sobre os grupos e converter cada JSON para GrupoDeCarona
        // Aqui você precisará de uma lógica específica para criar instâncias de GrupoDeCarona a partir do JSON

        return new ComunicadoTodasCaronas(/* Map de GrupoDeCarona resultante da conversão */);
    }
}
