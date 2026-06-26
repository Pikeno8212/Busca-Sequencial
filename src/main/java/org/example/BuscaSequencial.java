package org.example;

/**
 * Classe responsável por conter a lógica do algoritmo de busca sequencial.
 * A separação desta classe atende ao requisito de isolar a lógica da interface gráfica.
 */
public class BuscaSequencial {

    /**
     * Realiza a busca sequencial de um número em um array de inteiros.
     * 
     * @param lista O array de inteiros onde a busca será realizada.
     * @param alvo O número que desejamos encontrar.
     * @return O índice do elemento na lista se for encontrado; -1 caso contrário.
     */
    public static int buscaSequencial(int[] lista, int alvo) {
        // Percorre cada índice da lista iterativamente
        for (int i = 0; i < lista.length; i++) {
            // Verifica se o elemento na posição atual (índice i) é igual ao alvo
            if (lista[i] == alvo) {
                return i; // Se encontrar, retorna o índice correspondente imediatamente
            }
        }
        // Se o laço for concluído e o elemento não for encontrado, retorna -1
        return -1;
    }
}
