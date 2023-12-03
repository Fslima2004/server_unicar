package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;

public class PedidoCriarGrupoDeCarona implements PedidoGrupoDeCarona {
    private GrupoCarona grupoDeCarona;

    public PedidoCriarGrupoDeCarona(GrupoCarona grupoDeCarona) {
        this.grupoDeCarona = grupoDeCarona;
    }

    public GrupoCarona getGrupoDeCarona() {
        return grupoDeCarona;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("grupoCarona", grupoDeCarona.toJson());
        JSONObject json = new JSONObject();
        json.put("type", "PedidoCriarGrupoDeCarona");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoCriarGrupoDeCarona(
                GrupoCarona.fromJson(json.getJSONObject("data").getJSONObject("grupoCarona")));
    }

}
