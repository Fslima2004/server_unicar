package projeto.integrador.iv.Servidor.servidor;

import java.util.HashMap;
import java.util.Map;

import projeto.integrador.iv.Servidor.aceitadoraDeConexao.AceitadoraDeConexao;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoDeCarona;
import projeto.integrador.iv.Servidor.teclado.Teclado;

public class Servidor {
    public static String PORTA_PADRAO = "3000";

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Uso esperado: java Servidor [PORTA]\n");
            return;
        }

        String porta = Servidor.PORTA_PADRAO;

        if (args.length == 1)
            porta = args[0];

        Map<String, GrupoDeCarona> gruposDeCarona = new HashMap<String, GrupoDeCarona>();

        // aceita se conectar com usuarios
        AceitadoraDeConexao aceitadoraDeConexao = null;
        try {
            aceitadoraDeConexao = new AceitadoraDeConexao(porta, gruposDeCarona);
            // AceitadoraDeConexao é uma thread que aceita
            // usuarios conforme pedem para se conectar
            
            System.out.println("Servidor ativo na porta " + porta + "!\n");
            aceitadoraDeConexao.start();
        } catch (Exception erro) {
            System.err.println("Escolha uma porta apropriada e liberada para uso!\n");
            return;
        }

        // controle dos comandos do servidor (nao importa agora)
        for (;;) {
            System.out.println("O servidor esta ativo! Para desativa-lo,");
            System.out.println("use o comando \"desativar\"\n");
            System.out.print("> ");

            String comando = null;
            try {
                comando = Teclado.getUmString();
            } catch (Exception erro) {
            }
            
            //TODO: remover desativar
            // if (comando.toLowerCase().equals("desativar")) {

            //     // synchronized é usado para garantir que apenas uma thread
            //     // possa acessar o objeto usuarios por vez
            //     synchronized (gruposDeCarona) {
            //         ComunicadoDeDesligamento comunicadoDeDesligamento = new ComunicadoDeDesligamento();

            //         for (Parceiro usuario : gruposDeCarona) {
            //             try {
            //                 usuario.receba(comunicadoDeDesligamento);
            //                 usuario.adeus();
            //             } catch (Exception erro) {
            //             }
            //         }
            //     }

            //     System.out.println("O servidor foi desativado!\n");
            //     System.exit(0);
            // } else
             
                System.err.println("Comando invalido!\n");
        }
    }
}
