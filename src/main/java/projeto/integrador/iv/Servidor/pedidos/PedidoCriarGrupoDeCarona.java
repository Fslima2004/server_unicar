package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;

public class PedidoCriarGrupoDeCarona implements PedidoGrupoDeCarona {
    private String idDoSolicitante;
    private String idDoGrupoDeCarona;

    public PedidoCriarGrupoDeCarona(String idDoSolicitante, String idDoGrupoDeCarona) {
        this.idDoSolicitante = idDoSolicitante;
        this.idDoGrupoDeCarona = idDoGrupoDeCarona;
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

        data.put("idDoGrupoDeCarona", idDoGrupoDeCarona);
        data.put("idDoSolicitante", idDoSolicitante);
        JSONObject json = new JSONObject();
        json.put("type", "PedidoCriarGrupoDeCarona");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    @Override
    public Comunicado fromJson(JSONObject json){
        return new PedidoCriarGrupoDeCarona(json.getString("idDoSolicitante") , json.getString("idDoGrupoSolicitante") );
    }

}
