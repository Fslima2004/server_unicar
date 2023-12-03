
package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;

public class PedidoEntrarNoGrupoDeCarona implements PedidoGrupoDeCarona {
    private String idGrupoCarona;
    private String parada;
    private Usuario usuario;

    public PedidoEntrarNoGrupoDeCarona(String idGrupoCarona, String parada,
            Usuario usuario) {
        this.idGrupoCarona = idGrupoCarona;
        this.parada = parada;
        this.usuario = usuario;
    }

    public String getIdGrupoCarona() {
        return idGrupoCarona;
    }

    public String getParada() {
        return parada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("idGrupo", idGrupoCarona);
        data.put("usuario", usuario.toJson());
        data.put("parada", parada);
        JSONObject json = new JSONObject();
        json.put("type", "PedidoEntrarNoGrupoDeCarona");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoEntrarNoGrupoDeCarona(json.getString("idGrupo"),
                json.getString("parada"), Usuario.fromJson(json.getJSONObject("usuario")));
    }
}
