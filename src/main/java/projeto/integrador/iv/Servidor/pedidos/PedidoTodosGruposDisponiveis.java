package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoTodosGruposDisponiveis implements Comunicado {

    private String idUsuario;

    public PedidoTodosGruposDisponiveis(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        JSONObject data = new JSONObject();
        data.put("idUsuario", idUsuario);
        json.put("type", "PedidoTodosGruposDisponiveis");
        json.put("data", data);
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoTodosGruposDisponiveis(json.getString("idUsuario"));
    }

}
