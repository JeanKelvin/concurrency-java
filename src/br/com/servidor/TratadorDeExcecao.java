package br.com.servidor;

public class TratadorDeExcecao implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Deu excecao na thread " + t.getName() + ", " + e.getMessage());
    }
}
