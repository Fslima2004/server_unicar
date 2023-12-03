
package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoEntrarNoGrupoDeCarona implements PedidoGrupoDeCarona {
    private String idDoSolicitante;
    private String idDoGrupoDeCarona;
    private String nome;
    private String contato;
    private String parada;

    public PedidoEntrarNoGrupoDeCarona(String idDoSolicitante, String idDoGrupoDeCarona, String nome, String contato, String parada) {
        this.idDoSolicitante = idDoSolicitante;
        this.idDoGrupoDeCarona = idDoGrupoDeCarona;
        this.nome = nome;
        this.contato = contato;
        this.parada = parada;
    }

    public String getIdDoSolicitante() {
        return idDoSolicitante;
    }

    public String getIdDoGrupoDeCarona() {
        return idDoGrupoDeCarona;
    }


    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("idGrupo", idDoGrupoDeCarona);
        data.put("idUsuario", idDoSolicitante);
        data.put("nome", nome);
        data.put("parada", parada);
        data.put("contato", contato);
        JSONObject json = new JSONObject();
        json.put("type", "PedidoEntrarNoGrupoDeCarona");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoEntrarNoGrupoDeCarona(json.getString("idUsuario"), json.getString("idGrupo"), json.getString("nome"), json.getString("parada"), json.getString("contato"));
    }
}
