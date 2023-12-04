package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

public class PedidoMeuGrupoCarona implements PedidoGrupoDeCarona {
    private String idUsuario;
    private String categoria;

    public static final String MOTORISTA = "motorista";
    public static final String PASSAGEIRO = "passageiro";

    public PedidoMeuGrupoCarona(String idUsuario, String categoria) {
        this.idUsuario = idUsuario;
        this.categoria = categoria;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("idUsuario", idUsuario);
        data.put("categoria", categoria);
        
        JSONObject json = new JSONObject();
        json.put("type", "PedidoMeuGrupoCarona");
        json.put("data", data);
        return json;
    }

    public static PedidoMeuGrupoCarona fromJson(JSONObject json) {
        return new PedidoMeuGrupoCarona(json.getString("idUsuario"), json.getString("categoria"));
    }

}
