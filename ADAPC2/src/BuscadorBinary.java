
public class BuscadorBinary {
    public static int BuscadorBinary(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] == key) {
                return mid; // Encontrado
            } else if (arr[mid] < key) {
                low = mid + 1; // Buscar en la mitad derecha
            } else {
                high = mid - 1; // Buscar en la mitad izquierda
            }
        }
        return -1; // No encontrado
    }
    public static void main(String[] args) {
        int[] data = {7, 11, 12, 25, 34, 90}; // IMPORTANTE: debe estar ordenado
        int key = 34;
        int result = BuscadorBinary(data, key);
        if (result == -1) {
            System.out.println("Elemento no encontrado");
        } else {
            System.out.println("Elemento encontrado en la posición: " + result);
        }
    }
}


//Es mucho mas rapido pero el arreglo debe de estar ordenado
