package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoSairDoGrupoDeCarona implements PedidoGrupoDeCarona {
    private String idGrupo;
    private String idUsuario;

    public PedidoSairDoGrupoDeCarona(String idGrupo, String idUsuario) {
        this.idGrupo = idGrupo;
        this.idUsuario = idUsuario;

    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        JSONObject data = new JSONObject();
        data.put("idGrupo", idGrupo);
        data.put("idUsuario", idUsuario);
        json.put("type", "PedidoSairDoGrupoDeCarona");
        json.put("data", data); 
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoSairDoGrupoDeCarona(json.getString("idGrupo"), json.getString("idUsuario"));
    }
}
