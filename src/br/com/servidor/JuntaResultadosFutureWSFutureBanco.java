package br.com.servidor;


import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JuntaResultadosFutureWSFutureBanco implements Runnable {

    private Future<String> futureWS;
    private Future<String> futureBanco;
    private PrintStream saidaCliente;

    public JuntaResultadosFutureWSFutureBanco(Future<String> futureWS, Future<String> futureBanco, PrintStream saidaCliente) {
        this.futureWS = futureWS;
        this.futureBanco = futureBanco;
        this.saidaCliente = saidaCliente;
    }

    @Override
    public void run() {

        System.out.println("Aguardando resultados do future WS e Banco");

        try {
            String numeroMagicoWS = this.futureWS.get(20, TimeUnit.SECONDS);
            String numeroMagicoBanco = this.futureBanco.get(20, TimeUnit.SECONDS);

            this.saidaCliente.println("Resultado comando c2: " + numeroMagicoWS + ", " + numeroMagicoBanco);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Timeout: Cancelado execucao comando do comando c2");
            this.saidaCliente.println("Timeout na execucao comando c2");
            this.futureWS.cancel(true);
            this.futureBanco.cancel(true);
        }

        System.out.println("Finalizou JuntaResultadosFutureWSFutureBanco");
    }
}
