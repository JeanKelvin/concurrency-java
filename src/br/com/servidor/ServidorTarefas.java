package br.com.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

    private ServerSocket servidor;
    private ExecutorService threadPool;
    private AtomicBoolean estaRodando; //private volatile boolean estaRodando;
    private BlockingQueue<String> filaComandos;

    public ServidorTarefas() throws IOException {
        System.out.println("--- Iniciando servidor ---");
        this.servidor = new ServerSocket(12345);
        this.estaRodando = new AtomicBoolean(true);
        this.threadPool = Executors.newCachedThreadPool(new FabricaDeThreads()); //Executors.newFixedThreadPool()
        this.filaComandos = new ArrayBlockingQueue<>(2);
        iniciarConsumidores();
    }

    private void iniciarConsumidores() {
        int qtdConsumidores = 2;
        for (int i = 0; i < qtdConsumidores; i++) {
            TarefaConsumir tarefa = new TarefaConsumir(filaComandos);
            this.threadPool.execute(tarefa);
        }
    }

    public static void main(String[] args) throws Exception {
        ServidorTarefas servidor = new ServidorTarefas();
        servidor.rodar();
        servidor.parar();
    }

    public void rodar() throws IOException {
        while (this.estaRodando.get()) {
            try {
                Socket socket = servidor.accept();
                System.out.println("Aceitando novo cliente na porta: " + socket.getPort());

                DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool, filaComandos, socket, this);
                threadPool.execute(distribuirTarefas);
            } catch (SocketException e) {
                System.out.println("SocketException, está rodando? " + this.estaRodando);
            }
        }
    }

    public void parar() throws IOException {
        this.estaRodando.set(false);
        this.servidor.close();
        this.threadPool.shutdown();
        //System.exit(0);
    }
}
