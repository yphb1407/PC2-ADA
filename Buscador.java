package dyv.adaavance;
public class Buscador {
    public static int linearSearch(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return i; // Devuelve la posición donde encontró el valor
            }
        }
        return -1; // Si no se encontró
    }

    public static void main(String[] args) {
        int[] data = {12, 34, 7, 25, 90, 11};
        int key = 25;

        int result = linearSearch(data, key);
        if (result == -1) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento encontrado en la posición: " + result);
        }
    }
}