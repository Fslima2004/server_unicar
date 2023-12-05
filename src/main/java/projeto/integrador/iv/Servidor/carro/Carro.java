package projeto.integrador.iv.Servidor.carro;

import org.json.JSONObject;

public class Carro{
    private String modelo;
    private String cor;
    private String placa;

    public Carro(String modelo, String cor, String placa) {
        this.modelo = modelo;
        this.cor = cor;
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCor() {
        return cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setModelo(String novoModelo) {
        this.modelo = novoModelo;
    }

    public void setCor(String novaCor) {
        this.cor = novaCor;
    }

    public void setPlaca(String novaPlaca) {
        this.placa = novaPlaca;
    }

    public Carro(Carro carro) {
        this.modelo = carro.modelo;
        this.cor = carro.cor;
        this.placa = carro.placa;
    }

    @Override
    public String toString() {
        return "Carro{" + "modelo=" + modelo + ", cor=" + cor + ", placa=" + placa + '}';
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("modelo", modelo);
        json.put("cor", cor);
        json.put("placa", placa);
        return json;
    }

    public static Carro fromJson(JSONObject json) {
        String modelo = json.getString("modelo");
        String cor = json.getString("cor");
        String placa = json.getString("placa");

        return new Carro(modelo, cor, placa);
    }
}