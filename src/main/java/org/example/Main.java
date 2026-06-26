package org.example;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main.java
 *
 * Responsabilidade: Ser o ponto de entrada da aplicação gráfica (Entry Point).
 * Ele configura o tema (Look and Feel) para combinar com o sistema operacional atual,
 * e inicia a janela MainFrame utilizando a Event Dispatch Thread (boa prática do Swing).
 */
public class Main {
    public static void main(String[] args) {
        
        // Tenta aplicar a aparência nativa do Windows, Mac ou Linux, em vez do tema padrão antigo do Java
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se houver qualquer falha, o Java usará o Metal (padrão) e a execução continua
            e.printStackTrace();
        }

        // Garante que a GUI seja instanciada e executada na thread de eventos (EDT), evitando travamentos
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
