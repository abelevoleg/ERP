package org.shkafulkin;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static org.junit.Assert.assertTrue;

import org.example.MainApp;
import org.junit.Test;


public class MainAppTest {
    private volatile boolean success = false;
    // тестирование запуска приложения
    @Test
    public void testMain() {
        Thread thread = new Thread(() -> {
            try {
                Application.launch(MainApp.class);
                success = true;
            } catch(Throwable t) {
                if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                    // исключение появляется при закрытии приложения
                    success = true;
                    return;
                }
                // обработка нештатного исключения
                Logger.getLogger(MainAppTest.class.getName()).log(Level.SEVERE, null, t);
            }
        });
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // ожидание перед закрытием приложения
        } catch(InterruptedException ex) {
        }
        thread.interrupt();
        try {
            thread.join(1); // ожидание завершения потока тестирования приложения
        } catch(InterruptedException ex) {
        }
        assertTrue(success);
    }
}