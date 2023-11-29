package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoObterTodasCaronas implements PedidoGrupoDeCarona {
    
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "PedidoObterTodasCaronas");
        json.put("data", new JSONObject()); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoObterTodasCaronas ();
    }
}
