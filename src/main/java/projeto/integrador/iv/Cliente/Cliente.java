package projeto.integrador.iv.Cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import projeto.integrador.iv.Servidor.parceiro.Parceiro;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoObterTodasCaronas;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;

public class Cliente {
	public static final String HOST_PADRAO = "localhost";
	public static final int PORTA_PADRAO = 3000;

	public static void main(String[] args) {
		if (args.length > 2) {
			System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
			return;
		}

		Socket conexao = null;
		try {
			String host = Cliente.HOST_PADRAO;
			int porta = Cliente.PORTA_PADRAO;

			if (args.length > 0)
				host = args[0];

			if (args.length == 2)
				porta = Integer.parseInt(args[1]);

			conexao = new Socket(host, porta);
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		ObjectOutputStream transmissor = null;
		try {
			transmissor = new ObjectOutputStream(
					conexao.getOutputStream());
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		ObjectInputStream receptor = null;
		try {
			receptor = new ObjectInputStream(
					conexao.getInputStream());
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");

			return;
		}

		Parceiro servidor = null;
		try {
			servidor = new Parceiro(conexao, receptor, transmissor);
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		// TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento =
		// null;
		// try {
		// tratadoraDeComunicadoDeDesligamento = new
		// TratadoraDeComunicadoDeDesligamento(servidor);
		// } catch (Exception erro) {
		// } // sei que servidor foi instanciado

		// // a thread tratadoraDeComunicadoDeDesligamento vai tratar
		// // os comunicados de desligamento que chegam do servidor
		// tratadoraDeComunicadoDeDesligamento.start();

		TratadorDeComunicados tratadorDeComunicados = null;
		try {
			tratadorDeComunicados = new TratadorDeComunicados(servidor);
		} catch (Exception erro) {
		} // sei que servidor foi instanciado

		tratadorDeComunicados.start();

		char opcao = ' ';

		do {

			System.out.print("Sua opcao ([E]ntrar, [S]air, [C]riar, [T]erminar, [R]ecuperar caronas disponiveis)? ");

			try {
				opcao = Character.toUpperCase(Teclado.getUmChar());
			} catch (Exception erro) {
				System.err.println("Opcao invalida!\n");

			}

			if ("ESCT".indexOf(opcao) == -1) {
				System.err.println("Opcao invalida!\n");

			}

			try {

				switch (opcao) {
					case 'E':
						System.out.print("Seu id de usuario? ");
						String id = Teclado.getUmString();
						System.out.println();

						System.out.print("Seu id de grupo? ");
						String idGrupo = Teclado.getUmString();
						System.out.println();

						servidor.receba(new PedidoEntrarNoGrupoDeCarona(id, idGrupo));
						break;
					case 'S':
						servidor.receba(new PedidoSairDoGrupoDeCarona());
						break;
					case 'C':
						System.out.print("Seu id de usuario? ");
						id = Teclado.getUmString();
						System.out.println();

						System.out.print("Seu id de grupo? ");
						idGrupo = Teclado.getUmString();
						System.out.println();

						servidor.receba(new PedidoCriarGrupoDeCarona(id, idGrupo));
						break;
					case 'R':
						servidor.receba(new PedidoObterTodasCaronas());
						break;
				}
			} catch (Exception erro) {
				System.err.println("Erro de comunicacao com o servidor;");
				System.err.println("Tente novamente!");
				System.err.println("Caso o erro persista, termine o programa");
				System.err.println("e volte a tentar mais tarde!\n");
			}

		} while (opcao != 'T');

		try {
			servidor.receba(new PedidoSairDoGrupoDeCarona());
		} catch (Exception erro) {
		}

		System.out.println("Obrigado por usar este programa!");
		System.exit(0);
	}
}
