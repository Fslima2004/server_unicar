
package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;

public class PedidoEntrarNoGrupoDeCarona implements PedidoGrupoDeCarona {
    private String idGrupoCarona;

    private Usuario usuario;

    public PedidoEntrarNoGrupoDeCarona(String idGrupoCarona,
            Usuario usuario) {
        this.idGrupoCarona = idGrupoCarona;

        this.usuario = usuario;

    }

    public String getIdGrupoCarona() {
        return idGrupoCarona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("idGrupo", idGrupoCarona);
        data.put("usuario", usuario.toJson());
        JSONObject json = new JSONObject();
        json.put("type", "PedidoEntrarNoGrupoDeCarona");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoEntrarNoGrupoDeCarona(json.getString("idGrupo"),
                Usuario.fromJson(json.getJSONObject("usuario")));
    }
}
