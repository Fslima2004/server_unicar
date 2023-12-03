package projeto.integrador.iv.Servidor.pedidos;

import org.json.JSONObject;

import projeto.integrador.iv.Servidor.comunicados.Comunicado;
import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoDeCarona;

public class PedidoCriarGrupoDeCarona implements PedidoGrupoDeCarona {
    private GrupoDeCarona grupoDeCarona;

    public PedidoCriarGrupoDeCarona(GrupoDeCarona grupoDeCarona) {
        this.grupoDeCarona = grupoDeCarona;
    }

    public String getIdDoSolicitante() {
        return grupoDeCarona.motorista.getId();
    }

    public String getIdDoGrupoDeCarona() {
        return idDoGrupoDeCarona;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("idGrupo", idDoGrupoDeCarona);
        data.put("idUsuario", idDoSolicitante);
        JSONObject json = new JSONObject();
        json.put("type", "PedidoCriarGrupoDeCarona");
        json.put("data", data); // Objeto vazio para "data"
        return json;
    }

    public static Comunicado fromJson(JSONObject json) {
        return new PedidoCriarGrupoDeCarona(json.getString("idUsuario") , json.getString("idGrupo") );
    }

}
