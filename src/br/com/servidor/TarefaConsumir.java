package br.com.servidor;

import java.util.concurrent.BlockingQueue;

public class TarefaConsumir implements Runnable {

    private BlockingQueue<String> filaComandos;

    public TarefaConsumir(BlockingQueue<String> filaComandos) {
        this.filaComandos = filaComandos;
    }

    @Override
    public void run() {
        try {
            String comando;
            while((comando = this.filaComandos.take()) != null) {
                System.out.println("Consumindo comando " + comando + ", " + Thread.currentThread().getName());
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
