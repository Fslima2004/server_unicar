package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoTodosGruposDisponiveis implements Comunicado {

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "PedidoTodosGruposDisponiveis");
        json.put("data", new JSONObject());
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoTodosGruposDisponiveis();
    }

}
