package org.example;

/**
 * SequentialSearch.java
 *
 * Responsabilidade: Isolar o algoritmo de busca sequencial puro.
 * Conforme requisito, o laço de repetição foi codificado manualmente sem bibliotecas
 * de busca pré-prontas. Esta classe pode ser executada instantaneamente para 
 * obtermos as métricas precisas de tempo de execução real na CPU antes da animação.
 */
public class SequentialSearch {

    /**
     * Executa a Busca Sequencial padrão, retornando o índice caso encontrado
     * ou -1 caso não encontrado.
     * 
     * @param lista O vetor de inteiros onde buscaremos.
     * @param chave O número que desejamos encontrar.
     * @return Índice do elemento na lista, ou -1 se inexistente.
     */
    public static int search(int[] lista, int chave) {
        // Implementação estrita e pura conforme solicitado nos requisitos
        for (int i = 0; i < lista.length; i++) {
            if (lista[i] == chave) {
                return i;
            }
        }
        return -1;
    }
}
